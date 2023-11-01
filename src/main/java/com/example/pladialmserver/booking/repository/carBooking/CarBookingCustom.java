package com.example.pladialmserver.booking.repository.carBooking;

import com.example.pladialmserver.product.car.entity.Car;
import com.example.pladialmserver.product.dto.response.ProductBookingRes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CarBookingCustom {
    boolean existsDateTime(Car car, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<ProductBookingRes> findResourceBookingByDate(Car car, LocalDate date);
//    List<Long> findBookedCarIdsByDateAndCarName(LocalDate startDate, LocalDate endDate, String carName);

}
