package com.example.pladialmserver.product.dto.response;

import com.example.pladialmserver.booking.entity.CarBooking;
import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.global.utils.DateTimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductBookingRes {
    @Schema(type = "String", description = "예약 시작 시간", example = "2023-10-23 13:00")
    private String startDateTime;
    @Schema(type = "String", description = "예약 종료 시간", example = "2023-10-24 15:00")
    private String endDateTime;
    @Schema(type = "String", description = "예약자명", example = "소징")
    private String reservatorName;
    @Schema(type = "String", description = "예약자 부서", example = "미디어")
    private String reservatorDepartment;
    @Schema(type = "String", description = "예약자 연락처", example = "010-1212-1111")
    private String reservatorPhone;

    public static ProductBookingRes toDto(ResourceBooking booking) {
        return ProductBookingRes.builder()
                .startDateTime(DateTimeUtil.dateTimeToString(booking.getStartDate()))
                .endDateTime(DateTimeUtil.dateTimeToString(booking.getEndDate()))
                .reservatorName(booking.getUser().getName())
                .reservatorDepartment(booking.getUser().getDepartment().getName())
                .reservatorPhone(booking.getUser().getPhone())
                .build();
    }

    public static ProductBookingRes toDto(CarBooking booking) {
        return ProductBookingRes.builder()
                .startDateTime(DateTimeUtil.dateTimeToString(booking.getStartDate()))
                .endDateTime(DateTimeUtil.dateTimeToString(booking.getEndDate()))
                .reservatorName(booking.getUser().getName())
                .reservatorDepartment(booking.getUser().getDepartment().getName())
                .reservatorPhone(booking.getUser().getPhone())
                .build();
    }
}
