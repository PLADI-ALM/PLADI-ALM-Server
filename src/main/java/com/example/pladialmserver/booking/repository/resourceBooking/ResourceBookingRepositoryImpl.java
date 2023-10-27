package com.example.pladialmserver.booking.repository.resourceBooking;

import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.global.utils.DateTimeUtil;
import com.example.pladialmserver.resource.entity.Resource;
import com.example.pladialmserver.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.pladialmserver.booking.entity.QResourceBooking.resourceBooking;


@RequiredArgsConstructor
public class ResourceBookingRepositoryImpl implements ResourceBookingCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;

    // 장비 예약 목록 조회
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

    // 장비 월별 예약 현황 조회
    @Override
    public List<String> getResourceBookedDate(Resource resource, LocalDate standardDate) {
        // 해당 월의 첫 날 (00:00)
        LocalDateTime startDateTime = standardDate.withDayOfMonth(1).atStartOfDay();
        // 다음 월의 첫 날 (00:00)
        LocalDateTime endDateTime = standardDate.plusMonths(1).atStartOfDay();
        // 해당 월의 예약 현황 조회
        List<ResourceBooking> bookings = jpaQueryFactory.selectFrom(resourceBooking)
                .where(resourceBooking.resource.eq(resource)
                        .and(resourceBooking.status.in(BookingStatus.WAITING, BookingStatus.BOOKED, BookingStatus.USING))
                        .and((resourceBooking.startDate.between(startDateTime, endDateTime))
                                .or(resourceBooking.endDate.between(startDateTime, endDateTime)))
                ).orderBy(resourceBooking.startDate.asc())
                .fetch();

        // 예약이 모두 된 날짜(첫 날 0시 ~ 다음 날 0시) 반환
        List<String> bookedDate = new ArrayList<>();
        int index = 0;
        boolean isContinuity = false;
        LocalDateTime standard = null;

        for (ResourceBooking b : bookings) {
            index++;

            // 연속 유무 및 연속 기준일 체크
            if (index == 1) {
                standard = bookings.get(0).getEndDate();
                if (isMidnight(bookings.get(0).getStartDate())) isContinuity = true;
            } else {
                isContinuity = (standard.isEqual(b.getStartDate()) || isMidnight(b.getStartDate()));
            }

            // 시작일 & 종료일 다른 경우
            if (!DateTimeUtil.dateTimeToDate(b.getStartDate()).isEqual(DateTimeUtil.dateTimeToDate(b.getEndDate()))) {
                // 연속인 경우
                if (isContinuity) {
                    // 다음 날 00시 이상인 경우 -> startDate 더해주기
                    if (b.getEndDate().isAfter(DateTimeUtil.getMidNightDateTime(b.getEndDate().plusDays(1)))
                            || b.getEndDate().isEqual(DateTimeUtil.getMidNightDateTime(b.getEndDate().plusDays(1)))) {
                        bookedDate.add(DateTimeUtil.dateToString(b.getStartDate().toLocalDate()));
                    }
                }
                // 중간 날짜 더해주기
                List<String> date = b.getStartDate().toLocalDate().datesUntil(b.getEndDate().toLocalDate())
                        .map(DateTimeUtil::dateToString)
                        .collect(Collectors.toList());
                bookedDate.addAll(date);
            }
            // 시작일 & 종료일 동일한 경우
            else {
                if (!isContinuity && bookings.size() > index)
                    isContinuity = isMidnight(bookings.get(index).getStartDate());
            }
            // 모두 수행
            standard = b.getEndDate();
        }
        return bookedDate;
    }

    private boolean isMidnight(LocalDateTime localDateTime) {
        return DateTimeUtil.dateTimeToTime(localDateTime).equals(LocalTime.MIN);
    }

    @Override
    public void updateBookingStatusForResigning(User user) {
        jpaQueryFactory.update(resourceBooking)
                .set(resourceBooking.status, BookingStatus.CANCELED)
                .where(resourceBooking.user.eq(user)
                        .and(resourceBooking.status.in(BookingStatus.WAITING, BookingStatus.BOOKED, BookingStatus.USING)))
                .execute();

        // 영속성 컨텍스트를 DB 에 즉시 반영
        entityManager.flush();
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
        // TODO 기획 변경으로 인한 수정
//        for (ResourceBooking b : bookings) {
//            List<LocalDate> date = b.getStartDate().datesUntil(b.getEndDate().plusDays(1)).collect(Collectors.toList());
//            bookedDate.addAll(date);
//        }

        // 2. startDate ~ endDate
        List<LocalDate> date = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());

        // 2번이 1번에 있는지 여부
        for (LocalDate localDate : date) {
            if (bookedDate.contains(localDate)) return true;
        }

        return false;
    }
}
