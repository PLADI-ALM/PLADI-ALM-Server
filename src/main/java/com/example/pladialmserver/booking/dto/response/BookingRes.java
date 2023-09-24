package com.example.pladialmserver.booking.dto.response;

import com.example.pladialmserver.booking.entity.OfficeBooking;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.example.pladialmserver.global.Constants.DATE_TIME_PATTERN;

@Getter
@Builder
public class BookingRes {

    private Long id;
    private String name;
    private String location;
    private String startDateTime;
    private String endDateTime;
    private String status;

    public static BookingRes toDto(OfficeBooking officeBooking) {
        return BookingRes.builder()
                .id(officeBooking.getOfficeBookingId())
                .name(officeBooking.getOffice().getName())
                .location(officeBooking.getOffice().getLocation())
                .startDateTime(dateAndTimeConvertString(officeBooking.getDate(), officeBooking.getStartTime()))
                .endDateTime(dateAndTimeConvertString(officeBooking.getDate(), officeBooking.getEndTime()))
                .status(officeBooking.getStatus().getValue())
                .build();
    }

    // localDate + localTime => localDateTime (String)
    private static String dateAndTimeConvertString(LocalDate date, LocalTime time) {
        return LocalDateTime.of(date, time).format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
    }

}
