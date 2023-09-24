package com.example.pladialmserver.booking.repository;

import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.booking.entity.OfficeBooking;
import com.example.pladialmserver.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
}