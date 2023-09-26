package com.example.pladialmserver.booking.repository;

import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.booking.entity.OfficeBooking;
import com.example.pladialmserver.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.pladialmserver.booking.entity.QOfficeBooking.officeBooking;

@RequiredArgsConstructor
public class OfficeBookingRepositoryImpl implements OfficeBookingCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<BookingRes> getBookingsByUser(User user, Pageable pageable) {
        List<OfficeBooking> bookings = jpaQueryFactory.selectFrom(officeBooking)
                .where(officeBooking.user.eq(user)
                        .and(officeBooking.isEnable.eq(true)))
                .orderBy(officeBooking.createdAt.desc())
                .fetch();

        List<BookingRes> res = bookings.stream()
                .map(BookingRes::toDto)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), res.size());
        return new PageImpl<>(res.subList(start, end), pageable, res.size());
    }

    @Override
    public Boolean existsByDateAndTime(LocalDate date, LocalTime startTime, LocalTime endTime) {
        Integer fetchOne = jpaQueryFactory.selectOne()
                .from(officeBooking)
                .where(officeBooking.date.eq(date)
                        .and(officeBooking.startTime.loe(startTime).and(officeBooking.endTime.gt(startTime)))
                        .or(officeBooking.startTime.lt(endTime).and(officeBooking.endTime.goe(endTime))))
                .fetchFirst();
        return fetchOne != null;
    }
}