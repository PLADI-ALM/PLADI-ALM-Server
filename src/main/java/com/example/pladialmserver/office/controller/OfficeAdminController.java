package com.example.pladialmserver.office.controller;

import com.example.pladialmserver.booking.dto.response.AdminBookingRes;
import com.example.pladialmserver.global.resolver.Account;
import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.office.service.OfficeService;
import com.example.pladialmserver.resource.dto.request.CreateOfficeReq;
import com.example.pladialmserver.resource.dto.request.CreateResourceReq;
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

@Api(tags = "관리자 회의실 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("admin/offices")
public class OfficeAdminController {
    private final OfficeService officeService;

    /**
     * 관리자 회의실 추가
     */
    @Operation(summary = "관리자 회의실 추가(이승학)", description = "관리자 회의실 추가한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "(O0003)회의실명을 입력해주세요. (O0004)설명은 255자 이하로 작성해주세요. (O0005)50자 이하로 작성해주세요. (O0006)회의실 위치를 입력해주세요. (O0007)회의실 시설을 입력해주세요. (O0008)수용인원을 입력해주세요. (O0009)회의실 설명을 입력해주세요.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다. (O0002)시설을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @PostMapping("")
    public ResponseCustom createOffice(
            @Account User user,
            @RequestBody @Valid CreateOfficeReq request){
        officeService.createOfficeByAdmin(user, request);
        return ResponseCustom.OK();
    }

    /**
     * 관리자 회의실 수정
     */
    @Operation(summary = "관리자 회의실 수정 (이승학)", description = "관리자가 회의실을 수정한다. (요청 값 모두 필요)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "400", description = "(R0004)설명은 255자 이하로 작성해주세요. (R0005)자원명은 50자 이하로 작성해주세요. (R0007)자원명을 입력해주세요. (R0008)카테고리를 입력해주세요. (R0009)설명을 입력해주세요.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(R0003)존재하지 않는 자원입니다. (U0001)사용자를 찾을 수 없습니다. (R0006)카테고리를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @PatchMapping("/{officeId}")
    public ResponseCustom updateOffice(
            @Account User user,
            @Parameter(description = "(Long) 회의실 Id", example = "1") @PathVariable(name="officeId") Long officeId,
            @RequestBody @Valid CreateOfficeReq request) {
        officeService.updateOffice(user, officeId, request);
        return ResponseCustom.OK();
    }


}
