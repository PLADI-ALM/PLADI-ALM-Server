package com.example.pladialmserver.product.car.controller;

import com.example.pladialmserver.global.resolver.Account;
import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.product.car.service.CarService;
import com.example.pladialmserver.product.resource.dto.request.CreateProductReq;
import com.example.pladialmserver.product.resource.dto.response.AdminProductDetailsRes;
import com.example.pladialmserver.product.resource.dto.response.AdminProductsRes;
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

import javax.validation.Valid;

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
    public ResponseCustom<Page<AdminProductsRes>> getCars(
            @Account User user,
            @Parameter(description = "(String) 차량명 검색어", example = "2345") @RequestParam(required = false) String keyword,
            @PageableDefault(size = 8) Pageable pageable) {
        return ResponseCustom.OK(carService.getProductByAdmin(user, keyword, pageable));
    }

    /**
     * 관리자 차량 추가
     */
    @Operation(summary = "관리자 차량 추가 (박소정)", description = "관리자가 차량을 추가한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "(P0007)설명은 255자 이하로 작성해주세요. (P0001)이름은 50자 이하로 작성해주세요. (P0004)보관장소를 입력해주세요. (P0002)이름을 입력해주세요. (P0006)책임자를 입력해주세요. (P0008)설명을 입력해주세요. (P0003)제조사는 30자 이하로 작성해주세요.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @PostMapping("")
    public ResponseCustom createCar(
            @Account User user,
            @RequestBody @Valid CreateProductReq request) {
        carService.createProductByAdmin(user, request);
        return ResponseCustom.OK();
    }

    /**
     * 관리자 차량 수정
     */
    @Operation(summary = "관리자 차량 수정 (박소정)", description = "관리자가 차량을 수정한다. (요청 값 모두 필요)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "(P0007)설명은 255자 이하로 작성해주세요. (P0001)이름은 50자 이하로 작성해주세요. (P0004)보관장소를 입력해주세요. (P0002)이름을 입력해주세요. (P0006)책임자를 입력해주세요. (P0008)설명을 입력해주세요. (P0003)제조사는 30자 이하로 작성해주세요.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(C0001)존재하지 않는 차량입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @PatchMapping("/{carId}")
    public ResponseCustom updateCar(
            @Account User user,
            @Parameter(description = "(Long) 차량 Id", example = "1") @PathVariable(name = "carId") Long carId,
            @RequestBody @Valid CreateProductReq request) {
        carService.updateProductByAdmin(user, carId, request);
        return ResponseCustom.OK();
    }

    /**
     * 관리자 차량 삭제
     */
    @Operation(summary = "관리자 차량 삭제 (박소정)", description = "관리자가 차량을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(C0001)존재하지 않는 차량입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "409", description = "(R0010)해당 장비의 예약 현황 수정이 필요합니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @DeleteMapping("/{carId}")
    public ResponseCustom updateCar(
            @Account User user,
            @Parameter(description = "(Long) 차량 Id", example = "1") @PathVariable(name = "carId") Long carId) {
        carService.deleteProductByAdmin(user, carId);
        return ResponseCustom.OK();
    }

    /**
     * 관리자 차량별 예약 이력을 조회한다.
     */
    @Operation(summary = "관리자 차량별 예약 이력을 조회 (이승학)", description = "관리자 차량별 예약 이력을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(C0001)존재하지 않는 차량입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @GetMapping("/{carId}")
    public ResponseCustom<AdminProductDetailsRes> getAdminCarDetails(
            @Account User user,
            @Parameter(description = "(Long) 차량 Id", example = "1") @PathVariable(name = "carId") Long carId) {
        {
            return ResponseCustom.OK(carService.getAdminProductsDetails(user, carId));
        }
    }

    /**
     * 관리자 차량 활성화/비활성화
     */
    @Operation(summary = "관리자 차량 활성화/비활성화 (이승학)", description = "관리자가 차량 활성화/비활성화 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(C0001)존재하지 않는 차량입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @PatchMapping("/{carId}/activation")
    public ResponseCustom activateCar(
            @Account User user,
            @Parameter(description = "(Long) 차량 Id", example = "1") @PathVariable(name="carId") Long carId) {
        carService.activateProductByAdmin(user, carId);
        return ResponseCustom.OK();
    }
}
