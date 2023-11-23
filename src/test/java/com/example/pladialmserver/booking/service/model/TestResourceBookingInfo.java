package com.example.pladialmserver.booking.service.model;

import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.product.resource.entity.Resource;
import com.example.pladialmserver.user.entity.User;

import java.time.LocalDateTime;

import static com.example.pladialmserver.resource.service.model.TestResourceInfo.setUpResource;

public class TestResourceBookingInfo {
    public static ResourceBooking setUpResourceBooking(User basicUser, User adminUser){
        Resource resource = setUpResource(adminUser);
        return new ResourceBooking(
                1L,
                basicUser,
                resource,
                LocalDateTime.of(2024,1,1,0,0),
                LocalDateTime.of(2024,1,2,0,0),
                "예약합니다.");
    }
}
