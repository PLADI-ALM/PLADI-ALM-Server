package com.example.pladialmserver.booking.repository.officeBooking;

import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.booking.entity.OfficeBooking;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.office.entity.Office;
import com.example.pladialmserver.user.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.pladialmserver.booking.entity.QOfficeBooking.officeBooking;

@RequiredArgsConstructor
public class OfficeBookingRepositoryImpl implements OfficeBookingCustom{
    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager entityManager;

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
    public Boolean existsByDateAndTime(Office office, LocalDate date, LocalTime startTime, LocalTime endTime) {
        Integer fetchOne = jpaQueryFactory.selectOne()
                .from(officeBooking)
                .where(officeBooking.date.eq(date)
                        .and(checkStartTimeOrEndTime(startTime, endTime))
                        .and(officeBooking.office.eq(office))
                        .and(officeBooking.status.in(BookingStatus.BOOKED, BookingStatus.USING)))
                .fetchFirst();
        return fetchOne != null;
    }

    private BooleanExpression checkStartTimeOrEndTime(LocalTime startTime, LocalTime endTime) {
        return officeBooking.startTime.loe(startTime).and(officeBooking.endTime.gt(startTime)
                .or(officeBooking.startTime.lt(endTime).and(officeBooking.endTime.goe(endTime))));
    }

    @Override
    public List<OfficeBooking> findByStatusAndDateAndEndTime(BookingStatus status) {
        return jpaQueryFactory.selectFrom(officeBooking)
                .where(officeBooking.status.eq(BookingStatus.BOOKED)
                        .and(officeBooking.date.eq(LocalDate.now()))
                        .and(officeBooking.endTime.hour().eq(LocalTime.now().getHour())))
                .fetch();
    }

    @Override
    public List<OfficeBooking> findByStatusAndDateAndStartTime(BookingStatus status) {
        return jpaQueryFactory.selectFrom(officeBooking)
                .where(officeBooking.status.eq(BookingStatus.BOOKED)
                        .and(officeBooking.date.eq(LocalDate.now()))
                        .and(officeBooking.startTime.hour().eq(LocalTime.now().getHour())))
                .fetch();
    }

    @Override
    public void updateBookingStatusForResigning(User user) {
        jpaQueryFactory.update(officeBooking)
                .set(officeBooking.status, BookingStatus.CANCELED)
                .where(officeBooking.user.eq(user)
                        .and(officeBooking.status.in(BookingStatus.BOOKED, BookingStatus.USING)))
                .execute();
        // 영속성 컨텍스트를 DB 에 즉시 반영
        entityManager.flush();
    }

}