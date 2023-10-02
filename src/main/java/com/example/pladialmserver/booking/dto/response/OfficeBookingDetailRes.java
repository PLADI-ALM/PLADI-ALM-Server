package com.example.pladialmserver.booking.dto.response;

import com.example.pladialmserver.booking.entity.OfficeBooking;
import com.example.pladialmserver.global.utils.DateTimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import static com.example.pladialmserver.global.Constants.DATE_PATTERN;
import static com.example.pladialmserver.global.Constants.TIME_PATTERN;

@Data
@Builder
public class OfficeBookingDetailRes {
    @Schema(type = "Long", description = "회의실 id", example = "1")
    private Long officeId;
    @Schema(type = "LocalDate(String)", description = "예약일자", example = "2023-09-02", required = true, pattern = DATE_PATTERN)
    private String date;
    @Schema(type = "LocalTime(String)", description = "예약시작시간", example = "11:00", required = true, pattern = TIME_PATTERN)
    private String startTime;
    @Schema(type = "LocalTime(String)", description = "예약종료시간", example = "12:00", required = true, pattern = TIME_PATTERN)
    private String endTime;
    @Schema(type = "String", description = "이용목적", maxLength = 30)
    private String memo;

    public static OfficeBookingDetailRes toDto(OfficeBooking officeBooking) {
        return OfficeBookingDetailRes.builder()
                .officeId(officeBooking.getOffice().getOfficeId())
                .date(DateTimeUtil.dateToString(officeBooking.getDate()))
                .startTime(DateTimeUtil.timeToString(officeBooking.getStartTime()))
                .endTime(DateTimeUtil.timeToString(officeBooking.getEndTime()))
                .memo(officeBooking.getMemo())
                .build();
    }
}
