package com.example.pladialmserver.booking.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OfficeBookingRepositoryImpl implements OfficeBookingCustom{
    private final JPAQueryFactory jpaQueryFactory;

}
