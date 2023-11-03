package com.example.pladialmserver.booking.dto.response;

import com.example.pladialmserver.booking.entity.OfficeBooking;
import com.example.pladialmserver.global.utils.DateTimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminBookingRes {

    @Schema(type = "Long", description = "회의실 Id / 자원 Id", example = "1")
    private Long id;
    @Schema(type = "String", description = "회의실명 / 자원명", example = "'회의실1' / '카메라1'")
    private String name;
    @Schema(type = "String", description = "회의실 위치 / 카테고리", example = "'401호' / '촬영장비'")
    private String detailInfo;
    @Schema(type = "String", description = "예약일자(시작일)", example = "'2023-10-01 12:00' / '2023-10-01'")
    private String startDateTime;
    @Schema(type = "String", description = "예약일자(종료일)", example = "'2023-10-01 13:00' / '2023-10-03'")
    private String endDateTime;
    @Schema(type = "String", description = "예약자 이름", example = "이승학")
    private String reservatorName;
    @Schema(type = "String", description = "예약자 전화번호", example = "010-4158-8124")
    private String reservatorPhone;
    @Schema(type = "String", description = "상태", example = "'예약중' / '사용중'")
    private String status;

    public static AdminBookingRes toDto(OfficeBooking officeBooking){
        return AdminBookingRes.builder()
                .id(officeBooking.getOfficeBookingId())
                .name(officeBooking.getOffice().getName())
                .detailInfo(officeBooking.getOffice().getLocation())
                .startDateTime(DateTimeUtil.dateAndTimeToString(officeBooking.getDate(),officeBooking.getStartTime()))
                .endDateTime(DateTimeUtil.dateAndTimeToString(officeBooking.getDate(),officeBooking.getEndTime()))
                .reservatorName(officeBooking.getUser().getName())
                .reservatorPhone(officeBooking.getUser().getPhone())
                .status(officeBooking.getStatus().getValue())
                .build();
    }



}