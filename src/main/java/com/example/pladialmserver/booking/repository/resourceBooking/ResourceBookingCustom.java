package com.example.pladialmserver.booking.repository.resourceBooking;

import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.resource.dto.response.ResourceBookingRes;
import com.example.pladialmserver.resource.entity.Resource;
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

    List<ResourceBookingRes> findResourceBookingByDate(Resource resource, LocalDate date);

    List<Long> findBookedResourceIdsByDateAndCarName(LocalDateTime startDate, LocalDateTime endDate, String resourceName);
    List<Long> findBookedResourceIdsByDate(LocalDateTime startDate, LocalDateTime endDate);
}
