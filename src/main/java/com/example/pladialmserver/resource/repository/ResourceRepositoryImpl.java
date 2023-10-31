package com.example.pladialmserver.resource.repository;

import com.example.pladialmserver.booking.repository.resourceBooking.ResourceBookingRepository;
import com.example.pladialmserver.resource.dto.response.AdminResourcesRes;
import com.example.pladialmserver.resource.dto.response.QAdminResourcesRes;
import com.example.pladialmserver.resource.dto.response.QResourceRes;
import com.example.pladialmserver.resource.dto.response.ResourceRes;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.pladialmserver.resource.entity.QResource.resource;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class ResourceRepositoryImpl implements ResourceCustom{

    private final JPAQueryFactory jpaQueryFactory;
    private final ResourceBookingRepository resourceBookingRepository;

    private BooleanExpression resourceNameContaining(String resourceName) {
        return hasText(resourceName) ? resource.name.contains(resourceName) : null;
    }

    @Override
    public Page<AdminResourcesRes> search(String resourceName, Pageable pageable) {
        List<AdminResourcesRes> results = jpaQueryFactory
                .select(new QAdminResourcesRes(
                        resource.resourceId,
                        resource.name,
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

    @Override
    public Page<ResourceRes> findAvailableResources(String resourceName, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        List<Long> bookedResourceIds = new ArrayList<>();

        if (resourceName != null && startDate != null && endDate != null) {
            bookedResourceIds = resourceBookingRepository.findBookedResourceIdsByDateAndCarName(startDate, endDate, resourceName);
        }
        if (startDate != null && endDate != null) {
            bookedResourceIds = resourceBookingRepository.findBookedResourceIdsByDate(startDate, endDate);
        }

        List<ResourceRes> results;
        long total;

        //장비 전체 조회
        if (resourceName == null && startDate == null && endDate == null) {
            results = jpaQueryFactory
                    .select(new QResourceRes(
                            resource.resourceId,
                            resource.imgKey,
                            resource.name,
                            resource.location,
                            resource.description)
                    )
                    .from(resource)
                    .where(resource.isEnable.isTrue(),
                            resource.isActive.isTrue())
                    .orderBy(resource.name.asc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            total = jpaQueryFactory
                    .selectFrom(resource)
                    .where(resource.isEnable.isTrue(),
                            resource.isActive.isTrue())
                    .fetchCount();
        }
        //장비 조회 : 장비 이름만 검색한 경우/ 날짜와 시간만 입력한 경우/ 장비 이름과 날짜와 시간을 모두 입력할 경우
        else {
            results = jpaQueryFactory
                    .select(new QResourceRes(
                            resource.resourceId,
                            resource.imgKey,
                            resource.name,
                            resource.location,
                            resource.description)
                    )
                    .from(resource)
                    .where(resourceNameContaining(resourceName),
                            resource.isEnable.isTrue(),
                            resource.isActive.isTrue(),
                            //해당 날짜와 시간에 예약되어있는 장비를 제외하고 조회
                            resource.resourceId.notIn(bookedResourceIds))
                    .orderBy(resource.name.asc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            //쿼리를 실행하고 선택된 결과의 총 수를 반환
            total = jpaQueryFactory
                    .selectFrom(resource)
                    .where(resourceNameContaining(resourceName),
                            resource.isEnable.isTrue(),
                            resource.isActive.isTrue(),
                            resource.resourceId.notIn(bookedResourceIds))
                    .fetchCount();
        }
        return new PageImpl<>(results, pageable, total);
    }
}
