package com.example.pladialmserver.booking.repository.carBooking;

import com.example.pladialmserver.booking.entity.CarBooking;
import com.example.pladialmserver.global.entity.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CarBookingRepository extends JpaRepository<CarBooking, Long>, CarBookingCustom {
    @Query("SELECT cb.car.carId " +
            "FROM CarBooking cb" +
            " WHERE (cb.startDate < :endDate AND cb.endDate > :startDate) " +
            "AND cb.car.name LIKE %:carName%")
    List<Long> findBookedCarIdsByDateAndCarName(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("carName") String carName);

    Page<CarBooking> findByStatusIn(List<BookingStatus> list, Pageable pageable);

    List<CarBooking> findByStartDateAndStatus(LocalDateTime now, BookingStatus bookingStatus);
}
