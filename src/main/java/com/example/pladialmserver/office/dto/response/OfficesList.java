package com.example.pladialmserver.office.dto.response;

import com.example.pladialmserver.booking.entity.OfficeBooking;
import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.global.utils.DateTimeUtil;
import com.example.pladialmserver.resource.dto.response.ResourcesList;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OfficesList {
    @Schema(type="String", description="요청자", example ="이승학")
    private String requester;
    @Schema(type = "String", description = "직위", example = "대리")
    private String position;
    @Schema(type = "String", description = "예약일자(시작일)", example = "'2023-10-01 12:00' / '2023-10-01'")
    private String startDateTime;
    @Schema(type = "String", description = "예약일자(종료일)", example = "'2023-10-01 13:00' / '2023-10-03'")
    private String endDateTime;
    @Schema(type = "String", description = "목적", example = "승학이 보물")
    private String goal;
    @Schema(type = "String", description = "예약상태", example = "예약중")
    private String bookingStatus;

    public static OfficesList toDto(OfficeBooking officeBooking){
        return OfficesList.builder()
                .requester(officeBooking.getUser().getName())
                .position(officeBooking.getUser().getPosition().getName())
                .startDateTime(DateTimeUtil.dateAndTimeToString(officeBooking.getDate(),officeBooking.getStartTime()))
                .endDateTime(DateTimeUtil.dateAndTimeToString(officeBooking.getDate(),officeBooking.getEndTime()))
                .goal(officeBooking.getMemo())
                .bookingStatus(officeBooking.getStatus().getValue())
                .build();
    }
}
