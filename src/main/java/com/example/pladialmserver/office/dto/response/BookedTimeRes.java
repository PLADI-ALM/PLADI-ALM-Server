package com.example.pladialmserver.office.dto.response;

import com.example.pladialmserver.global.utils.DateTimeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class BookedTimeRes {
    @Schema(type = "LocalTime(String)", description = "예약시작시간", example = "11:00")
    private String startTime;
    @Schema(type = "LocalTime(String)", description = "예약종료시간", example = "12:00")
    private String endTime;

    public static BookedTimeRes toDto(LocalTime startTime, LocalTime endTime){
        return BookedTimeRes.builder()
                .startTime(DateTimeUtil.timeToString(startTime))
                .endTime(DateTimeUtil.timeToString(endTime))
                .build();
    }
}
