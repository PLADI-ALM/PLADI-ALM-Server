package com.example.pladialmserver.user.controller;

import com.example.pladialmserver.global.resolver.Account;
import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.user.dto.request.CreateUserReq;
import com.example.pladialmserver.user.dto.request.UpdateUserReq;
import com.example.pladialmserver.user.dto.response.DepartmentListDto;
import com.example.pladialmserver.user.dto.response.UserRes;
import com.example.pladialmserver.user.entity.User;
import com.example.pladialmserver.user.service.UserService;
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

@Api(tags = "관리자 유저 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserService userService;

    @Operation(summary = " 직원 등록 (장채은)", description = "직원을 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)직원 등록 성공"),
            @ApiResponse(responseCode = "400", description = "(U0002)이메일 형식을 확인해주세요. \n(U0003)비밀번호 형식을 확인해주세요.\n(U0010)휴대폰 번호 형식을 확인해주세요. \n(U0004)이메일을 입력해주세요. \n(U0005) 비밀번호를 입력해주세요. \n(U0006) 비밀번호가 일치하지 않습니다. " +
                    "\n(U0007)성명을 입력해주세요. \n (U0008)부서를 입력해주세요. \n(U0009)휴대폰번호를 입력해주세요. \n(U0011)역할을 입력해주세요.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "403", description = "(G0002)접근 권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다.[관리자 정보를 찾을 수 없는 경우] \n (U0012)부서를 찾을 수 없습니다. \n (U0013)역할을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "409", description = "(U0014)존재하는 이메일입니다. (U0019)존재하는 휴대폰번호입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @PostMapping("")
    public ResponseCustom createUser(@Account User user,
                                     @RequestBody @Valid CreateUserReq createUserReq) {
        userService.createUser(user, createUserReq);
        return ResponseCustom.OK();
    }

    @Operation(summary = " 직원 수정 (장채은)", description = "직원을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)직원 수정 성공"),
            @ApiResponse(responseCode = "400", description = "(U0007)성명을 입력해주세요. \n U0008)부서를 입력해주세요. \n(U0010)휴대폰 번호 형식을 확인해주세요. \n(U0009)휴대폰번호를 입력해주세요. \n(U0010)직책을 입력해주세요. \n(U0011)역할을 입력해주세요.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "403", description = "(G0002)접근 권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다.[관리자 및 직원 모두] \n (U0012)부서를 찾을 수 없습니다. \n (U0013)역할을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "409", description = "(U0019)존재하는 휴대폰번호입니다. [자신 휴대폰번호 제외]", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @PatchMapping("/{userId}")
    public ResponseCustom updateUser(@Account User user,
                                     @Parameter(description = "(Long) 변경하려는 사용자 id", example = "1") @PathVariable(name = "userId") Long userId,
                                     @RequestBody @Valid UpdateUserReq updateUserReq) {
        userService.updateUser(user, userId, updateUserReq);
        return ResponseCustom.OK();
    }

    @Operation(summary = "부서 리스트 (장채은)", description = "부서 리스트를 확인한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)부서 리스트 확인 성공")
    })
    @GetMapping("/departments")
    public ResponseCustom<DepartmentListDto> getDepartmentList(@Account User user) {
        return ResponseCustom.OK(userService.getDepartmentList());
    }

    @Operation(summary = "직원 계정 목록 조회 (장채은)", description = "직원 계정 목록을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)직원 계정 목록 조회 성공"),
            @ApiResponse(responseCode = "403", description = "(G0002)접근 권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(U0012)부서를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("")
    public ResponseCustom<Page<UserRes>> getUserList(@Account User user,
                                                     Pageable pageable,
                                                     @Parameter(description = "(String) 성명 검색", example = "홍길동") @RequestParam(name = "name", required = false) String name,
                                                     @Parameter(description = "(String) 부서 검색", example = "마케팅") @RequestParam(name = "department", required = false) String department) {
        return ResponseCustom.OK(userService.getUserList(user, name, department, pageable));
    }

    @Operation(summary = "직원 개별 조회 (장채은)", description = "개별 직원의 정보를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)직원 개별 조회 성공"),
            @ApiResponse(responseCode = "403", description = "(G0002)접근 권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다.[관리자 및 직원 모두]", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("/{userId}")
    public ResponseCustom<UserRes> getUserInfo(@Account User user,
                                                     @Parameter(description = "(Long) 변경하려는 사용자 id", example = "1") @PathVariable(name = "userId") Long userId){
        return ResponseCustom.OK(userService.getUserInfo(user, userId));
    }

    @Operation(summary = "직원 탈퇴 (장채은)", description = "직원을 탈퇴 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)직원 탈퇴  성공"),
            @ApiResponse(responseCode = "403", description = "(G0002)접근 권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다.[관리자 및 직원 모두]", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @DeleteMapping("/{userId}")
    public ResponseCustom resignUser(@Account User user,
                                     @Parameter(description = "(Long) 삭제하려는 사용자 id", example = "1") @PathVariable(name = "userId") Long userId){
        userService.resignUser(user, userId);
        return ResponseCustom.OK();
    }

}
