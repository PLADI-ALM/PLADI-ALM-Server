package com.example.pladialmserver.booking.repository;

import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;

public interface OfficeBookingCustom {
    Page<BookingRes> getBookingsByUser(User user, Pageable pageable);
    Boolean existsByDateAndTime(LocalDate date, LocalTime startTime, LocalTime endTime);
}