package com.example.pladialmserver.user.controller;

import com.example.pladialmserver.global.resolver.Account;
import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.user.dto.request.CreateUserReq;
import com.example.pladialmserver.user.entity.User;
import com.example.pladialmserver.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
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
            @ApiResponse(responseCode = "400", description = "(U0002)이메일 형식을 확인해주세요. \n(U0003)비밀번호 형식을 확인해주세요. \n(U0004)이메일을 입력해주세요. \n(U0005) 비밀번호를 입력해주세요. \n(U0006) 비밀번호가 일치하지 않습니다. " +
                    "\n(U0007)성명을 입력해주세요. \n U0008)부서를 입력해주세요. \n(U0009)직위 입력해주세요. \n(U0010)직책 입력해주세요. \n(U0011)역할을 입력해주세요.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "403", description = "(G0002)접근 권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다.[관리자 정보를 찾을 수 없는 경우] \n (U0012)부서를 찾을 수 없습니다. \n (U0013)직위를 찾을 수 없습니다. \n (U0013)역할을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "409", description = "(U0014)존재하는 이메일입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @PostMapping("")
    public ResponseCustom createUser(@Account User user,
                                     @RequestBody @Valid CreateUserReq createUserReq) {
        userService.createUser(user, createUserReq);
        return ResponseCustom.OK();
    }

}
