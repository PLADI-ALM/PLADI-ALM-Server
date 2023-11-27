package com.example.pladialmserver.office.repository.office;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.pladialmserver.office.entity.QOffice.office;
import static com.example.pladialmserver.office.entity.QOfficeFacility.officeFacility;


@RequiredArgsConstructor
public class OfficeRepositoryImpl implements OfficeCustom{
    private final JPAQueryFactory jpaQueryFactory;
    BooleanExpression predicate = office.isEnable.isTrue().and(office.isActive.isTrue());



//    @Override
//    public Page<OfficeRes> findAvailableOffices(String facilityName, List<Long> bookedOfficeIds, Pageable pageable) {
//        // 시설 이름을 기반으로 필터링
//        if (StringUtils.hasText(facilityName)) {
//            predicate = predicate.and(officeFacility.facility.name.eq(facilityName)
//                    .and(officeFacility.office.eq(office)));
//        }
//
//        // 이미 예약된 회의실 제외
//        if (!bookedOfficeIds.isEmpty()) {
//            predicate = predicate.and(office.officeId.notIn(bookedOfficeIds));
//        }
//
//        List<OfficeRes> results = jpaQueryFactory
//                .select(new QOfficeRes(
//                        office.officeId,
//                        office.name,
//                        office.location,
//                        office.capacity,
//                        office.description,
//                        office.imgKey,
//                        office.isActive))
//                .from(office)
//                .leftJoin(officeFacility).on(officeFacility.office.eq(office))
//                .leftJoin(facility).on(officeFacility.facility.eq(facility))
//                .where(predicate)
//                .orderBy(office.name.asc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        long total = jpaQueryFactory
//                .selectFrom(office)
//                .where(predicate)
//                .fetchCount();
//
//        // 시설 목록을 각 OfficeRes 객체에 추가
//        results.forEach(officeRes -> {
//            List<String> facilityNames = jpaQueryFactory
//                    .select(facility.name)
//                    .from(officeFacility)
//                    .leftJoin(facility).on(officeFacility.facility.eq(facility))
//                    .where(officeFacility.office.officeId.eq(officeRes.getOfficeId()))
//                    .fetch();
//            officeRes.setFacilityList(facilityNames);
//        });
//
//        return new PageImpl<>(results, pageable, total);
//    }
}
