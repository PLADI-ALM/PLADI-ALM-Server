package com.example.pladialmserver.booking.repository.officeBooking;

import com.example.pladialmserver.booking.entity.OfficeBooking;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.office.entity.Office;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface OfficeBookingRepository extends JpaRepository<OfficeBooking, Long>, OfficeBookingCustom {
    @Query("SELECT ob.office.officeId " +
            "FROM OfficeBooking ob " +
            "WHERE ob.date = :date " +
            "AND ((ob.startTime <= :startTime AND ob.endTime > :startTime) " +
            "OR (ob.startTime < :endTime AND ob.endTime >= :endTime))"+
            "AND (ob.status = 'USING' OR ob.status = 'BOOKED')")
    List<Long> findBookedOfficeIdsByDateAndTime(@Param("date") LocalDate date, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);
    Page<OfficeBooking> findByStatusIn(List<BookingStatus> list, Pageable pageable);
    List<OfficeBooking> findByOfficeAndDateAndStatusNot(Office office, LocalDate date, BookingStatus bookingStatus);

    boolean existsByOfficeAndStatusIn(Office office, List<BookingStatus> bookingStatus);

    List<OfficeBooking> findAllByOfficeOrderByStartTimeDesc(Office office);
}
