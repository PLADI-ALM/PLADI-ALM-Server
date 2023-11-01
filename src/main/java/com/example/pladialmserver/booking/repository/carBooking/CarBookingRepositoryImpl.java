package com.example.pladialmserver.booking.repository.carBooking;

import com.example.pladialmserver.booking.entity.CarBooking;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.product.car.entity.Car;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
