package com.example.pladialmserver.booking.service.model;

import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.product.resource.entity.Resource;
import com.example.pladialmserver.user.entity.User;

import java.time.LocalDateTime;

import static com.example.pladialmserver.resource.service.model.TestResourceInfo.setUpResource;

public class TestResourceBookingInfo {
    public static ResourceBooking setUpResourceBooking(Long resourceBookingId, User basicUser, User adminUser, BookingStatus status) {
        Resource resource = setUpResource(adminUser);
        return ResourceBooking.builder()
                .resourceBookingId(resourceBookingId)
                .user(basicUser)
                .resource(resource)
                .startDate(LocalDateTime.of(2024, 1, 1, 0, 0))
                .endDate(LocalDateTime.of(2024, 1, 2, 0, 0))
                .memo("예약합니다.")
                .status(status)
                .build();
    }
}
