package com.example.pladialmserver.booking.repository.resourceBooking;

import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.resource.entity.Resource;
import com.example.pladialmserver.global.utils.DateTimeUtil;
import com.example.pladialmserver.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.pladialmserver.booking.entity.QResourceBooking.resourceBooking;


@RequiredArgsConstructor
public class ResourceBookingRepositoryImpl implements ResourceBookingCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<BookingRes> getBookingsByUser(User user, Pageable pageable) {
        List<ResourceBooking> bookings = jpaQueryFactory.selectFrom(resourceBooking)
                .where(resourceBooking.user.eq(user)
                        .and(resourceBooking.isEnable.eq(true)))
                .orderBy(resourceBooking.createdAt.desc())
                .fetch();

        List<BookingRes> res = bookings.stream()
                .map(BookingRes::toDto)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), res.size());
        return new PageImpl<>(res.subList(start, end), pageable, res.size());
    }

    // 자원 월별 예약 현황 조회
    @Override
    public List<String> getResourceBookedDate(Resource resource, LocalDate standardDate) {
        // 해당 월의 첫 날
        LocalDate startDate = standardDate.withDayOfMonth(1);
        // 해당 월의 마지막 날
        LocalDate endDate = standardDate.withDayOfMonth(standardDate.lengthOfMonth());

        // 해당 월의 예약 현황 조회
        List<ResourceBooking> bookings = jpaQueryFactory.selectFrom(resourceBooking)
                .where(resourceBooking.resource.eq(resource)
                        .and(resourceBooking.status.in(BookingStatus.WAITING, BookingStatus.BOOKED, BookingStatus.USING))
                        .and((resourceBooking.startDate.between(startDate, endDate))
                        .or(resourceBooking.endDate.between(startDate, endDate))))
                .orderBy(resourceBooking.startDate.asc())
                .fetch();

        // 예약 시작 ~ 끝 날짜 반환
        List<String> bookedDate = new ArrayList<>();
        for (ResourceBooking b : bookings) {
            List<String> date = b.getStartDate().datesUntil(b.getEndDate().plusDays(1))
                    .map(DateTimeUtil::dateToString)
                    .collect(Collectors.toList());
            bookedDate.addAll(date);
        }

        return bookedDate;
    }

    @Override
    public boolean existsDate(Resource resource, LocalDate startDate, LocalDate endDate) {

        // 1. 예약중 & 사용중인 날짜들
        List<ResourceBooking> bookings = jpaQueryFactory.selectFrom(resourceBooking)
                .where(resourceBooking.resource.eq(resource)
                        .and(resourceBooking.status.in(BookingStatus.BOOKED, BookingStatus.USING)))
                .orderBy(resourceBooking.startDate.asc())
                .fetch();

        List<LocalDate> bookedDate = new ArrayList<>();
        for (ResourceBooking b : bookings) {
            List<LocalDate> date = b.getStartDate().datesUntil(b.getEndDate().plusDays(1)).collect(Collectors.toList());
            bookedDate.addAll(date);
        }

        // 2. startDate ~ endDate
        List<LocalDate> date = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());

        // 2번이 1번에 있는지 여부
        for (LocalDate localDate : date) {
            if(bookedDate.contains(localDate)) return true;
        }

        return false;
    }
}
