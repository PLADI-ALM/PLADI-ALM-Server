package com.example.pladialmserver.booking.repository.carBooking;

import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.product.car.dto.CarRes;
import com.example.pladialmserver.product.car.entity.Car;
import com.example.pladialmserver.product.dto.response.ProductBookingRes;
import com.example.pladialmserver.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CarBookingCustom {
    boolean existsDateTime(Car car, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<String> getCarBookedDate(Car car, LocalDate standardDate);

    ProductBookingRes findCarBookingByDate(Car car, LocalDateTime dateTime);

    Page<BookingRes> getBookingsByUser(User user, Pageable pageable);

    List<Long> findBookedCarIdsByDate(LocalDateTime startDate, LocalDateTime endDate);

    List<String> getBookedTime(Car car, LocalDate date);
}
