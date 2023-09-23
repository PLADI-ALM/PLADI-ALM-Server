package com.example.pladialmserver.booking.dto.response;

import com.example.pladialmserver.booking.entity.OfficeBooking;
import lombok.Builder;
import lombok.Data;

import java.time.format.DateTimeFormatter;

import static com.example.pladialmserver.global.Constants.DATE_PATTERN;
import static com.example.pladialmserver.global.Constants.TIME_PATTERN;

@Data
@Builder
public class OfficeBookingDetailRes {
    private String date;
    private String startTime;
    private String endTime;
    private String memo;

    public static OfficeBookingDetailRes toDto(OfficeBooking officeBooking) {
        return OfficeBookingDetailRes.builder()
                .date(officeBooking.getDate().format(DateTimeFormatter.ofPattern(DATE_PATTERN)))
                .startTime(officeBooking.getStartTime().format(DateTimeFormatter.ofPattern(TIME_PATTERN)))
                .endTime(officeBooking.getEndTime().format(DateTimeFormatter.ofPattern(TIME_PATTERN)))
                .memo(officeBooking.getMemo())
                .build();
    }
}
