package com.example.pladialmserver.booking.repository.resourceBooking;

import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.booking.entity.QResourceBooking;
import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.global.utils.DateTimeUtil;
import com.example.pladialmserver.product.dto.response.ProductBookingRes;
import com.example.pladialmserver.product.resource.entity.Resource;
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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

//    private BooleanExpression dateContaining(LocalDate date) {
//        return ObjectUtil.check ?
//        return hasText(resourceName) ? resource.name.contains(resourceName) : null;
//    }

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
                    // 연속인 경우 & 다음 날 00시 또는 이상인 경우 -> startDate 더해주기
                    if (isContinuity &&
                            b.getEndDate().isAfter(DateTimeUtil.getMidNightDateTime(b.getEndDate().plusDays(1)))
                            || b.getEndDate().isEqual(DateTimeUtil.getMidNightDateTime(b.getEndDate().plusDays(1)))) {
                        bookedDate.add(DateTimeUtil.dateToString(b.getStartDate().toLocalDate()));
                    } else if (!isContinuity && ChronoUnit.DAYS.between(b.getStartDate(), b.getEndDate()) == 1) {
                        break;
                    } else {
                        List<String> dates = b.getStartDate().toLocalDate().datesUntil(b.getEndDate().toLocalDate())
                                .map(DateTimeUtil::dateToString)
                                .collect(Collectors.toList());
                        bookedDate.addAll(dates);
                    }
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
    public ProductBookingRes findResourceBookingByDate(Resource resource, LocalDateTime standardDate) {

        // 해당 날짜가 포함된 예약
        ResourceBooking booking = jpaQueryFactory
                .selectFrom(resourceBooking)
                .where(resourceBooking.resource.eq(resource),
                        (resourceBooking.status.in(BookingStatus.WAITING, BookingStatus.BOOKED, BookingStatus.USING)),
                        (resourceBooking.startDate.before(standardDate).or(resourceBooking.startDate.eq(standardDate))),
                        (resourceBooking.endDate.after(standardDate))
                ).orderBy(resourceBooking.startDate.asc())
                .fetchOne();

        return ProductBookingRes.toDto(booking);
    }


    @Override
    public boolean existsDateTime(Resource resource, LocalDateTime startDateTime, LocalDateTime endDateTime) {

        // 1. 예약중 & 사용중인 날짜들
        List<ResourceBooking> bookings = jpaQueryFactory.selectFrom(resourceBooking)
                .where(resourceBooking.resource.eq(resource)
                        .and(resourceBooking.status.in(BookingStatus.BOOKED, BookingStatus.USING)))
                .orderBy(resourceBooking.startDate.asc())
                .fetch();

        if (bookings.isEmpty()) return false;
        for (ResourceBooking b : bookings) {
            // 시작 날짜 또는 종료 날짜가 사이에 있다면
            if (!startDateTime.isBefore(b.getStartDate()) && startDateTime.isBefore(b.getEndDate())) return true;
            if (!endDateTime.isBefore(b.getStartDate()) && endDateTime.isBefore(b.getEndDate())) return true;
        }

        return false;
    }


    @Override
    public List<Long> findBookedResourceIdsByDate(LocalDateTime startDate, LocalDateTime endDate) {
        //주어진 날짜와 시간 범위 내에 장비가 예약되었는지 확인
        return jpaQueryFactory
                .select(QResourceBooking.resourceBooking.resource.resourceId)
                .from(QResourceBooking.resourceBooking)
                .where(QResourceBooking.resourceBooking.startDate.before(endDate),
                        QResourceBooking.resourceBooking.endDate.after(startDate),
                        QResourceBooking.resourceBooking.status.notIn(BookingStatus.CANCELED, BookingStatus.FINISHED))
                .fetch();
    }

    @Override
    public List<String> getBookedTime(Resource resource, LocalDate standardDate) {

        // 기준 날짜의 첫 시간 (00:00)
        LocalDateTime startDateTime = standardDate.atStartOfDay();
        // 기준 날짜의 마지막 시간 (23:00)
        LocalDateTime endDateTime = standardDate.atTime(23, 0);

        // 기준 날짜에 포함된 예약
        List<ResourceBooking> bookings = jpaQueryFactory.selectFrom(resourceBooking)
                .where(resourceBooking.resource.eq(resource)
                        .and(resourceBooking.status.in(BookingStatus.WAITING, BookingStatus.BOOKED, BookingStatus.USING))
                        .and(resourceBooking.startDate.before(endDateTime)
                                .and(resourceBooking.endDate.after(startDateTime))
                                .or(resourceBooking.startDate.before(startDateTime).and(resourceBooking.endDate.after(endDateTime)))
                        )
                ).orderBy(resourceBooking.startDate.asc())
                .fetch();

        // 0시부터 24시
        List<LocalDateTime> hoursList = IntStream.range(0, 24)
                .mapToObj(startDateTime::plusHours)
                .collect(Collectors.toList());

        List<String> answer = new ArrayList<>();
        for (ResourceBooking b : bookings) {
            for (LocalDateTime dateTime : hoursList) {
                if ((dateTime.isAfter(b.getStartDate()) && dateTime.isBefore(b.getEndDate())) || dateTime.isEqual(b.getStartDate()) || dateTime.isEqual(b.getEndDate()))
                    answer.add(DateTimeUtil.dateTimeToStringTime(dateTime));
            }
        }

        return answer;
    }

    @Override
    public List<ProductBookingRes> findResourceBookingByDate(Resource resource, LocalDate standardDate) {
        // 기준 날짜의 첫 시간 (00:00)
        LocalDateTime startDateTime = standardDate.atStartOfDay();
        // 기준 날짜의 마지막 시간 (23:00)
        LocalDateTime endDateTime = standardDate.atTime(23, 0);

        // 기준 날짜에 포함된 예약
        List<ResourceBooking> bookings = jpaQueryFactory.selectFrom(resourceBooking)
                .where(resourceBooking.resource.eq(resource)
                        .and(resourceBooking.status.in(BookingStatus.WAITING, BookingStatus.BOOKED, BookingStatus.USING))
                        .and(resourceBooking.startDate.before(endDateTime)
                                .and(resourceBooking.endDate.after(startDateTime))
                                .or(resourceBooking.startDate.before(startDateTime).and(resourceBooking.endDate.after(endDateTime)))
                        )
                ).orderBy(resourceBooking.startDate.asc())
                .fetch();

        return bookings.stream().map(ProductBookingRes::toDto).collect(Collectors.toList());
    }
}
