package com.example.pladialmserver.resource.controller;

import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.global.resolver.Account;
import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.resource.dto.request.ResourceReq;
import com.example.pladialmserver.resource.dto.response.ResourceBookingRes;
import com.example.pladialmserver.resource.dto.response.ResourceDetailRes;
import com.example.pladialmserver.resource.dto.response.ResourceRes;
import com.example.pladialmserver.resource.service.ResourceService;
import com.example.pladialmserver.user.entity.User;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.pladialmserver.global.Constants.DATE_PATTERN;
import static com.example.pladialmserver.global.Constants.DATE_TIME_PATTERN;

@Api(tags = "장비 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/resources")
public class ResourceController {
    private final ResourceService resourceService;


    /**
     * 전체 장비 목록 조회 and 예약 가능한 장비 목록 조회
     */
    @Operation(summary = "장비 목록 조회 (이승학)", description = "장비 목록 조회를 진행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)장비 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "(R0001)장비와 날짜를 모두 입력해주세요.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "400", description = "(R0002)종료일은 시작일보다 빠를 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),

    })
    @GetMapping
    public ResponseCustom<Page<ResourceRes>> getResource(
            @Account User user,
            @Parameter(description = "장비 이름",example = "맥북 프로") @RequestParam(required = false) String resourceName,
            @Parameter(description = "시작 예약 날짜",example = "2023-10-22 15:00") @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime startDate,
            @Parameter(description = "종료 예약 날짜",example = "2023-10-23 16:00") @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime endDate,
            Pageable pageable
    ) {
        if ((resourceName != null && (startDate == null || endDate == null)) ||
                (resourceName == null && (startDate != null || endDate != null))) {
            throw new BaseException(BaseResponseCode.NAME_OR_DATE_IS_NULL);
        }
        return ResponseCustom.OK(resourceService.findAvailableResources(resourceName, startDate, endDate, pageable));
    }


    /**
     * 장비 개별 조회
     */
    @Operation(summary = "장비 개별 조회 (박소정)", description = "장비 개별 조회를 진행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "(R0003)존재하지 않는 장비입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("/{resourceId}")
    public ResponseCustom<ResourceDetailRes> getResourceDetail(
            @Account User user,
            @Parameter(description = "(Long) 장비 Id", example = "1") @PathVariable(name = "resourceId") Long resourceId
    ) {
        return ResponseCustom.OK(resourceService.getResourceDetail(resourceId));
    }


    /**
     * 장비 월별 예약 현황 조회
     */
    @Operation(summary = "장비 월별 예약 현황 조회 (박소정)", description = "월별로 장비 예약이 불가능한 날짜를 조회를 진행한다. /n 일 기준 예약이 아예 불가한 날짜 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "(R0003)존재하지 않는 장비입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("/{resourceId}/booking-state")
    public ResponseCustom<List<String>> getResourceBookedDate(
            @Account User user,
            @Parameter(description = "(Long) 장비 Id", example = "1") @PathVariable(name = "resourceId") Long resourceId,
            @Parameter(description = "장비 예약 현황 조회 년도월 (YYYY-MM)",example = "2023-10") @RequestParam String month,
            @Parameter(description = "장비 예약 현황 조회 날짜 (YYYY-MM-DD)",example = "2023-10-23") @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDate date) {
        return ResponseCustom.OK(resourceService.getResourceBookedDate(resourceId, month, date));
    }



    /**
     * 장비 예약
     */
    @Operation(summary = "장비 예약 (박소정)", description = "장비을 예약한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "(B0010)날짜를 모두 입력해주세요. (B0002) 요청사항은 30자 이하로 작성해주세요. (B0003)시작시간보다 끝나는 시간이 더 앞에 있습니다. (B0004)미래의 날짜를 선택해주세요.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(R0003)존재하지 않는 장비입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "409", description = "(B0005)이미 예약되어 있는 시간입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))})
    @PostMapping("/{resourceId}")
    public ResponseCustom bookResource(
            @Account User user,
            @Parameter(description = "(Long) 장비 Id", example = "1") @PathVariable(name = "resourceId") Long resourceId,
            @RequestBody @Valid ResourceReq resourceReq) {

        // 현재 보다 과거 날짜로 등록 하는 경우
        if (LocalDateTime.now().isAfter(resourceReq.getStartDateTime()))
            throw new BaseException(BaseResponseCode.DATE_MUST_BE_THE_FUTURE);
        // 종료일이 시작일 보다 빠른 경우
        if (resourceReq.getEndDateTime().isBefore(resourceReq.getStartDateTime()))
            throw new BaseException(BaseResponseCode.START_TIME_MUST_BE_IN_FRONT);

        resourceService.bookResource(user, resourceId, resourceReq);
        return ResponseCustom.OK();
    }

    /**
     * 해당 날짜의 장비 예약 내역 조회
     */
    @Operation(summary = "해당 날짜의 장비 예약 내역 조회 (박소정)", description = "해당 날짜의 장비 예약 내역을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "(R0003)존재하지 않는 장비입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("/{resourceId}/booking")
    public ResponseCustom<List<ResourceBookingRes>> getResourceBookingByDate(
            @Account User user,
            @Parameter(description = "(Long) 장비 Id", example = "1") @PathVariable(name = "resourceId") Long resourceId,
            @Parameter(description = "장비 예약 현황 조회 날짜 (YYYY-MM-DD)", example = "2023-10-30") @RequestParam @DateTimeFormat(pattern = DATE_PATTERN) LocalDate date) {
        return ResponseCustom.OK(resourceService.getResourceBookingByDate(resourceId, date));
    }
}
