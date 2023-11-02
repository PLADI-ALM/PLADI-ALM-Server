package com.example.pladialmserver.booking.repository.carBooking;

import com.example.pladialmserver.booking.entity.CarBooking;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.global.utils.DateTimeUtil;
import com.example.pladialmserver.product.car.entity.Car;
import com.example.pladialmserver.product.dto.response.ProductBookingRes;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<ProductBookingRes> findCarBookingByDate(Car car, LocalDate standardDate) {

        // 해당 일의 00:00
        LocalDateTime startDateTime = LocalDateTime.of(standardDate, LocalTime.MIN);
        // 해당 일의 23:59
        LocalDateTime endDateTime = LocalDateTime.of(standardDate, LocalTime.MAX);

        // 해당 날짜가 포함된 예약
        List<CarBooking> bookings = jpaQueryFactory
                .selectFrom(carBooking)
                .where(carBooking.car.eq(car),
                        (carBooking.status.in(BookingStatus.WAITING, BookingStatus.BOOKED, BookingStatus.USING)),
                        ((carBooking.startDate.between(startDateTime, endDateTime))
                                .or(carBooking.endDate.between(startDateTime, endDateTime)))
                ).orderBy(carBooking.startDate.asc())
                .fetch();

        return bookings.stream().map(ProductBookingRes::toDto).collect(Collectors.toList());
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

            return Collections.singletonList(DateTimeUtil.dateTimeToString(booking.getStartDate()));
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
                    }
                    // 중간 날짜 더해주기
                    List<String> dates = b.getStartDate().toLocalDate().datesUntil(b.getEndDate().toLocalDate())
                            .map(DateTimeUtil::dateToString)
                            .collect(Collectors.toList());
                    bookedDate.addAll(dates);
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

//
//
//    private BooleanExpression carNameContaining(String carName) {
//        return hasText(carName) ? car.name.contains(carName) : null;
//    }
//    @Override
//    public List<Long> findBookedCarIdsByDateAndCarName(LocalDate startDate, LocalDate endDate, String carName) {
//        return jpaQueryFactory
//                .select(QCarBooking.carBooking.car.carId)
//                .from(QCarBooking.carBooking)
//                .where(
//                QCarBooking.carBooking.startDate.before(endDate.atTime(23, 59, 59)),
//                QCarBooking.carBooking.endDate.after(startDate.atStartOfDay()),
//                        carNameContaining(carName))
////                        QCarBooking.carBooking.car.name.like("%" + carName + "%"))
//                .fetch();
//        }
}
