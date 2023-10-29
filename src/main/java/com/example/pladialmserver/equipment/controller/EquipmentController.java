package com.example.pladialmserver.equipment.controller;


import com.example.pladialmserver.equipment.dto.request.RegisterEquipmentReq;
import com.example.pladialmserver.equipment.dto.request.UpdateEquipmentReq;
import com.example.pladialmserver.equipment.dto.response.SearchEquipmentRes;
import com.example.pladialmserver.equipment.service.EquipmentService;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "비품 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/equipments")
public class EquipmentController {
    private final EquipmentService equipmentService;

    /*
    구매한 비품을 등록한다.
     */
    @Operation(summary = "구매 비품 등록 (김민기)", description = "구매한 비품을 등록한다..")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "400", description = "(E0001)부적절한 비품 등록 요청입니다. 공백및 특수문자를 제외하고 다시 입력해주세요.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @PostMapping("")
    public ResponseCustom registerEquipment(
            @RequestBody @Valid RegisterEquipmentReq registerEquipmentReq,
            @Account User user
    )
    {

        equipmentService.registerEquipment(registerEquipmentReq, user);
        return ResponseCustom.OK();
    }

    /*
    비품 목록을 조회한다.
     */
    @Operation(summary = "비품 목록 조회 (김민기)", description = "비품 목록을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
    })
    @GetMapping("")
    public ResponseCustom<Page<SearchEquipmentRes>> searchEquipment(
            String cond,
            Pageable pageable
    )
    {
        return ResponseCustom.OK(equipmentService.searchEquipment(cond, pageable));
    }

    /*
    비품 정보를 수정한다.
     */
    @Operation(summary = "비품 정보 수정 (김민기)", description = "비품 정보를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(E0002)존재하지 않는 비품입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "400", description = "(E0003)부적절한 비품 수정 요청입니다. 공백및 특수문자를 제외하고 다시 입력해주세요.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @PatchMapping("{equipmentId}")
    public ResponseCustom updateEquipment(
            @RequestBody @Valid UpdateEquipmentReq updateEquipmentReq,
            @Parameter(description = "(Long) 비품 Id", example = "1") @PathVariable Long equipmentId,
            @Account User user
    )
    {
        equipmentService.updateEquipment(equipmentId, updateEquipmentReq, user);
        return ResponseCustom.OK();
    }

    /*
    비품 정보를 삭제한다.
     */
    @DeleteMapping("{equipmentId}")
    public ResponseCustom deleteEquipment(
            @Parameter(description = "(Long) 비품 Id", example = "1") @PathVariable Long equipmentId,
            @Account User user
    )
    {
        equipmentService.deleteEquipment(equipmentId, user);
        return ResponseCustom.OK();
    }
}
