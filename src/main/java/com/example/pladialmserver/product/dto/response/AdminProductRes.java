package com.example.pladialmserver.product.dto.response;

import com.example.pladialmserver.booking.entity.CarBooking;
import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.global.utils.DateTimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminProductRes {
    @Schema(type = "Long", description = "예약 Id", example = "1")
    private Long id;
    @Schema(type = "Long", description = "예약 대상 Id", example = "1")
    private Long targetId;
    @Schema(type = "String", description = "장비 이름", example = "'벤츠'")
    private String name;
    @Schema(type = "String", description = "보관장소", example = "'4층 창고'")
    private String location;
    @Schema(type = "String", description = "예약일자(시작일)", example = "'2023-10-01 12:00'")
    private String startDateTime;
    @Schema(type = "String", description = "예약일자(종료일)", example = "'2023-10-01 13:00'")
    private String endDateTime;
    @Schema(type = "String", description = "예약자 이름", example = "이승학")
    private String reservatorName;
    @Schema(type = "String", description = "예약자 전화번호", example = "010-0000-0000")
    private String reservatorPhone;
    @Schema(type = "String", description = "이용목적", example = "드라마 촬영")
    private String memo;
    @Schema(type = "String", description = "상태", example = "'예약중' / '사용중'")
    private String status;

    public static AdminProductRes toDto(ResourceBooking resourceBooking){
        return AdminProductRes.builder()
                .id(resourceBooking.getResourceBookingId())
                .targetId(resourceBooking.getResource().getResourceId())
                .name(resourceBooking.getResource().getName())
                .location(resourceBooking.getResource().getLocation())
                .startDateTime(DateTimeUtil.dateTimeToString(resourceBooking.getStartDate()))
                .endDateTime(DateTimeUtil.dateTimeToString(resourceBooking.getEndDate()))
                .reservatorName(resourceBooking.getUser().getName())
                .reservatorPhone(resourceBooking.getUser().getPhone())
                .memo(resourceBooking.getMemo())
                .status(resourceBooking.getStatus().getValue())
                .build();
    }

    public static AdminProductRes toDto(CarBooking carBooking){
        return AdminProductRes.builder()
                .id(carBooking.getCarBookingId())
                .targetId(carBooking.getCar().getCarId())
                .name(carBooking.getCar().getName())
                .location(carBooking.getCar().getLocation())
                .startDateTime(DateTimeUtil.dateTimeToString(carBooking.getStartDate()))
                .endDateTime(DateTimeUtil.dateTimeToString(carBooking.getEndDate()))
                .reservatorName(carBooking.getUser().getName())
                .reservatorPhone(carBooking.getUser().getPhone())
                .memo(carBooking.getMemo())
                .status(carBooking.getStatus().getValue())
                .build();
    }



}
