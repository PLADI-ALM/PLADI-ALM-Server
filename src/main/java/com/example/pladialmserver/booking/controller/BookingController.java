package com.example.pladialmserver.booking.controller;

import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.booking.dto.response.OfficeBookingDetailRes;
import com.example.pladialmserver.booking.service.BookingService;
import com.example.pladialmserver.global.response.ResponseCustom;
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

    private final BookingService bookingService;

    /**
     * 예약 목록 조회
     */
    @GetMapping
    public ResponseCustom<Page<BookingRes>> getBookings(@RequestParam(required = false) String category,
                                                        @PageableDefault(size = 8) Pageable pageable){
        // TODO 유저 ID 받아오는 로직 추가
        Long userId = 1L;
        return ResponseCustom.OK(bookingService.getBookings(userId, category, pageable));
    }

    /**
     * 회의실 예약 개별 조회
     */
    @GetMapping("/offices/{officeBookingId}")
    public ResponseCustom<OfficeBookingDetailRes> getOfficeBookingDetail(@PathVariable(name="officeBookingId") Long officeBookingId){
        return ResponseCustom.OK(bookingService.getOfficeBookingDetail(officeBookingId));
    }

    /**
     * 회의실 예약 취소
     */
    @Operation(summary = "회의실 예약 취소", description = "회의실 예약을 취소한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)회의실 에약 취소 성공", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(B0006)존재하지 않는 예약입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "409", description = "(B0007)이미 취소된 예약입니다. (B0008)이미 사용이 완료된 예약입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @PatchMapping("/offices/{officeBookingId}")
    public ResponseCustom cancelBookingOffice(
            @Parameter(description = "(Long) 회의실 예약 Id", example = "1") @PathVariable(name = "officeBookingId") Long officeBookingId) {
        bookingService.cancelBookingOffice(officeBookingId);
        return ResponseCustom.OK();
    }


}
