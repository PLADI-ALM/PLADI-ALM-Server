package com.example.pladialmserver.admin;

import com.example.pladialmserver.booking.dto.response.BookingRes;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "관리자 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/Admin")
public class AdminController {
    private final AdminService adminService;

    /**
     * 관리자 회의실 예약 목록 조회
     */
    @Operation(summary = "관리자 회의실 예약 목록 조회", description = "관리자 페이지에서 회의실 예약 내역을 전체 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
    })
    @GetMapping
    public ResponseCustom<Page<AdminBookingRes>> getBookingOffices(
            @PageableDefault(size = 8) Pageable pageable){
        return ResponseCustom.OK(adminService.getBookingOffices(pageable));
    }


    /**
     * 관리자 회의실 예약 반
     */


    /**
     * 관리자 회의실 상세 내역을 조회한다.
     */


    /**
     * 관리자 자원 예약 목록을 조회
     */
}
