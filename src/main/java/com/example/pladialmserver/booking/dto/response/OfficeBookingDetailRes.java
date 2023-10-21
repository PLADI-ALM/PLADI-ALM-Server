package com.example.pladialmserver.booking.dto.response;

import com.example.pladialmserver.booking.entity.OfficeBooking;
import com.example.pladialmserver.global.utils.DateTimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import static com.example.pladialmserver.global.Constants.DATE_PATTERN;

@Data
@Builder
public class OfficeBookingDetailRes {

    @Schema(type = "Long", description = "회의실 id", example = "1")
    private Long officeId;
    @Schema(type = "String", description = "예약자 이름", example = "박소정")
    private String reservatorName;
    @Schema(type = "String", description = "예약자 연락처", example = "010-1111-1004")
    private String reservatorPhone;
    @Schema(type = "LocalDate(String)", description = "예약일자", example = "2023-09-02", required = true, pattern = DATE_PATTERN)
    private String date;
    @Schema(type = "LocalTime(String)", description = "예약시작시간", example = "11:00")
    private String startTime;
    @Schema(type = "LocalTime(String)", description = "예약종료시간", example = "12:00")
    private String endTime;
    @Schema(type = "String", description = "이용목적")
    private String memo;
    @Schema(type = "String", description = "예약상태", example = "예약중")
    private String bookingStatus;

    public static OfficeBookingDetailRes toDto(OfficeBooking officeBooking) {
        return OfficeBookingDetailRes.builder()
                .officeId(officeBooking.getOffice().getOfficeId())
                .reservatorName(officeBooking.getUser().getName())
                .reservatorPhone(officeBooking.getUser().getPhone())
                .date(DateTimeUtil.dateToString(officeBooking.getDate()))
                .startTime(DateTimeUtil.timeToString(officeBooking.getStartTime()))
                .endTime(DateTimeUtil.timeToString(officeBooking.getEndTime()))
                .memo(officeBooking.getMemo())
                .bookingStatus(officeBooking.getStatus().getValue())
                .build();
    }
}
