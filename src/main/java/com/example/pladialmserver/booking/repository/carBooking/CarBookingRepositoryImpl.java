package com.example.pladialmserver.booking.repository.carBooking;

import com.example.pladialmserver.booking.entity.CarBooking;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.product.car.entity.Car;
import com.example.pladialmserver.product.dto.response.ProductBookingRes;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    public List<ProductBookingRes> findResourceBookingByDate(Car car, LocalDate standardDate) {

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
