package com.example.pladialmserver.booking.repository.carBooking;

import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.booking.entity.CarBooking;
import com.example.pladialmserver.booking.entity.QCarBooking;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.global.utils.DateTimeUtil;
import com.example.pladialmserver.product.car.entity.Car;
import com.example.pladialmserver.product.dto.response.ProductBookingRes;
import com.example.pladialmserver.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.pladialmserver.booking.entity.QCarBooking.carBooking;

@RequiredArgsConstructor
public class CarBookingRepositoryImpl implements CarBookingCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean existsDateTime(Car car, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // 1. 예약중 & 사용중인 날짜들
        List<CarBooking> bookings = jpaQueryFactory.selectFrom(carBooking)
                .where(carBooking.car.eq(car)
                        .and(carBooking.status.in(BookingStatus.BOOKED, BookingStatus.USING)))
                .orderBy(carBooking.startDate.asc())
                .fetch();

        if (bookings.isEmpty()) return false;
        for (CarBooking b : bookings) {
            // 시작 날짜 또는 종료 날짜가 사이에 있다면
            if (!startDateTime.isBefore(b.getStartDate()) && startDateTime.isBefore(b.getEndDate())) return true;
            if (!endDateTime.isBefore(b.getStartDate()) && endDateTime.isBefore(b.getEndDate())) return true;
        }
        return false;
    }

    @Override
    public ProductBookingRes findCarBookingByDate(Car car, LocalDateTime standardDate) {

        // 해당 날짜가 포함된 예약
        CarBooking booking = jpaQueryFactory
                .selectFrom(carBooking)
                .where(carBooking.car.eq(car),
                        (carBooking.status.in(BookingStatus.WAITING, BookingStatus.BOOKED, BookingStatus.USING)),
                        (carBooking.startDate.before(standardDate)),
                        (carBooking.endDate.after(standardDate))
                ).orderBy(carBooking.startDate.asc())
                .fetchOne();

        return ProductBookingRes.toDto(booking);
    }

    @Override
    public List<String> getCarBookedDate(Car car, LocalDate standardDate, LocalDate date) {
        // 해당 월의 첫 날 (00:00)
        LocalDateTime startDateTime = standardDate.withDayOfMonth(1).atStartOfDay();
        // 다음 월의 첫 날 (00:00)
        LocalDateTime endDateTime = standardDate.plusMonths(1).atStartOfDay();

        // 이후 가장 가까운 예약 날짜&시간
        if (date != null) {
            // 조회 일의 첫 날 (00:00)
            LocalDateTime dateTime = date.atStartOfDay();
            // 가장 가까운 예약 현황 조회
            CarBooking booking = jpaQueryFactory.selectFrom(carBooking)
                    .where(carBooking.car.eq(car),
                            carBooking.status.in(BookingStatus.WAITING, BookingStatus.BOOKED, BookingStatus.USING),
                            carBooking.startDate.after(dateTime))
                    .orderBy(carBooking.startDate.asc())
                    .fetchFirst();

            return Optional.ofNullable(booking)
                    .map(b -> Collections.singletonList(DateTimeUtil.dateTimeToString(b.getStartDate())))
                    .orElse(null);
        } else {
            // 해당 월의 예약 현황 조회
            List<CarBooking> bookings = jpaQueryFactory.selectFrom(carBooking)
                    .where(carBooking.car.eq(car)
                            .and(carBooking.status.in(BookingStatus.WAITING, BookingStatus.BOOKED, BookingStatus.USING))
                            .and((carBooking.startDate.between(startDateTime, endDateTime))
                                    .or(carBooking.endDate.between(startDateTime, endDateTime)))
                    ).orderBy(carBooking.startDate.asc())
                    .fetch();

            // 예약이 모두 된 날짜(첫 날 0시 ~ 다음 날 0시) 반환
            List<String> bookedDate = new ArrayList<>();
            int index = 0;
            boolean isContinuity = false;
            LocalDateTime standard = null;

            for (CarBooking b : bookings) {
                index++;

                // 연속 유무 및 연속 기준일 체크
                if (index == 1) {
                    standard = bookings.get(0).getEndDate();
                    if (isMidnight(bookings.get(0).getStartDate())) isContinuity = true;
                } else {
                    isContinuity = (standard.isEqual(b.getStartDate()) || isMidnight(b.getStartDate()));
                }

                // 시작일 & 종료일 다른 경우
                if (!DateTimeUtil.dateTimeToDate(b.getStartDate()).isEqual(DateTimeUtil.dateTimeToDate(b.getEndDate()))) {
                    // 연속인 경우 & 다음 날 00시 또는 이상인 경우 -> startDate 더해주기
                    if (isContinuity &&
                            b.getEndDate().isAfter(DateTimeUtil.getMidNightDateTime(b.getEndDate().plusDays(1))) || b.getEndDate().isEqual(DateTimeUtil.getMidNightDateTime(b.getEndDate().plusDays(1)))) {
                        bookedDate.add(DateTimeUtil.dateToString(b.getStartDate().toLocalDate()));
                    } else if (!isContinuity && ChronoUnit.DAYS.between(b.getStartDate(), b.getEndDate()) == 1) {
                        break;
                    } else {
                        // 중간 날짜 더해주기
                        List<String> dates = b.getStartDate().toLocalDate().datesUntil(b.getEndDate().toLocalDate())
                                .map(DateTimeUtil::dateToString)
                                .collect(Collectors.toList());
                        bookedDate.addAll(dates);
                    }
                }
                // 시작일 & 종료일 동일한 경우
                else {
                    if (!isContinuity && bookings.size() > index)
                        isContinuity = isMidnight(bookings.get(index).getStartDate());
                }
                // 모두 수행
                standard = b.getEndDate();
            }
            return bookedDate;
        }
    }

    private boolean isMidnight(LocalDateTime localDateTime) {
        return DateTimeUtil.dateTimeToTime(localDateTime).equals(LocalTime.MIN);
    }


    @Override
    public Page<BookingRes> getBookingsByUser(User user, Pageable pageable) {
        List<CarBooking> bookings = jpaQueryFactory.selectFrom(carBooking)
                .where(carBooking.user.eq(user)
                        .and(carBooking.isEnable.eq(true)))
                .orderBy(carBooking.createdAt.desc())
                .fetch();

        List<BookingRes> res = bookings.stream()
                .map(BookingRes::toDto)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), res.size());
        return new PageImpl<>(res.subList(start, end), pageable, res.size());
    }

    @Override
    public List<Long> findBookedCarIdsByDate(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaQueryFactory
                .select(QCarBooking.carBooking.car.carId)
                .from(QCarBooking.carBooking)
                .where(QCarBooking.carBooking.startDate.before(endDate),
                        QCarBooking.carBooking.endDate.after(startDate),
                        QCarBooking.carBooking.status.notIn(BookingStatus.CANCELED, BookingStatus.FINISHED))
                .fetch();
    }

    @Override
    public List<String> getBookedTime(Car car, LocalDate standardDate) {
        // 기준 날짜의 첫 시간 (00:00)
        LocalDateTime startDateTime = standardDate.atStartOfDay();
        // 기준 날짜의 마지막 시간 (23:00)
        LocalDateTime endDateTime = standardDate.atTime(23, 0);

        // 기준 날짜에 포함된 예약
        List<CarBooking> bookings = jpaQueryFactory.selectFrom(carBooking)
                .where(carBooking.car.eq(car)
                        .and(carBooking.status.in(BookingStatus.WAITING, BookingStatus.BOOKED, BookingStatus.USING))
                        .and((carBooking.startDate.between(startDateTime, endDateTime))
                                .or(carBooking.endDate.between(startDateTime, endDateTime)))
                ).orderBy(carBooking.startDate.asc())
                .fetch();

        // 0시부터 24시
        List<LocalDateTime> hoursList = IntStream.range(0, 24)
                .mapToObj(startDateTime::plusHours)
                .collect(Collectors.toList());

        List<String> answer = new ArrayList<>();
        for (CarBooking b : bookings) {
            for (LocalDateTime dateTime : hoursList) {
                if ((dateTime.isAfter(b.getStartDate()) && dateTime.isBefore(b.getEndDate())) || dateTime.isEqual(b.getStartDate()) || dateTime.isEqual(b.getEndDate()))
                    answer.add(DateTimeUtil.dateTimeToStringTime(dateTime));
            }
        }

        return answer;
    }
}
