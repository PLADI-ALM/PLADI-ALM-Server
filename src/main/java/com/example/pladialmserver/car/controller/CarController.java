package com.example.pladialmserver.car.controller;

import com.example.pladialmserver.car.dto.CarRes;
import com.example.pladialmserver.car.dto.response.CarDetailRes;
import com.example.pladialmserver.car.service.CarService;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.global.resolver.Account;
import com.example.pladialmserver.global.response.ResponseCustom;
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

import java.time.LocalDateTime;

import static com.example.pladialmserver.global.Constants.DATE_TIME_PATTERN;

@Api(tags = "차량 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cars")
public class CarController {
    private final CarService carService;

    /**
     * 전체 차량 목록 조회 and 예약 가능한 차량 목록 조회
     */
    @Operation(summary = "차량 목록 조회 (이승학)", description = "차량 목록 조회를 진행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)차량 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "(R0001)차량과 날짜를 모두 입력해주세요.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "400", description = "(R0002)종료일은 시작일보다 빠를 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),

    })
    @GetMapping
    public ResponseCustom<Page<CarRes>> getCar(
            @Account User user,
            @Parameter(description = "차량 이름", example = "벤츠") @RequestParam(required = false) String carName,
            @Parameter(description = "시작 예약 날짜", example = "2023-10-29 11:00") @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime startDate,
            @Parameter(description = "종료 예약 날짜", example = "2023-10-29 12:00") @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_PATTERN) LocalDateTime endDate,
            Pageable pageable
    ) {
        if ((carName != null && (startDate == null || endDate == null)) ||
                (carName == null && (startDate != null || endDate != null))) {
            throw new BaseException(BaseResponseCode.NAME_OR_DATE_IS_NULL);
        }
        return ResponseCustom.OK(carService.findAvailableCars(carName, startDate, endDate, pageable));
    }

    /**
     * 차량 개별 조회
     */
    @Operation(summary = "차량 개별 조회 (박소정)", description = "차량 개별 조회를 진행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "(C0001)존재하지 않는 차량입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("/{carId}")
    public ResponseCustom<CarDetailRes> getCarDetail(
            @Account User user,
            @Parameter(description = "(Long) 차량 Id", example = "1") @PathVariable(name = "carId") Long carId
    ) {
        return ResponseCustom.OK(carService.getCarDetail(carId));
    }
}
