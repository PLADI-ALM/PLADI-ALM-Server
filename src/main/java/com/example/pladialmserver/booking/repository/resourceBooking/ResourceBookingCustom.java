package com.example.pladialmserver.booking.repository.resourceBooking;

import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.resource.entity.Resource;
import com.example.pladialmserver.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ResourceBookingCustom {
    Page<BookingRes> getBookingsByUser(User user, Pageable pageable);
    boolean existsDate(Resource resource, LocalDate startDate, LocalDate endDate);
    List<String> getResourceBookedDate(Resource resource, LocalDate standardDate);
    void updateBookingStatusForResigning(User user);
}
