package com.example.pladialmserver.office.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class OfficeRepositoryImpl implements OfficeCustom{
    private final JPAQueryFactory jpaQueryFactory;



}
