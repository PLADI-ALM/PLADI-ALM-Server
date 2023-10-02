package com.example.pladialmserver.booking.dto.response;

import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.global.utils.DateTimeUtil;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResourceBookingDetailRes {
    private String status;
    private String startDate;
    private String endDate;
    private String returnDateTime;
    private String memo;

    public static ResourceBookingDetailRes toDto(ResourceBooking resourceBooking) {
        return ResourceBookingDetailRes.builder()
                .status(resourceBooking.getStatus().getValue())
                .startDate(DateTimeUtil.dateToString(resourceBooking.getStartDate()))
                .endDate(DateTimeUtil.dateToString(resourceBooking.getEndDate()))
                .returnDateTime(resourceBooking.getReturnDate() == null ? null : DateTimeUtil.dateTimeToString(resourceBooking.getReturnDate()))
                .memo(resourceBooking.getMemo())
                .build();
    }
}
