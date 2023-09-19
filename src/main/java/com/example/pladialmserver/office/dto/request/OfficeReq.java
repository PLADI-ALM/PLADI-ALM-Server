package com.example.pladialmserver.office.dto.request;

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
    @NotNull(message = "B0001")
    @DateTimeFormat(pattern = DATE_PATTERN)
    private LocalDate date;
    @NotNull(message = "B0001")
    @DateTimeFormat(pattern = TIME_PATTERN)
    private LocalTime startTime;
    @NotNull(message = "B0001")
    @DateTimeFormat(pattern = TIME_PATTERN)
    private LocalTime endTime;
    @Size(max = 30, message = "B0002")
    private String memo;
}
