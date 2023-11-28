package com.example.pladialmserver.office.repository.facility;

import com.example.pladialmserver.office.entity.Facility;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.pladialmserver.office.entity.QFacility.facility;


@RequiredArgsConstructor
public class FacilityRepositoryImpl implements FacilityCustom {
    private final JPAQueryFactory jpaQueryFactory;

    private BooleanExpression findKeyword(String name) {
        return StringUtils.hasText(name) ? facility.name.contains(name) : null;
    }

    @Override
    public List<String> findByNameContainingAndIsActive(String name) {
        return jpaQueryFactory.selectFrom(facility)
                .where(findKeyword(name), facility.isEnable.eq(true))
                .fetch()
                .stream().map(Facility::getName)
                .collect(Collectors.toList());
    }
}
