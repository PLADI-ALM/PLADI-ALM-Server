package com.example.pladialmserver.product.car.controller;

import com.example.pladialmserver.global.resolver.Account;
import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.product.car.service.CarService;
import com.example.pladialmserver.product.resource.dto.response.AdminResourcesRes;
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

@Api(tags = "관리자 차량 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/cars")
public class CarAdminController {
    private final CarService carService;

    /**
     * 관리자 차량 목록 조회
     */
    @Operation(summary = "관리자 차량 목록 조회 (박소정)", description = "관리자가 차량 목록을 조회 및 검색한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @GetMapping("")
    public ResponseCustom<Page<AdminResourcesRes>> getCars(
            @Account User user,
            @Parameter(description = "(String) 차량명 검색어", example = "2345") @RequestParam(required = false) String keyword,
            @PageableDefault(size = 8) Pageable pageable) {
        return ResponseCustom.OK(carService.getResourcesByAdmin(user, keyword, pageable));
    }

    /**
     * 관리자 차량 활성화/비활성화
     */
    @Operation(summary = "관리자 차량 활성화/비활성화 (이승학)", description = "관리자가 차량 활성화/비활성화 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(R0003)존재하지 않는 장비입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @PatchMapping("/{carId}/activation")
    public ResponseCustom activateResource(
            @Account User user,
            @Parameter(description = "(Long) 차량 Id", example = "1") @PathVariable(name="carId") Long carId) {
        carService.activateCarByAdmin(user, carId);
        return ResponseCustom.OK();
    }
}
