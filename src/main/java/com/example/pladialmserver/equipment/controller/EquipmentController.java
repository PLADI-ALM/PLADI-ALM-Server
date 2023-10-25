package com.example.pladialmserver.equipment.controller;


import com.example.pladialmserver.equipment.dto.request.RegisterEquipmentReq;
import com.example.pladialmserver.equipment.service.EquipmentService;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.global.resolver.Account;
import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.user.entity.User;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @PostMapping("")
    public ResponseCustom registerEquipment(
            @RequestBody @Valid RegisterEquipmentReq registerEquipmentReq,
            @Account User user
    )
    {
        if(!StringUtils.hasText(registerEquipmentReq.getName()) || !StringUtils.hasText(registerEquipmentReq.getCategory()) ||
                !StringUtils.hasText(registerEquipmentReq.getDescription()) || !StringUtils.hasText(registerEquipmentReq.getLocation()) ||
                !StringUtils.hasText(registerEquipmentReq.getImgKey()) || registerEquipmentReq.getQuantity() == 0)
            throw new BaseException(BaseResponseCode.INVALID_REGISTER_EQUIPMENT_REQUEST);

        equipmentService.registerEquipment(registerEquipmentReq, user);
        return ResponseCustom.OK();
    }

    /*
    비품 목록을 조회한다.
     */

    /*
    비품 정보를 수정한다.
     */

    /*
    비품 정보를 삭제한다.
     */
}
