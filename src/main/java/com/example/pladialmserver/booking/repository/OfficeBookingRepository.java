package com.example.pladialmserver.booking.repository;

import com.example.pladialmserver.office.entity.Office;
import com.example.pladialmserver.booking.entity.OfficeBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface OfficeBookingRepository extends JpaRepository<OfficeBooking, Long> {
    @Query("SELECT ob FROM OfficeBooking ob WHERE ob.date = :date AND ((ob.startTime <= :startTime AND ob.endTime > :startTime) OR (ob.startTime < :endTime AND ob.endTime >= :endTime))")
    List<OfficeBooking> findByDateAndTime(@Param("date") LocalDate date, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

    // todo: querydsl 로 코드 수정 예정
    @Query("SELECT count(ob.officeBookingId) > 0 FROM OfficeBooking ob WHERE ob.date = :date AND ((ob.startTime <= :startTime AND ob.endTime > :startTime) OR (ob.startTime < :endTime AND ob.endTime >= :endTime))")
    Boolean existsByDateAndTime(@Param("date") LocalDate date, @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

    List<OfficeBooking> findByOfficeAndDate(Office office, LocalDate date);
}
