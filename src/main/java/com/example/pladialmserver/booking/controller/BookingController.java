package com.example.pladialmserver.booking.controller;

import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.booking.dto.response.OfficeBookingDetailRes;
import com.example.pladialmserver.booking.dto.response.ResourceBookingDetailRes;
import com.example.pladialmserver.booking.service.CarBookingService;
import com.example.pladialmserver.booking.service.OfficeBookingService;
import com.example.pladialmserver.booking.service.ResourceBookingService;
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

@Api(tags = "예약 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final OfficeBookingService officeBookingService;
    private final ResourceBookingService resourceBookingService;
    private final CarBookingService carBookingService;

    /**
     * 회의실 예약 목록 조회
     */
    @Operation(summary = "회의실 예약 목록 조회 (박소정)", description = "회의실 예약 내역을 전체 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "(G0001)잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("/offices")
    public ResponseCustom<Page<BookingRes>> getOfficeBookings(
            @Account User user,
            @PageableDefault(size = 8) Pageable pageable){
        return ResponseCustom.OK(officeBookingService.getOfficeBookings(user, pageable));
    }

    /**
     * 장비 예약 목록 조회
     */
    @Operation(summary = "장비 예약 목록 조회 (박소정)", description = "장비 예약 내역을 전체 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "(G0001)잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("/resources")
    public ResponseCustom<Page<BookingRes>> getResourceBookings(
            @Account User user,
            @PageableDefault(size = 8) Pageable pageable) {
        return ResponseCustom.OK(resourceBookingService.getProductBookings(user, pageable));
    }

    /**
     * 차량 예약 목록 조회
     */
    @Operation(summary = "차량 예약 목록 조회 (박소정)", description = "차량 예약 내역을 전체 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "(G0001)잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("/cars")
    public ResponseCustom<Page<BookingRes>> getCarBookings(
            @Account User user,
            @PageableDefault(size = 8) Pageable pageable) {
        return ResponseCustom.OK(carBookingService.getProductBookings(user, pageable));
    }

    /**
     * 회의실 예약 개별 조회
     */
    @Operation(summary = "회의실 예약 개별 조회 (박소정)", description = "회의실 예약 내역을 개별 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)회의실 예약 개별 조회 성공"),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(B0006)존재하지 않는 예약입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("/offices/{officeBookingId}")
    public ResponseCustom<OfficeBookingDetailRes> getOfficeBookingDetail(
            @Account User user,
            @Parameter(description = "(Long) 회의실 예약 Id", example = "1")
            @PathVariable(name="officeBookingId") Long officeBookingId){
        return ResponseCustom.OK(officeBookingService.getOfficeBookingDetailByBasic(user, officeBookingId));
    }

    /**
     * 회의실 예약 취소
     */
    @Operation(summary = "회의실 예약 취소 (장채은)", description = "회의실 예약을 취소한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)회의실 에약 취소 성공", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(B0006)존재하지 않는 예약입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "409", description = "(B0007)이미 취소된 예약입니다. (B0008)이미 사용이 완료된 예약입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @PatchMapping("/offices/{officeBookingId}/cancel")
    public ResponseCustom cancelBookingOffice(
            @Account User user,
            @Parameter(description = "(Long) 회의실 예약 Id", example = "1") @PathVariable(name = "officeBookingId") Long officeBookingId) {
        officeBookingService.cancelBookingOffice(user, officeBookingId);
        return ResponseCustom.OK();
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 장비 예약 개별 조회
     */
    @Operation(summary = "장비 예약 개별 조회 (박소정)", description = "장비 예약 내역을 개별 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(B0006)존재하지 않는 예약입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("/resources/{resourceBookingId}")
    public ResponseCustom<ResourceBookingDetailRes> getResourceBookingDetail(
            @Account User user,
            @Parameter(description = "(Long) 장비 예약 Id", example = "1") @PathVariable(name = "resourceBookingId") Long resourceBookingId) {
        return ResponseCustom.OK(resourceBookingService.getProductBookingDetail(user, resourceBookingId));
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
    @GetMapping("/cars/{carBookingId}")
    public ResponseCustom<ResourceBookingDetailRes> getCarBookingDetail(
            @Account User user,
            @Parameter(description = "(Long) 차량 예약 Id", example = "1") @PathVariable(name = "carBookingId") Long carBookingId) {
        return ResponseCustom.OK(carBookingService.getProductBookingDetail(user, carBookingId));
    }

    /**
     * 자원 예약 취소
     */
    @Operation(summary = "자원 예약 취소 (장채은)", description = "자원 예약을 취소한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)자원 에약 취소 성공", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(B0006)존재하지 않는 예약입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "409", description = "(B0007)이미 취소된 예약입니다. (B0008)이미 사용이 완료된 예약입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @PatchMapping("/resources/{resourceBookingId}/cancel")
    public ResponseCustom cancelBookingResource(
            @Account User user,
            @Parameter(description = "(Long) 자원 예약 Id", example = "1") @PathVariable(name = "resourceBookingId") Long resourceBookingId
    ) {
        resourceBookingService.cancelBookingProduct(user, resourceBookingId);
        return ResponseCustom.OK();
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
    @PatchMapping("/cars/{carBookingId}/cancel")
    public ResponseCustom cancelBookingCar(
            @Account User user,
            @Parameter(description = "(Long) 차량 예약 Id", example = "1") @PathVariable(name = "carBookingId") Long carBookingId
    ) {
        carBookingService.cancelBookingProduct(user, carBookingId);
        return ResponseCustom.OK();
    }
}
