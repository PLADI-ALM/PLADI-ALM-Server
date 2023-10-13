package com.example.pladialmserver.user.repository.user;

import com.example.pladialmserver.user.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.example.pladialmserver.user.entity.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserCustom {
    private final JPAQueryFactory jpaQueryFactory;

    private BooleanExpression findKeyword(String name) {
        return StringUtils.hasText(name) ? user.name.contains(name) : null;
    }

    @Override
    public Page<User> findAllByName(String name, Pageable pageable) {
        List<User> content = jpaQueryFactory.selectFrom(user)
                .where(findKeyword(name))
                .fetch();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), content.size());
        return new PageImpl<>(content.subList(start, end), pageable, content.size());
    }
}