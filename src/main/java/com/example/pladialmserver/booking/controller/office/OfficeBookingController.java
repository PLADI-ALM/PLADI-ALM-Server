package com.example.pladialmserver.booking.controller.office;

import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.booking.dto.response.OfficeBookingDetailRes;
import com.example.pladialmserver.booking.service.OfficeBookingService;
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

@Api(tags = "회의실 예약 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings/offices")
public class OfficeBookingController {
    private final OfficeBookingService officeBookingService;

    /**
     * 회의실 예약 목록 조회
     */
    @Operation(summary = "회의실 예약 목록 조회 (박소정)", description = "회의실 예약 내역을 전체 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "(G0001)잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("")
    public ResponseCustom<Page<BookingRes>> getOfficeBookings(
            @Account User user,
            @PageableDefault(size = 8) Pageable pageable){
        return ResponseCustom.OK(officeBookingService.getOfficeBookings(user, pageable));
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
    @GetMapping("/{officeBookingId}")
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
    @PatchMapping("/{officeBookingId}/cancel")
    public ResponseCustom cancelBookingOffice(
            @Account User user,
            @Parameter(description = "(Long) 회의실 예약 Id", example = "1") @PathVariable(name = "officeBookingId") Long officeBookingId) {
        officeBookingService.cancelBookingOffice(user, officeBookingId);
        return ResponseCustom.OK();
    }
}
