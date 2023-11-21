package com.example.pladialmserver.product.resource.controller;

import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.global.resolver.Account;
import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.product.dto.request.ProductReq;
import com.example.pladialmserver.product.dto.response.ProductBookingRes;
import com.example.pladialmserver.product.dto.response.ProductDetailRes;
import com.example.pladialmserver.product.resource.dto.response.ResourceRes;
import com.example.pladialmserver.product.resource.service.ResourceService;
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

import static com.example.pladialmserver.global.Constants.*;

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
            @ApiResponse(responseCode = "400", description = "(R0011)시작,종료 날짜와 시간을 모두 입력해주세요.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
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
        if ((startDate == null && endDate != null) || (startDate != null && endDate == null)) {
            throw new BaseException(BaseResponseCode.START_DATE_OR_END_DATE_IS_NULL);
        }
        // 현재보다 과거 날짜 및 시간으로 등록하는 경우의 예외 처리
        if (startDate != null && LocalDateTime.now().isAfter(startDate)) {
            throw new BaseException(BaseResponseCode.DATE_MUST_BE_THE_FUTURE);
        }
        // 끝나는 시간이 시작 시간보다 빠른 경우의 예외 처리
        if (startDate != null && endDate != null && !endDate.isAfter(startDate)) {
            throw new BaseException(BaseResponseCode.START_TIME_MUST_BE_IN_FRONT);
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
    public ResponseCustom<ProductDetailRes> getResourceDetail(
            @Account User user,
            @Parameter(description = "(Long) 장비 Id", example = "1") @PathVariable(name = "resourceId") Long resourceId
    ) {
        return ResponseCustom.OK(resourceService.getProductDetail(resourceId));
    }


    /**
     * 장비 월별 예약 현황 조회
     */
    @Operation(summary = "장비 월별 예약 현황 조회 (박소정)", description = "해당 월의 장비 예약이 불가능한 날짜를 조회한다. (YYYY-MM-DD)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "(R0003)존재하지 않는 장비입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("/{resourceId}/booking-state")
    public ResponseCustom<List<String>> getResourceBookedDate(
            @Account User user,
            @Parameter(description = "(Long) 장비 Id", example = "1") @PathVariable(name = "resourceId") Long resourceId,
            @Parameter(description = "장비 예약 현황 조회 년도월 (YYYY-MM)", example = "2023-10") @RequestParam String month) {
        return ResponseCustom.OK(resourceService.getProductBookedDate(resourceId, month));
    }

    /**
     * 해당 날짜의 장비 예약된 시간 조회
     */
    @Operation(summary = "해당 날짜의 장비 예약된 시간 조회 (박소정)", description = "해당 날짜의 장비 예약된 시간 반환 (HH:SS)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "(R0003)존재하지 않는 장비입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("/{resourceId}/booking-time")
    public ResponseCustom<List<String>> getResourceBookedTime(
            @Account User user,
            @Parameter(description = "(Long) 장비 Id", example = "1") @PathVariable(name = "resourceId") Long resourceId,
            @Parameter(description = "장비 예약 현황 조회 날짜 (YYYY-MM-DD)", example = "2023-10-23") @RequestParam(required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDate date) {
        return ResponseCustom.OK(resourceService.getProductBookedTime(resourceId, date));
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
            @RequestBody @Valid ProductReq productReq) {

        // 현재 보다 과거 날짜로 등록 하는 경우
        if (LocalDateTime.now().isAfter(productReq.getStartDateTime()))
            throw new BaseException(BaseResponseCode.DATE_MUST_BE_THE_FUTURE);
        // 종료일이 시작일 보다 빠른 경우
        if (productReq.getEndDateTime().isBefore(productReq.getStartDateTime()))
            throw new BaseException(BaseResponseCode.START_TIME_MUST_BE_IN_FRONT);

        resourceService.bookProduct(user, resourceId, productReq);
        return ResponseCustom.OK();
    }

    /**
     * 해당 날짜의 장비 예약 내역 조회
     */
    @Operation(summary = "해당 일시의 장비 예약 내역 조회 (박소정)", description = "해당 일시의 장비 예약 내역을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "(R0003)존재하지 않는 장비입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("/{resourceId}/booking")
    public ResponseCustom<ProductBookingRes> getResourceBookingByDate(
            @Account User user,
            @Parameter(description = "(Long) 장비 Id", example = "1") @PathVariable(name = "resourceId") Long resourceId,
            @Parameter(description = "장비 예약 현황 조회 날짜 (YYYY-MM-DD HH)", example = "2023-10-30 11") @RequestParam @DateTimeFormat(pattern = DATE_HOUR_PATTERN) LocalDateTime dateTime) {
        return ResponseCustom.OK(resourceService.getProductBookingByDate(resourceId, dateTime));
    }
}
