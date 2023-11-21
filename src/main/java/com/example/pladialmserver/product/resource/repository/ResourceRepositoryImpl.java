package com.example.pladialmserver.product.resource.repository;

import com.example.pladialmserver.product.resource.dto.response.AdminProductsRes;
import com.example.pladialmserver.product.resource.dto.response.QAdminResourcesRes;
import com.example.pladialmserver.product.resource.dto.response.QResourceRes;
import com.example.pladialmserver.product.resource.dto.response.ResourceRes;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.pladialmserver.product.resource.entity.QResource.resource;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class ResourceRepositoryImpl implements ResourceCustom {

    private final JPAQueryFactory jpaQueryFactory;

    private BooleanExpression resourceNameContaining(String resourceName) {
        return hasText(resourceName) ? resource.name.contains(resourceName) : null;
    }

    @Override
    public Page<AdminProductsRes> search(String resourceName, Pageable pageable) {
        List<AdminProductsRes> results = jpaQueryFactory
                .select(new QAdminResourcesRes(
                        resource.resourceId,
                        resource.name,
                        resource.manufacturer,
                        resource.location,
                        resource.user.name,
                        resource.user.phone,
                        resource.description,
                        resource.isActive)
                )
                .from(resource)
                .where(
                        resourceNameContaining(resourceName),
                        resource.isEnable.eq(true)
                )
                .orderBy(resource.name.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = results.size();
        return new PageImpl<>(results, pageable, total);
    }

    //분기문 case 동적쿼리로 처리한 메서드
    private Predicate resourceFilter(String resourceName, List<Long> bookedResourceIds) {
        //전체 장비 조회하는 경우
        BooleanExpression predicate = resource.isEnable.isTrue().and(resource.isActive.isTrue());

        //장비 이름 검색한 경우
        if (StringUtils.hasText(resourceName)) {
            predicate = predicate.and(resourceNameContaining(resourceName));
        }
        //날짜와 시간을 입력한 경우
        if (!bookedResourceIds.isEmpty()) {
            predicate = predicate.and(resource.resourceId.notIn(bookedResourceIds));
        }

        return predicate;
    }

    @Override
    public Page<ResourceRes> findAvailableResources(String resourceName,  List<Long> bookedResourceIds, Pageable pageable) {
        Predicate filterPredicate = resourceFilter(resourceName, bookedResourceIds);

        List<ResourceRes> results = jpaQueryFactory
                    .select(new QResourceRes(
                            resource.resourceId,
                            resource.imgKey,
                            resource.name,
                            resource.manufacturer,
                            resource.location,
                            resource.description)
                    )
                    .from(resource)
                    .where(filterPredicate)
                    .orderBy(resource.name.asc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            long total = jpaQueryFactory
                    .selectFrom(resource)
                    .where(filterPredicate)
                    .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }
}
