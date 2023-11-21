package com.example.pladialmserver.booking.controller;

import com.example.pladialmserver.booking.dto.request.ReturnProductReq;
import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.booking.dto.response.ProductBookingDetailRes;
import com.example.pladialmserver.booking.service.CarBookingService;
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
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@Api(tags = "차량 예약 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings/cars")
public class CarBookingController {
    private final CarBookingService carBookingService;

    /**
     * 차량 예약 목록 조회
     */
    @Operation(summary = "차량 예약 목록 조회 (박소정)", description = "차량 예약 내역을 전체 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "(G0001)잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("")
    public ResponseCustom<Page<BookingRes>> getCarBookings(
            @Account User user,
            @PageableDefault(size = 8) Pageable pageable) {
        return ResponseCustom.OK(carBookingService.getProductBookings(user, pageable));
    }

    /**
     * 차량 예약 개별 조회
     */
    @Operation(summary = "차량 예약 개별 조회 (박소정)", description = "차량 예약 내역을 개별 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(B0006)존재하지 않는 예약입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("/{carBookingId}")
    public ResponseCustom<ProductBookingDetailRes> getCarBookingDetail(
            @Account User user,
            @Parameter(description = "(Long) 차량 예약 Id", example = "1") @PathVariable(name = "carBookingId") Long carBookingId) {
        return ResponseCustom.OK(carBookingService.getProductBookingDetail(user, carBookingId));
    }
    /**
     * 차량 예약 취소
     */
    @Operation(summary = "차량 예약 취소 (장채은)", description = "차량 예약을 취소한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(B0006)존재하지 않는 예약입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "409", description = "(B0007)이미 취소된 예약입니다. (B0008)이미 사용이 완료된 예약입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @PatchMapping("/{carBookingId}/cancel")
    public ResponseCustom cancelBookingCar(
            @Account User user,
            @Parameter(description = "(Long) 차량 예약 Id", example = "1") @PathVariable(name = "carBookingId") Long carBookingId
    ) {
        carBookingService.cancelBookingProduct(user, carBookingId);
        return ResponseCustom.OK();
    }

    /**
     * 차량 예약 반납
     */
    @Operation(summary = "차량 예약 반납 (박소정)", description = "차량 예약을 반납한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(B0006)존재하지 않는 예약입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "409", description = "(B0009)사용중인 상태에서만 반납이 가능합니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @PatchMapping("/{carBookingId}/return")
    public ResponseCustom returnBookingCar(
            @Account User user,
            @Parameter(description = "(Long) 차량 예약 Id", example = "1") @PathVariable(name = "carBookingId") Long carBookingId,
            @RequestBody ReturnProductReq request
    ) {
        carBookingService.returnBookingProduct(user, carBookingId, request);
        return ResponseCustom.OK();
    }
}
