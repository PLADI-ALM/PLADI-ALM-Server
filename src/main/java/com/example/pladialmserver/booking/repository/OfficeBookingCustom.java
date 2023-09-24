package com.example.pladialmserver.booking.repository;

import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OfficeBookingCustom {
    Page<BookingRes> getBookingsByUser(User user, Pageable pageable);
}