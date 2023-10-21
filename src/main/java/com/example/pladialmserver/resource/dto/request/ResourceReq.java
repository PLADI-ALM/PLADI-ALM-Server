package com.example.pladialmserver.resource.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.example.pladialmserver.global.Constants.DATE_PATTERN;

@Getter
@NoArgsConstructor
public class ResourceReq {
    @Schema(type = "LocalDate(String)", description = "예약 시작일", example = "2023-10-10", required = true, pattern = DATE_PATTERN)
    @NotNull(message = "B0010")
    @DateTimeFormat(pattern = DATE_PATTERN)
    private LocalDateTime startDate;

    @Schema(type = "LocalDate(String)", description = "예약 종료일", example = "2023-10-12", required = true, pattern = DATE_PATTERN)
    @NotNull(message = "B0010")
    @DateTimeFormat(pattern =DATE_PATTERN)
    private LocalDateTime endDate;

    @Schema(type = "String", description = "이용목적", maxLength = 30)
    @Size(max = 30, message = "B0002")
    private String memo;
}
