package com.example.pladialmserver.booking.repository.resourceBooking;

import com.example.pladialmserver.booking.entity.ResourceBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceBookingRepository extends JpaRepository<ResourceBooking, Long>, ResourceBookingCustom {

}
