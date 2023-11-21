package com.example.pladialmserver.office.controller;

import com.example.pladialmserver.global.resolver.Account;
import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.office.dto.response.AdminOfficeRes;
import com.example.pladialmserver.office.dto.response.AdminOfficesDetailsRes;
import com.example.pladialmserver.office.service.OfficeService;
import com.example.pladialmserver.office.dto.request.CreateOfficeReq;
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
            @ApiResponse(responseCode = "400", description = "(O0003)회의실명을 입력해주세요. (O0004)설명은 255자 이하로 작성해주세요. (O0005)50자 이하로 작성해주세요. (O0006)회의실 위치를 입력해주세요. (O0007)회의실 시설을 입력해주세요. (O0008)수용인원을 입력해주세요. (O0009)회의실 설명을 입력해주세요.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다. (O0002)시설을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @PatchMapping("/{officeId}")
    public ResponseCustom updateOffice(
            @Account User user,
            @Parameter(description = "(Long) 회의실 Id", example = "1") @PathVariable(name="officeId") Long officeId,
            @RequestBody @Valid CreateOfficeReq request) {
        officeService.updateOffice(user, officeId, request);
        return ResponseCustom.OK();
    }

    /**
     * 관리자 회의실 삭제
     */
    @Operation(summary = "관리자 회의실 삭제 (이승학)", description = "관리자가 회의실을 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(O0001)존재하지 않는 회의실 입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "409", description = "(O0010)해당 자원의 예약 현황 수정이 필요합니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @DeleteMapping("/{officeId}")
    public ResponseCustom deleteResource(
            @Account User user,
            @Parameter(description = "(Long) 회의실 Id", example = "1") @PathVariable(name="officeId") Long officeId) {
        officeService.deleteOfficeByAdmin(user, officeId);
        return ResponseCustom.OK();
    }

    /**
     * 관리자 회의실별 예약 이력을 조회한다.
     */
    @Operation(summary = "관리자 회의실별 예약 이력을 조회 (이승학)", description = "관리자 회의실별 예약 이력을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(O0001)존재하지 않는 회의실 입니다.",content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @GetMapping("/offices/{officeId}")
    public ResponseCustom<AdminOfficesDetailsRes> getAdminOfficesDetails(
            @Account User user,
            @Parameter(description = "(Long) 회의실 Id", example = "1") @PathVariable(name = "officeId") Long officeId) {
        {
            return ResponseCustom.OK(officeService.getAdminOfficesDetails(user, officeId));
        }
    }

    /**
     * 관리자 회의실 활성화/비활성화
     */
    @Operation(summary = "관리자 회의실 활성화/비활성화 (이승학)", description = "관리자가 회의실을 활성화/비활성화 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(R0003)존재하지 않는 회의실입니다. (U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @PatchMapping("/{officeId}/activation")
    public ResponseCustom activateOffice(
            @Account User user,
            @Parameter(description = "(Long) 회의실 Id", example = "1") @PathVariable(name="officeId") Long officeId) {
        officeService.activateOfficeByAdmin(user, officeId);
        return ResponseCustom.OK();
    }
    /**
     * 관리자 전체 회의실 목록 조회 and 예약 가능한 회의실 목록 조회
     */
    @Operation(summary = "관리자 회의실 목록 조회 (이승학)", description = "관리자 회의실 목록 조회를 진행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)회의실 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "(B0001)날짜와 시간을 모두 입력해주세요.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
    })
    @GetMapping
    public ResponseCustom<Page<AdminOfficeRes>> searchOffice(
            @Account User user,
            @Parameter(description = "회의실 이름",example = "회의실1")@RequestParam(required = false) String facilityName,
            Pageable pageable
    ) {
        return ResponseCustom.OK(officeService.findAvailableAdminOffices(user,facilityName,pageable));
    }



}
