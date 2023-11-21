package com.example.pladialmserver.product.dto.response;

import com.example.pladialmserver.booking.entity.CarBooking;
import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.global.utils.DateTimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductList {
    @Schema(type = "String", description = "예약자 이름", example = "이승학")
    private String reservatorName;
    @Schema(type = "String", description = "예약자 전화번호", example = "010-0000-0000")
    private String reservatorPhone;
    @Schema(type = "String", description = "예약일자(시작일)", example = "'2023-10-01 12:00' / '2023-10-01'")
    private String startDateTime;
    @Schema(type = "String", description = "예약일자(종료일)", example = "'2023-10-01 13:00' / '2023-10-03'")
    private String endDateTime;
    @Schema(type = "String", description = "목적", example = "승학이 보물")
    private String memo;
    @Schema(type = "String", description = "예약상태", example = "예약중")
    private String bookingStatus;

    public static ProductList toDto(ResourceBooking resourceBooking) {
        return ProductList.builder()
                .reservatorName(resourceBooking.getUser().getName())
                .reservatorPhone(resourceBooking.getUser().getPhone())
                .startDateTime(DateTimeUtil.dateTimeToString(resourceBooking.getStartDate()))
                .endDateTime(DateTimeUtil.dateTimeToString(resourceBooking.getEndDate()))
                .memo(resourceBooking.getMemo())
                .bookingStatus(resourceBooking.getStatus().getValue())
                .build();
    }

    public static ProductList toDto(CarBooking carBooking) {
        return ProductList.builder()
                .reservatorName(carBooking.getUser().getName())
                .reservatorPhone(carBooking.getUser().getPhone())
                .startDateTime(DateTimeUtil.dateTimeToString(carBooking.getStartDate()))
                .endDateTime(DateTimeUtil.dateTimeToString(carBooking.getEndDate()))
                .memo(carBooking.getMemo())
                .bookingStatus(carBooking.getStatus().getValue())
                .build();
    }
}
