package com.example.pladialmserver.office.controller;

import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.office.dto.request.OfficeReq;
import com.example.pladialmserver.office.dto.response.BookingStateRes;
import com.example.pladialmserver.office.dto.response.OfficeRes;
import com.example.pladialmserver.office.service.OfficeService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.data.domain.Pageable;

import static com.example.pladialmserver.global.Constants.DATE_PATTERN;
import static com.example.pladialmserver.global.Constants.TIME_PATTERN;

@Api(tags = "회의실 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/offices")
public class OfficeController {

    private final OfficeService officeService;

    /**
     * 전체 회의실 목록 조회 and 예약 가능한 회의실 목록 조회
     */
    @Operation(summary = "회의실 목록 조회", description = "회의실 목록 조회를 진행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)회의실 목록 조회 성공", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "400", description = "(B0001)날짜와 시간을 모두 입력해주세요.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @GetMapping
    public ResponseCustom<Page<OfficeRes>> searchOffice(
            @Parameter(description = "예약 날짜",example = "2023-09-20") @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDate date,
            @Parameter(description = "시작 예약 시간",example = "14:00") @RequestParam(required = false) @DateTimeFormat(pattern = TIME_PATTERN) LocalTime startTime,
            @Parameter(description = "종료 예약 시간",example = "15:00") @RequestParam(required = false) @DateTimeFormat(pattern = TIME_PATTERN) LocalTime endTime,
            Pageable pageable
    ) {
        // 날짜와 시작 시간 또는 종료 시간 중 하나라도 입력되지 않았다면 에러 반환
        if ((date != null && (startTime == null || endTime == null)) ||
                (date == null && (startTime != null || endTime != null))) {
            throw new BaseException(BaseResponseCode.DATE_OR_TIME_IS_NULL);
        }
        return ResponseCustom.OK(officeService.findAvailableOffices(date, startTime, endTime,pageable));
    }

    /**
     * 회의실 개별 조회
     */
    @Operation(summary = "회의실 개별 조회", description = "회의실 개별 조회를 진행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)회의실 목록 조회 성공", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(O0001)존재하지 않는 회의실입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @GetMapping("/{officeId}")
    public ResponseCustom<OfficeRes> getOffice(@PathVariable(name="officeId") Long officeId){
        return ResponseCustom.OK(officeService.getOffice(officeId));
    }


    /**
     * 일자별 회의실 예약 현황 조회
     */
    @Operation(summary = "일자별 회의실 예약 현황 조회", description = "일자별 회의실 예약 현황 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)일자별 회의실 예약 현황 조회 성공"),
            @ApiResponse(responseCode = "404", description = "(O0001)존재하지 않는 회의실입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("/{officeId}/booking-state")
    public ResponseCustom<BookingStateRes> getOfficeBookedTimes(
            @Parameter(description = "(Long) 회의실 Id", example = "1") @PathVariable Long officeId,
            @Parameter(description = "(String) 일자", example = "2023-10-02") @RequestParam @DateTimeFormat(pattern = DATE_PATTERN) LocalDate date
    ) {
        return ResponseCustom.OK(officeService.getOfficeBookedTimes(officeId, date));
    }

    /**
     * 회의실 예약
     */
    @Operation(summary = "회의실 예약", description = "회의실을 예약한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)회의실 예약 성공", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "400", description = "(B0001)날짜와 시간을 모두 입력해주세요. (B0002) 요청사항은 30자 이하로 작성해주세요. (B0003)시작시간보다 끝나는 시간이 더 앞에 있습니다. (B0004)미래의 날짜를 선택해주세요.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(O0001)존재하지 않는 회의실입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "409", description = "(B0005)이미 예약되어 있는 시간입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @PostMapping("/{officeId}/booking")
    public ResponseCustom bookOffice(
            @Parameter(description = "(Long) 회의실 Id", example = "1") @PathVariable(name = "officeId") Long officeId,
            @RequestBody @Valid OfficeReq officeReq){
        // 현재보다 과거 날짜로 등록하는 경우
        if(LocalDate.now().isAfter(officeReq.getDate())) throw new BaseException(BaseResponseCode.DATE_MUST_BE_THE_FUTURE);
        // 끝나는 시간이 시작시간보다 빠른경우
        if (!officeReq.getStartTime().isBefore(officeReq.getEndTime()) && !officeReq.getEndTime().equals(LocalTime.MIDNIGHT)) throw new BaseException(BaseResponseCode.START_TIME_MUST_BE_IN_FRONT);
        officeService.bookOffice(officeId, officeReq);
        return ResponseCustom.OK();
    }

}
