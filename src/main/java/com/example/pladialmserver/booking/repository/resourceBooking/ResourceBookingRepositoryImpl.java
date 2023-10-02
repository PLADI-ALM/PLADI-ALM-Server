package com.example.pladialmserver.booking.repository.resourceBooking;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ResourceBookingRepositoryImpl implements ResourceBookingCustom{
    private final JPAQueryFactory jpaQueryFactory;
}
