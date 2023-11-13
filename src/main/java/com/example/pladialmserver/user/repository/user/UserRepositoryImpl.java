package com.example.pladialmserver.user.repository.user;

import com.example.pladialmserver.user.entity.Affiliation;
import com.example.pladialmserver.user.entity.Department;
import com.example.pladialmserver.user.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
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

    private BooleanExpression findDepartment(Department department) {
        return department != null ? user.department.eq(department) : null;
    }
    private BooleanExpression findAffiliation(Affiliation affiliation) {
        return affiliation != null ? user.affiliation.eq(affiliation) : null;
    }

    private JPAQuery<User> getUserListByName(String name) {
        return jpaQueryFactory.selectFrom(user)
                .where(user.isEnable.eq(true).and(findKeyword(name)));
    }

    @Override
    public Page<User> findAllByName(String name, Department department, Affiliation affiliation, Pageable pageable) {
        List<User> content = getUserListByName(name)
                .where(
                        findDepartment(department),
                        findAffiliation(affiliation)
                        )
                .fetch();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), content.size());
        return new PageImpl<>(content.subList(start, end), pageable, content.size());
    }

    @Override
    public List<User> findAllByName(String name) {
        return getUserListByName(name)
                .fetch();
    }
}