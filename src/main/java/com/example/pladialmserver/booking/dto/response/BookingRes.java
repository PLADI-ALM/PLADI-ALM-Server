package com.example.pladialmserver.booking.dto.response;

import com.example.pladialmserver.booking.entity.OfficeBooking;
import com.example.pladialmserver.global.utils.DateTimeUtil;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookingRes {

    private Long id;
    private String name;
    private String detailInfo;
    private String startDateTime;
    private String endDateTime;
    private String status;

    public static BookingRes toDto(OfficeBooking officeBooking) {
        return BookingRes.builder()
                .id(officeBooking.getOfficeBookingId())
                .name(officeBooking.getOffice().getName())
                .detailInfo(officeBooking.getOffice().getLocation())
                .startDateTime(DateTimeUtil.dateAndTimeToString(officeBooking.getDate(), officeBooking.getStartTime()))
                .endDateTime(DateTimeUtil.dateAndTimeToString(officeBooking.getDate(), officeBooking.getEndTime()))
                .status(officeBooking.getStatus().getValue())
                .build();
    }

}
