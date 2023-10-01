package com.example.pladialmserver.office.dto.response;

import com.example.pladialmserver.global.utils.DateTimeUtil;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.example.pladialmserver.global.Constants.TIME_PATTERN;

@Data
@Builder
public class BookedTimeRes {
    private String startTime;
    private String endTime;

    public static BookedTimeRes toDto(LocalTime startTime, LocalTime endTime){
        return BookedTimeRes.builder()
                .startTime(DateTimeUtil.timeToString(startTime))
                .endTime(DateTimeUtil.timeToString(endTime))
                .build();
    }
}
