package com.example.pladialmserver.product.resource.controller;

import com.example.pladialmserver.global.resolver.Account;
import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.product.dto.request.CreateProductReq;
import com.example.pladialmserver.product.dto.response.AdminProductDetailsRes;
import com.example.pladialmserver.product.dto.response.AdminProductsRes;
import com.example.pladialmserver.product.resource.service.ResourceService;
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


@Api(tags = "관리자 장비 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/resources")
public class ResourceAdminController {

    private final ResourceService resourceService;

    /**
     * 관리자 장비 목록 조회
     */
    @Operation(summary = "관리자 장비 목록 조회 (박소정)", description = "관리자가 장비 목록을 조회 및 검색한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @GetMapping("")
    public ResponseCustom<Page<AdminProductsRes>> getResources(
            @Account User user,
            @Parameter(description = "(String) 장비명 검색어", example = "MacBook") @RequestParam(required = false) String keyword,
            @PageableDefault(size = 8) Pageable pageable) {
        return ResponseCustom.OK(resourceService.getProductByAdmin(user, keyword, pageable));
    }

    /**
     * 관리자 장비 추가
     */
    @Operation(summary = "관리자 장비 추가 (박소정)", description = "관리자가 장비를 추가한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "(R0004)설명은 255자 이하로 작성해주세요. (R0005)장비명은 50자 이하로 작성해주세요. (R0006)보관장소를 입력해주세요. (R0007)장비명을 입력해주세요. (R0008)책임자를 입력해주세요. (R0009)설명을 입력해주세요. (R0012)제조사는 30자 이하로 작성해주세요.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @PostMapping("")
    public ResponseCustom createResource(
            @Account User user,
            @RequestBody @Valid CreateProductReq request) {
        resourceService.createProductByAdmin(user, request);
        return ResponseCustom.OK();
    }

    /**
     * 관리자 장비 수정
     */
    @Operation(summary = "관리자 장비 수정 (박소정)", description = "관리자가 장비를 수정한다. (요청 값 모두 필요)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "(R0004)설명은 255자 이하로 작성해주세요. (R0005)장비명은 50자 이하로 작성해주세요. (R0006)보관장소를 입력해주세요. (R0007)장비명을 입력해주세요. (R0008)책임자를 입력해주세요. (R0009)설명을 입력해주세요.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(R0003)존재하지 않는 장비입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @PatchMapping("/{resourceId}")
    public ResponseCustom updateResource(
            @Account User user,
            @Parameter(description = "(Long) 장비 Id", example = "1") @PathVariable(name = "resourceId") Long resourceId,
            @RequestBody @Valid CreateProductReq request) {
        resourceService.updateProductByAdmin(user, resourceId, request);
        return ResponseCustom.OK();
    }


    /**
     * 관리자 장비 삭제
     */
    @Operation(summary = "관리자 장비 삭제 (박소정)", description = "관리자가 장비를 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(R0003)존재하지 않는 장비입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "409", description = "(R0010)해당 장비의 예약 현황 수정이 필요합니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @DeleteMapping("/{resourceId}")
    public ResponseCustom updateResource(
            @Account User user,
            @Parameter(description = "(Long) 장비 Id", example = "1") @PathVariable(name="resourceId") Long resourceId) {
        resourceService.deleteProductByAdmin(user, resourceId);
        return ResponseCustom.OK();
    }

    /**
     * 관리자 장비별 예약 이력을 조회한다.
     */
    @Operation(summary = "관리자 장비별 예약 이력을 조회 (이승학)", description = "관리자 장비별 예약 이력을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(R0003)존재하지 않는 장비입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @GetMapping("/{resourceId}")
    public ResponseCustom<AdminProductDetailsRes> getAdminResourcesDetails(
            @Account User user,
            @Parameter(description = "(Long) 장비 Id", example = "1") @PathVariable(name = "resourceId") Long resourceId) {
        {
            return ResponseCustom.OK(resourceService.getAdminProductsDetails(user, resourceId));
        }
    }

    /**
     * 관리자 장비 활성화/비활성화
     */
    @Operation(summary = "관리자 장비 활성화/비활성화 (박소정)", description = "관리자가 장비를 활성화/비활성화 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(R0003)존재하지 않는 장비입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @PatchMapping("/{resourceId}/activation")
    public ResponseCustom activateResource(
            @Account User user,
            @Parameter(description = "(Long) 장비 Id", example = "1") @PathVariable(name="resourceId") Long resourceId) {
        resourceService.activateProductByAdmin(user, resourceId);
        return ResponseCustom.OK();
    }
}
