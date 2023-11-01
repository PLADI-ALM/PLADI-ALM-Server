package com.example.pladialmserver.booking.repository.carBooking;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CarBookingRepositoryImpl implements CarBookingCustom {
//    private final JPAQueryFactory jpaQueryFactory;
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
