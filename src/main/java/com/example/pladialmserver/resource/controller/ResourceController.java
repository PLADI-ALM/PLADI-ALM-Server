package com.example.pladialmserver.resource.controller;

import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.resource.dto.response.ResourceRes;
import com.example.pladialmserver.resource.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static com.example.pladialmserver.global.Constants.DATE_PATTERN;

@Api(tags = "자원 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/resources")
public class ResourceController {
    private final ResourceService resourceService;


    /**
     * 전체 자원 목록 조회 and 예약 가능한 자원 목록 조회
     */
    @Operation(summary = "자원 목록 조회", description = "자원 목록 조회를 진행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)자원 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "(R0001)자원과 날짜를 모두 입력해주세요.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "400", description = "(R0002)종료일은 시작일보다 빠를 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),

    })
    @GetMapping
    public ResponseCustom<Page<ResourceRes>> selectResource(
            @Parameter(description = "자원 이름",example = "벤츠") @RequestParam(required = false) String resourceName,
            @Parameter(description = "시작 예약 날짜",example = "2023-10-02") @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDate startDate,
            @Parameter(description = "종료 예약 날짜",example = "2023-10-02") @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDate endDate,
            Pageable pageable
    ) {
        if ((resourceName != null && (startDate == null || endDate == null)) ||
                (resourceName == null && (startDate != null || endDate != null))) {
            throw new BaseException(BaseResponseCode.NAME_OR_DATE_IS_NULL);
        }
        return ResponseCustom.OK(resourceService.findAvailableResources(resourceName, startDate, endDate, pageable));
    }


    /**
     * 자원 개별 조회
     */



    /**
     * 자원 기간별 예약 현황 조회
     */



    /**
     * 자원 예약
     */
}
