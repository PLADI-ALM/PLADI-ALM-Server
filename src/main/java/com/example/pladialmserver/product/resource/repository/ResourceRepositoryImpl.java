package com.example.pladialmserver.product.resource.repository;

import com.example.pladialmserver.product.resource.dto.response.AdminResourcesRes;
import com.example.pladialmserver.resource.dto.response.QAdminResourcesRes;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.pladialmserver.resource.entity.QResource.resource;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class ResourceRepositoryImpl implements ResourceCustom{

    private final JPAQueryFactory jpaQueryFactory;

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
}
