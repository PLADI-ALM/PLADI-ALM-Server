package com.example.pladialmserver.booking.dto.response;

import com.example.pladialmserver.booking.entity.CarBooking;
import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.global.utils.DateTimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductBookingDetailRes {
    @Schema(type = "Long", description = "Id", example = "1")
    private Long resourceId;
    @Schema(type = "String", description = "상태", example = "'예약대기' / '예약중' / '사용중' / '사용완료' / '예약취소'")
    private String status;
    @Schema(type = "String", description = "예약자 이름", example = "박소정")
    private String reservatorName;
    @Schema(type = "String", description = "예약자 연락처", example = "010-1111-1004")
    private String reservatorPhone;
    @Schema(type = "String", description = "예약일자(시작일)", example = "2023-10-01 10:00")
    private String startDate;
    @Schema(type = "String", description = "예약일자(종료일)", example = "2023-10-02 11:00")
    private String endDate;
    @Schema(type = "String", description = "반납일", example = "2023-10-02 14:00 / null")
    private String returnDateTime;
    @Schema(type = "String", description = "이용목적", example = "10월 2일 촬영에 사용할 예정입니다.")
    private String memo;

    public static ProductBookingDetailRes toDto(ResourceBooking resourceBooking) {
        return ProductBookingDetailRes.builder()
                .resourceId(resourceBooking.getResource().getResourceId())
                .status(resourceBooking.getStatus().getValue())
                .reservatorName(resourceBooking.getUser().getName())
                .reservatorPhone(resourceBooking.getUser().getPhone())
                .startDate(DateTimeUtil.dateTimeToString(resourceBooking.getStartDate()))
                .endDate(DateTimeUtil.dateTimeToString(resourceBooking.getEndDate()))
                .returnDateTime(DateTimeUtil.dateTimeToStringNullable(resourceBooking.getReturnDate()))
                .memo(resourceBooking.getMemo())
                .build();
    }

    public static ProductBookingDetailRes toDto(CarBooking carBooking) {
        return ProductBookingDetailRes.builder()
                .resourceId(carBooking.getCar().getCarId())
                .status(carBooking.getStatus().getValue())
                .reservatorName(carBooking.getUser().getName())
                .reservatorPhone(carBooking.getUser().getPhone())
                .startDate(DateTimeUtil.dateTimeToString(carBooking.getStartDate()))
                .endDate(DateTimeUtil.dateTimeToString(carBooking.getEndDate()))
                .returnDateTime(DateTimeUtil.dateTimeToStringNullable(carBooking.getReturnDate()))
                .memo(carBooking.getMemo())
                .build();
    }
}
