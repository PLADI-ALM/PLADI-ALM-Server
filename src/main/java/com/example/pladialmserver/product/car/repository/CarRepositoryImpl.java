package com.example.pladialmserver.product.car.repository;

import com.example.pladialmserver.product.car.dto.CarRes;
import com.example.pladialmserver.product.car.dto.QCarRes;
import com.example.pladialmserver.product.resource.dto.response.AdminProductsRes;
import com.example.pladialmserver.product.resource.dto.response.QAdminResourcesRes;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.pladialmserver.product.car.entity.QCar.car;

@RequiredArgsConstructor
public class CarRepositoryImpl implements CarCustom {
    private final JPAQueryFactory jpaQueryFactory;



    private BooleanExpression carNameContaining(String carName) {
        return StringUtils.hasText(carName) ? car.name.contains(carName) : null;
    }
    //분기문 case 동적쿼리로 처리한 메서드
    private Predicate carFilter(String carName, List<Long> bookedCarIds) {
        //전체 장비 조회하는 경우
        BooleanExpression predicate = car.isEnable.isTrue().and(car.isActive.isTrue());

        //장비 이름 검색한 경우
        if (StringUtils.hasText(carName)) {
            predicate = predicate.and(carNameContaining(carName));
        }
        //날짜와 시간을 입력한 경우
        if (!bookedCarIds.isEmpty()) {
            predicate = predicate.and(car.carId.notIn(bookedCarIds));
        }

        return predicate;
    }

    @Override
    public Page<CarRes> findAvailableCars(String carName, List<Long> bookedCarIds, Pageable pageable) {
        Predicate filterPredicate = carFilter(carName, bookedCarIds);

        List<CarRes> results = jpaQueryFactory
                .select(new QCarRes(
                        car.carId,
                        car.imgKey,
                        car.name,
                        car.manufacturer,
                        car.location,
                        car.description)
                )
                .from(car)
                .where(filterPredicate)
                .orderBy(car.name.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = jpaQueryFactory
                .selectFrom(car)
                .where(filterPredicate)
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<AdminProductsRes> search(String carName, Pageable pageable) {
        List<AdminProductsRes> results = jpaQueryFactory
                .select(new QAdminResourcesRes(
                        car.carId,
                        car.name,
                        car.manufacturer,
                        car.location,
                        car.user.name,
                        car.user.phone,
                        car.description,
                        car.isActive)
                )
                .from(car)
                .where(
                        carNameContaining(carName),
                        car.isEnable.eq(true)
                )
                .orderBy(car.name.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = results.size();
        return new PageImpl<>(results, pageable, total);
    }


}
