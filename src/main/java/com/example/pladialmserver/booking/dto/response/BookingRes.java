package com.example.pladialmserver.booking.dto.response;

import com.example.pladialmserver.booking.entity.OfficeBooking;
import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.global.utils.DateTimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookingRes {

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
    @Schema(type = "String", description = "상태", example = "'예약대기' / '예약중' / '사용중' / '사용완료' / '예약취소'")
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

    // TODO 기획 변경으로 인한 수정
    public static BookingRes toDto(ResourceBooking resourceBooking) {
        return BookingRes.builder()
                .id(resourceBooking.getResourceBookingId())
                .name(resourceBooking.getResource().getName())
//                .detailInfo(resourceBooking.getResource().getResourceCategory().getName())
//                .startDateTime(DateTimeUtil.dateToString(resourceBooking.getStartDate()))
//                .endDateTime(DateTimeUtil.dateToString(resourceBooking.getEndDate()))
                .status(resourceBooking.getStatus().getValue())
                .build();
    }

}
