package com.example.pladialmserver.booking.controller;

import com.example.pladialmserver.booking.dto.response.AdminBookingRes;
import com.example.pladialmserver.booking.dto.response.OfficeBookingDetailRes;
import com.example.pladialmserver.booking.dto.response.ResourceBookingDetailRes;
import com.example.pladialmserver.booking.service.BookingService;
import com.example.pladialmserver.global.resolver.Account;
import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.product.resource.dto.response.AdminResourceRes;
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

@Api(tags = "관리자 예약 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/bookings")
public class BookingAdminController {
    private final BookingService bookingService;


    /**
     * 관리자 회의실 예약 목록 조회
     */
    @Operation(summary = "관리자 회의실 예약 목록 조회 (이승학)", description = "관리자 페이지에서 회의실 예약 내역을 전체 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
    })
    @GetMapping("/offices")
    public ResponseCustom<Page<AdminBookingRes>> getBookingOffices(
            @Account User user,
            @PageableDefault(size = 8) Pageable pageable){
        return ResponseCustom.OK(bookingService.getBookingOffices(user,pageable));
    }


    /**
     * 관리자 회의실 예약 반려
     */
    @Operation(summary = "관리자 회의실 예약 반려 (이승학)", description = "관리자 회의실 예약을 반려한다.")
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
        bookingService.cancelBookingOfficeByAdmin(user, officeBookingId);
        return ResponseCustom.OK();
    }



    /**
     * 관리자 회의실 상세 내역 조회
     */
    @Operation(summary = "관리자 회의실 예약 상세 조회 (박소정)", description = "관리자 회의실 예약 내역을 상세 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)회의실 예약 개별 조회 성공"),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(B0006)존재하지 않는 예약입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("/offices/{officeBookingId}")
    public ResponseCustom<OfficeBookingDetailRes> getOfficeBookingDetail(
            @Account User user,
            @Parameter(description = "(Long) 회의실 예약 Id", example = "1") @PathVariable(name="officeBookingId") Long officeBookingId){
        return ResponseCustom.OK(bookingService.getOfficeBookingDetailByAdmin(user, officeBookingId));
    }

    /**
     * 관리자 장비 예약 개별 조회
     */
    @Operation(summary = "관리자 장비 예약 개별 조회 (박소정)", description = "장비 예약 내역을 개별 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(B0006)존재하지 않는 예약입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("/resources/{resourceBookingId}")
    public ResponseCustom<ResourceBookingDetailRes> getResourceBookingDetail(
            @Account User user,
            @Parameter(description = "(Long) 장비 예약 Id", example = "1") @PathVariable(name="resourceBookingId") Long resourceBookingId){
        return ResponseCustom.OK(bookingService.getResourceBookingDetailByAdmin(user, resourceBookingId));
    }




    /**
     * 관리자 장비 예약 반려
     */
    @Operation(summary = "관리자 장비 예약 반려 (박소정)", description = "관리자 페이지에서 장비 예약을 반려한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(B0006)존재하지 않는 예약입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "409", description = "(B0011)불가능한 예약 상태입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @PatchMapping("/resources/{resourceBookingId}/reject")
    public ResponseCustom rejectResourceBooking(
            @Account User user,
            @Parameter(description = "(Long) 장비 예약 Id", example = "1") @PathVariable(name = "resourceBookingId") Long resourceBookingId
    ){
        bookingService.rejectResourceBooking(user, resourceBookingId);
        return ResponseCustom.OK();
    }

    /**
     * 관리자 장비 예약 허가
     */
    @Operation(summary = "관리자 장비 예약 허가 (박소정)", description = "관리자 페이지에서 장비 예약을 허가한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(B0005)이미 예약되어 있는 시간입니다. (B0006)존재하지 않는 예약입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "409", description = "(B0011)불가능한 예약 상태입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @PatchMapping("/resources/{resourceBookingId}/allow")
    public ResponseCustom allowResourceBooking(
            @Account User user,
            @Parameter(description = "(Long) 장비 예약 Id", example = "1") @PathVariable(name = "resourceBookingId") Long resourceBookingId
    ){
        bookingService.allowResourceBooking(user, resourceBookingId);
        return ResponseCustom.OK();
    }

    /**
     * 관리자 장비 예약 반납
     */
    @Operation(summary = "관리자 장비 예약 반납 (박소정)", description = "장비를 반납한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(B0006)존재하지 않는 예약입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "409", description = "(B0009)사용중인 상태에서만 반납이 가능합니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @PatchMapping("/resources/{resourceBookingId}/return")
    public ResponseCustom returnBookingResource(
            @Account User user,
            @Parameter(description = "(Long) 장비 예약 Id", example = "1") @PathVariable(name = "resourceBookingId") Long resourceBookingId
    ){
        bookingService.returnBookingResourceByAdmin(user, resourceBookingId);
        return ResponseCustom.OK();
    }

    /**
     * 관리자 장비 예약 목록을 조회
     */
    @Operation(summary = "관리자 장비 예약 목록 조회 (이승학)", description = "관리자 페이지에서 장비 예약 내역을 전체 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
    })
    @GetMapping("/resources")
    public ResponseCustom<Page<AdminResourceRes>> getBookingResources(
            @Account User user,
            @Parameter(description = "(Boolean) 오름차순/내림차순", example = "true / false") boolean active,
            @PageableDefault(size = 8) Pageable pageable){
        return ResponseCustom.OK(bookingService.getBookingResources(user,pageable,active));
    }


    /**
     * 관리자 차량 예약 개별 조회
     */
    @Operation(summary = "관리자 차량 예약 개별 조회 (박소정)", description = "차량 예약 내역을 개별 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(B0006)존재하지 않는 예약입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("/cars/{carBookingId}")
    public ResponseCustom<ResourceBookingDetailRes> getCarBookingDetail(
            @Account User user,
            @Parameter(description = "(Long) 차량 예약 Id", example = "1") @PathVariable(name="carBookingId") Long carBookingId){
        return ResponseCustom.OK(bookingService.getCarBookingDetailByAdmin(user, carBookingId));
    }

    /**
     * 관리자 차량 예약 반려
     */
    @Operation(summary = "관리자 차량 예약 반려 (박소정)", description = "관리자 페이지에서 차량 예약을 반려한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(B0006)존재하지 않는 예약입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "409", description = "(B0011)불가능한 예약 상태입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @PatchMapping("/cars/{carBookingId}/reject")
    public ResponseCustom rejectCarBooking(
            @Account User user,
            @Parameter(description = "(Long) 차량 예약 Id", example = "1") @PathVariable(name = "carBookingId") Long carBookingId
    ){
        bookingService.rejectCarBooking(user, carBookingId);
        return ResponseCustom.OK();
    }

    /**
     * 관리자 차량 예약 허가
     */
    @Operation(summary = "관리자 차량 예약 허가 (박소정)", description = "관리자 페이지에서 차량 예약을 허가한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(B0005)이미 예약되어 있는 시간입니다. (B0006)존재하지 않는 예약입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "409", description = "(B0011)불가능한 예약 상태입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @PatchMapping("/cars/{carBookingId}/allow")
    public ResponseCustom allowCarBooking(
            @Account User user,
            @Parameter(description = "(Long) 차량 예약 Id", example = "1") @PathVariable(name = "carBookingId") Long carBookingId
    ){
        bookingService.allowCarBooking(user, carBookingId);
        return ResponseCustom.OK();
    }

    /**
     * 관리자 차량 예약 반납
     */
    @Operation(summary = "관리자 차량 예약 반납 (박소정)", description = "차량을 반납한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(B0006)존재하지 않는 예약입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "409", description = "(B0009)사용중인 상태에서만 반납이 가능합니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @PatchMapping("/cars/{carBookingId}/return")
    public ResponseCustom returnBookingCar(
            @Account User user,
            @Parameter(description = "(Long) 차량 예약 Id", example = "1") @PathVariable(name = "carBookingId") Long carBookingId
    ){
        bookingService.returnBookingCarByAdmin(user, carBookingId);
        return ResponseCustom.OK();
    }

    /**
     * 관리자 차량 예약 목록을 조회
     */
    @Operation(summary = "관리자 차량 예약 목록 조회 (이승학)", description = "관리자 페이지에서 차량 예약 내역을 전체 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
    })
    @GetMapping("/cars")
    public ResponseCustom<Page<AdminResourceRes>> getBookingCars(
            @Account User user,
            @Parameter(description = "(Boolean) 오름차순/내림차순", example = "true / false") boolean active,
            @PageableDefault(size = 8) Pageable pageable){
        return ResponseCustom.OK(bookingService.getBookingCars(user,pageable,active));
    }
}
