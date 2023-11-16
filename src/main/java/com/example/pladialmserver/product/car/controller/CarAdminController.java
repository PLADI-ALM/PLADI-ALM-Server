package com.example.pladialmserver.product.car.controller;

import com.example.pladialmserver.global.resolver.Account;
import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.product.car.service.CarService;
import com.example.pladialmserver.user.entity.User;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "관리자 차량 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/cars")
public class CarAdminController {
    private final CarService carService;

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
