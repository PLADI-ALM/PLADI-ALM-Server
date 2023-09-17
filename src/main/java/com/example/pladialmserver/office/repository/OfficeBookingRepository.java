package com.example.pladialmserver.office.repository;

import com.example.pladialmserver.office.entity.OfficeBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficeBookingRepository extends JpaRepository<OfficeBooking, Long> {
}
