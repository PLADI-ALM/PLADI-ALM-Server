package com.example.pladialmserver.resource.controller;

import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.resource.dto.response.AdminResourceRes;
import com.example.pladialmserver.resource.service.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "관리자 자원 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/resources")
public class ResourceAdminController {
    private final ResourceService resourceService;

    /**
     * 관리자 자원 예약 목록을 조회
     */
    @Operation(summary = "관리자 자원 예약 목록 조회", description = "관리자 페이지에서 자원 예약 내역을 전체 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
    })
    @GetMapping("/resources")
    public ResponseCustom<Page<AdminResourceRes>> getBookingResources(
            @PageableDefault(size = 8) Pageable pageable){
        return ResponseCustom.OK(resourceService.getBookingResources(pageable));
    }
}
