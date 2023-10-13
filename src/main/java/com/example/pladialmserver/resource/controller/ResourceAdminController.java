package com.example.pladialmserver.resource.controller;

import com.example.pladialmserver.global.resolver.Account;
import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.resource.dto.response.AdminResourcesRes;
import com.example.pladialmserver.resource.service.ResourceService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "관리자 자원 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/resources")
public class ResourceAdminController {

    private final ResourceService resourceService;

    /**
     * 자원 카테고리
     */
    @Operation(summary = "자원 카테고리 (차유상)", description = "자원 카테고리를 가져온다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @GetMapping("/category")
    public ResponseCustom getResourceCategory(@Account User user) {
        return ResponseCustom.OK(resourceService.getResourceCategory(user));
    }

    /**
     * 관리자 자원 목록 조회
     */
    @Operation(summary = "관리자 자원 목록 조회 (박소정)", description = "관리자가 자원 목록을 조회 및 검색한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @GetMapping("")
    public ResponseCustom<Page<AdminResourcesRes>> getResources(
            @Account User user,
            @Parameter(description = "(String) 이름 검색어", example = "'MacBook'") @RequestParam(required = false) String keyword,
            @PageableDefault(size = 8) Pageable pageable) {
            return ResponseCustom.OK(resourceService.getResourcesByAdmin(user, keyword, pageable));
        }

}
