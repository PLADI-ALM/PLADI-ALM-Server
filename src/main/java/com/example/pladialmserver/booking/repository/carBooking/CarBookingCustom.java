package com.example.pladialmserver.booking.repository.carBooking;

import com.example.pladialmserver.product.car.entity.Car;

import java.time.LocalDateTime;

public interface CarBookingCustom {
    boolean existsDateTime(Car car, LocalDateTime startDateTime, LocalDateTime endDateTime);
//    List<Long> findBookedCarIdsByDateAndCarName(LocalDate startDate, LocalDate endDate, String carName);

}
