package com.example.pladialmserver.booking.controller;

import com.example.pladialmserver.booking.dto.response.OfficeBookingDetailRes;
import com.example.pladialmserver.booking.service.BookingService;
import com.example.pladialmserver.global.response.ResponseCustom;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "예약 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    /**
     * 회의실 예약 목록 조회
     */

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

}
