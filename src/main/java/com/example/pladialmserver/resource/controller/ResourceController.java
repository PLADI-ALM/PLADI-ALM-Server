package com.example.pladialmserver.resource.controller;

import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.office.dto.request.OfficeReq;
import com.example.pladialmserver.resource.dto.request.ResourceReq;
import com.example.pladialmserver.resource.dto.response.ResourceDetailRes;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;

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
    @Operation(summary = "자원 개별 조회", description = "자원 개별 조회를 진행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "(R0003)존재하지 않는 자원입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("/{resourceId}")
    public ResponseCustom<ResourceDetailRes> getResourceDetail(
            @Parameter(description = "(Long) 자원 Id", example = "1") @PathVariable(name = "resourceId") Long resourceId
    ) {
        return ResponseCustom.OK(resourceService.getResourceDetail(resourceId));
    }


    /**
     * 자원 기간별 예약 현황 조회별
     */



    /**
     * 자원 예약
     */
    @Operation(summary = "자원 예약", description = "자원을 예약한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "400", description = "(B0010)날짜를 모두 입력해주세요. (B0002) 요청사항은 30자 이하로 작성해주세요. (B0003)시작시간보다 끝나는 시간이 더 앞에 있습니다. (B0004)미래의 날짜를 선택해주세요.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(R0003)존재하지 않는 자원입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "409", description = "(B0005)이미 예약되어 있는 시간입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))})
    @PostMapping("/{resourceId}")
    public ResponseCustom bookResource(
            @Parameter(description = "(Long) 자원 Id", example = "1") @PathVariable(name = "resourceId") Long resourceId,
            @RequestBody @Valid ResourceReq resourceReq) {

        // 현재 보다 과거 날짜로 등록 하는 경우
        if (LocalDate.now().isAfter(resourceReq.getStartDate()))
            throw new BaseException(BaseResponseCode.DATE_MUST_BE_THE_FUTURE);
        // 종료일이 시작일 보다 빠른 경우
        if (resourceReq.getEndDate().isBefore(resourceReq.getStartDate()))
            throw new BaseException(BaseResponseCode.START_TIME_MUST_BE_IN_FRONT);
        // TODO 유저 ID 받아오는 로직 추가
        Long userId = 1L;

        resourceService.bookResource(userId, resourceId, resourceReq);
        return ResponseCustom.OK();
    }
}
