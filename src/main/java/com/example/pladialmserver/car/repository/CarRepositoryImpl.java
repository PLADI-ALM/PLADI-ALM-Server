package com.example.pladialmserver.car.repository;

import com.example.pladialmserver.booking.entity.QCarBooking;
import com.example.pladialmserver.booking.repository.carBooking.CarBookingRepository;
import com.example.pladialmserver.car.dto.CarRes;
import com.example.pladialmserver.car.dto.QCarRes;
import com.example.pladialmserver.car.entity.Car;
import com.example.pladialmserver.car.entity.QCar;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

import static com.example.pladialmserver.car.entity.QCar.car;
import static io.jsonwebtoken.lang.Strings.hasText;

@RequiredArgsConstructor
public class CarRepositoryImpl implements CarCustom {
//    private final JPAQueryFactory jpaQueryFactory;
//    private final CarBookingRepository carBookingRepository;
//
//    private BooleanExpression carNameContaining(String carName) {
//        return StringUtils.hasText(carName) ? car.name.contains(carName) : null;
//    }
//    @Override
//    public Page<CarRes> findAvailableCars(String carName, LocalDate startDate, LocalDate endDate, Pageable pageable) {
//        List<Long> bookedCarIds = carBookingRepository.findBookedCarIdsByDateAndCarName(startDate, endDate, carName);
//
//        List<CarRes> results = jpaQueryFactory
//                .select(new QCarRes(
//                        car.carId,
//                        car.imgKey,
//                        car.name,
//                        car.location,
//                        car.description)
//                )
//                .from(car)
//                .where(carNameContaining(carName),
//                        car.isEnable.isTrue(),
//                        car.isActive.isTrue(),
//                        car.carId.notIn(bookedCarIds))
//                .orderBy(car.name.asc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        long total = jpaQueryFactory
//                .selectFrom(car)
//                .where(carNameContaining(carName),
//                        car.isEnable.isTrue(),
//                        car.isActive.isTrue(),
//                        car.carId.notIn(bookedCarIds))
//                .fetchCount();
//
//        return new PageImpl<>(results, pageable, total);
//    }


    //TODO:동적 쿼리로 빼기 가능할듯(고민해야되는 부분)
//    @Override
//    public Page<CarRes> findAllCars(Pageable pageable) {
//        List<CarRes> results = jpaQueryFactory
//                .select(new QCarRes(
//                        QCar.car.carId,
//                        QCar.car.imgKey,
//                        QCar.car.name,
//                        QCar.car.location,
//                        QCar.car.description)
//                )
//                .from(QCar.car)
//                .where(QCar.car.isEnable.isTrue(),
//                        QCar.car.isActive.isTrue())
//                .orderBy(QCar.car.name.asc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        long total = jpaQueryFactory
//                .selectFrom(QCar.car)
//                .where(QCar.car.isEnable.isTrue(),
//                        QCar.car.isActive.isTrue())
//                .fetchCount();
//
//        return new PageImpl<>(results, pageable, total);
//    }


}
