package com.example.pladialmserver.booking.dto.response;

import com.example.pladialmserver.booking.entity.OfficeBooking;
import com.example.pladialmserver.global.utils.DateTimeUtil;
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
                .date(DateTimeUtil.dateToString(officeBooking.getDate()))
                .startTime(DateTimeUtil.timeToString(officeBooking.getStartTime()))
                .endTime(DateTimeUtil.timeToString(officeBooking.getEndTime()))
                .memo(officeBooking.getMemo())
                .build();
    }
}
