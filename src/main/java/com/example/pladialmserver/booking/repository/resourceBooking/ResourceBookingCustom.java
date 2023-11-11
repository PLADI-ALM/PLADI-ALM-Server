package com.example.pladialmserver.booking.repository.resourceBooking;

import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.product.dto.response.ProductBookingRes;
import com.example.pladialmserver.product.resource.entity.Resource;
import com.example.pladialmserver.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ResourceBookingCustom {
    Page<BookingRes> getBookingsByUser(User user, Pageable pageable);
    boolean existsDateTime(Resource resource, LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<String> getResourceBookedDate(Resource resource, LocalDate standardDate, LocalDate date);
    void updateBookingStatusForResigning(User user);

    ProductBookingRes findResourceBookingByDate(Resource resource, LocalDateTime dateTime);

    List<Long> findBookedResourceIdsByDate(LocalDateTime startDate, LocalDateTime endDate);
}
