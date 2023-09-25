package com.example.pladialmserver.office.controller;

import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.office.dto.request.OfficeReq;
import com.example.pladialmserver.office.dto.response.BookedTimeRes;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static com.example.pladialmserver.global.Constants.Booking.BOOKED_TIMES;
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
    public ResponseCustom<List<OfficeRes>> searchOffice(
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(pattern = TIME_PATTERN) LocalTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = TIME_PATTERN) LocalTime endTime
    ) {
        // 날짜와 시작 시간 또는 종료 시간 중 하나라도 입력되지 않았다면 에러 반환
        if ((date != null && (startTime == null || endTime == null)) ||
                (date == null && (startTime != null || endTime != null))) {
            throw new BaseException(BaseResponseCode.DATE_OR_TIME_IS_NULL);
        }
        return ResponseCustom.OK(officeService.findAvailableOffices(date, startTime, endTime));
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
     * 회의실 일자별 예약 현황 조회
     */
    @GetMapping("/{officeId}/booking-state")
    public ResponseCustom<Map<String, List<BookedTimeRes>>> getOfficeBookedTimes(
            @PathVariable Long officeId,
            @RequestParam @DateTimeFormat(pattern = DATE_PATTERN) LocalDate date
    ) {
        return ResponseCustom.OK(Map.of(BOOKED_TIMES, officeService.getOfficeBookedTimes(officeId, date)));
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
        if (!officeReq.getStartTime().isBefore(officeReq.getEndTime())) throw new BaseException(BaseResponseCode.START_TIME_MUST_BE_IN_FRONT);
        officeService.bookOffice(officeId, officeReq);
        return ResponseCustom.OK();
    }

}
