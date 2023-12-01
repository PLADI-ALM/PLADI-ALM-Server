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

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.example.pladialmserver.booking.entity.QCarBooking.carBooking;

@RequiredArgsConstructor
public class CarBookingRepositoryImpl implements CarBookingCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;

    @Override
    public boolean existsDateTime(Car car, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // 1. 예약중 & 사용중인 날짜들
        List<CarBooking> bookings = jpaQueryFactory.selectFrom(carBooking)
                .where(carBooking.car.eq(car)
                        .and(carBooking.status.notIn(BookingStatus.CANCELED, BookingStatus.FINISHED)))
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
                        (carBooking.status.notIn(BookingStatus.CANCELED)),
                        (carBooking.startDate.loe(standardDate)),
                        (carBooking.endDate.after(standardDate))
                ).orderBy(carBooking.startDate.asc())
                .fetchOne();

        return ProductBookingRes.toDto(booking);
    }

    @Override
    public List<String> getCarBookedDate(Car car, LocalDate standardDate) {
        // 해당 월의 첫 날 (00:00)
        LocalDateTime startDateTime = standardDate.withDayOfMonth(1).atStartOfDay();
        // 해당 월의 마지막 날 (23:00)
        LocalDateTime endDateTime = standardDate.withDayOfMonth(standardDate.lengthOfMonth()).atTime(23, 0);

        // 해당 월의 예약 현황 조회
        List<CarBooking> bookings = jpaQueryFactory.selectFrom(carBooking)
                .where(carBooking.car.eq(car)
                        .and(carBooking.status.notIn(BookingStatus.CANCELED))
                        .and(carBooking.startDate.loe(endDateTime)
                                .and(carBooking.endDate.after(startDateTime))
                                .or(carBooking.startDate.before(startDateTime).and(carBooking.endDate.after(endDateTime)))
                        )
                ).orderBy(carBooking.startDate.asc())
                .fetch();

        Map<LocalDate, Integer> datesOfMonth = createDatesOfMonth(standardDate.getYear(), standardDate.getMonth());
        for (LocalDate date : datesOfMonth.keySet()) {
            List<LocalDateTime> hoursList = IntStream.range(0, 24)
                    .mapToObj(date.atStartOfDay()::plusHours)
                    .collect(Collectors.toList());

            for (CarBooking b : bookings) {
                for (LocalDateTime dateTime : hoursList) {
                    if ((dateTime.isAfter(b.getStartDate()) && dateTime.isBefore(b.getEndDate())) || dateTime.isEqual(b.getStartDate()))
                        // 시간마다 +1
                        datesOfMonth.put(date, datesOfMonth.get(date) + 1);
                }
            }
        }

        return datesOfMonth.entrySet().stream()
                // 예약이 모두 된 날짜 반환
                .filter(entry -> entry.getValue() == 24)
                .map(date -> DateTimeUtil.dateToString(date.getKey()))
                .sorted()
                .collect(Collectors.toList());
    }

    private Map<LocalDate, Integer> createDatesOfMonth(int year, Month month) {
        return IntStream.rangeClosed(1, month.maxLength())
                .mapToObj(day -> LocalDate.of(year, month, day))
                .collect(Collectors.toMap(date -> date, date -> 0));
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
                        .and(carBooking.status.notIn(BookingStatus.CANCELED))
                        .and(carBooking.startDate.loe(endDateTime)
                                .and(carBooking.endDate.after(startDateTime))
                                .or(carBooking.startDate.before(startDateTime).and(carBooking.endDate.after(endDateTime)))
                        )
                ).orderBy(carBooking.startDate.asc())
                .fetch();

        // 0시부터 24시
        List<LocalDateTime> hoursList = IntStream.range(0, 24)
                .mapToObj(startDateTime::plusHours)
                .collect(Collectors.toList());

        List<String> answer = new ArrayList<>();
        for (CarBooking b : bookings) {
            for (LocalDateTime dateTime : hoursList) {
                if ((dateTime.isAfter(b.getStartDate()) && dateTime.isBefore(b.getEndDate())) || dateTime.isEqual(b.getStartDate()))
                    answer.add(DateTimeUtil.dateTimeToStringTime(dateTime));
            }
        }

        return answer;
    }

    @Override
    public List<ProductBookingRes> findCarBookingByDate(Car car, LocalDate standardDate) {
        // 기준 날짜의 첫 시간 (00:00)
        LocalDateTime startDateTime = standardDate.atStartOfDay();
        // 기준 날짜의 마지막 시간 (23:00)
        LocalDateTime endDateTime = standardDate.atTime(23, 0);

        // 기준 날짜에 포함된 예약
        List<CarBooking> bookings = jpaQueryFactory.selectFrom(carBooking)
                .where(carBooking.car.eq(car)
                        .and(carBooking.status.notIn(BookingStatus.CANCELED))
                        .and(carBooking.startDate.loe(endDateTime)
                                .and(carBooking.endDate.after(startDateTime))
                                .or(carBooking.startDate.before(startDateTime).and(carBooking.endDate.after(endDateTime)))
                        )
                ).orderBy(carBooking.startDate.asc())
                .fetch();

        return bookings.stream().map(ProductBookingRes::toDto).collect(Collectors.toList());
    }

    // 회원 탈퇴를 위한 예약 상태 변경
    @Override
    public void updateBookingStatusForResigning(User user) {
        jpaQueryFactory.update(carBooking)
                .set(carBooking.status, BookingStatus.CANCELED)
                .where(carBooking.user.eq(user)
                        .and(carBooking.status.in(BookingStatus.WAITING, BookingStatus.BOOKED, BookingStatus.USING)))
                .execute();

        // 영속성 컨텍스트를 DB 에 즉시 반영
        entityManager.flush();
    }
}
