package com.example.pladialmserver.booking.repository.resourceBooking;

import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.global.entity.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ResourceBookingRepository extends JpaRepository<ResourceBooking, Long>, ResourceBookingCustom {

    @Query("SELECT rb.resource.resourceId FROM ResourceBooking rb WHERE (rb.startDate <= :endDate AND rb.endDate >= :startDate) AND rb.resource.name LIKE %:resourceName%")
    List<Long> findBookedResourceIdsByDateAndResourceName(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("resourceName") String resourceName);

}
