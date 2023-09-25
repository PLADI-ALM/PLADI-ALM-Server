package com.example.pladialmserver.office.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.example.pladialmserver.global.Constants.DATE_PATTERN;
import static com.example.pladialmserver.global.Constants.TIME_PATTERN;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OfficeReq {
    @Schema(type = "LocalDate(String)", description = "예약일자", example = "2023-09-02", required = true, pattern = DATE_PATTERN)
    @NotNull(message = "B0001")
    @DateTimeFormat(pattern = DATE_PATTERN)
    private LocalDate date;
    @Schema(type = "LocalTime(String)", description = "예약시작시간", example = "11:00", required = true, pattern = TIME_PATTERN)
    @NotNull(message = "B0001")
    @DateTimeFormat(pattern = TIME_PATTERN)
    private LocalTime startTime;
    @Schema(type = "LocalTime(String)", description = "예약종료시간", example = "12:00", required = true, pattern = TIME_PATTERN)
    @NotNull(message = "B0001")
    @DateTimeFormat(pattern = TIME_PATTERN)
    private LocalTime endTime;
    @Schema(type = "String", description = "이용목적", maxLength = 30)
    @Size(max = 30, message = "B0002")
    private String memo;
}
