package com.example.pladialmserver.booking.repository.carBooking;

import com.example.pladialmserver.booking.entity.QCarBooking;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.example.pladialmserver.car.entity.QCar.car;
import static com.example.pladialmserver.resource.entity.QResource.resource;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class CarBookingRepositoryImpl implements CarBookingCustom{
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
