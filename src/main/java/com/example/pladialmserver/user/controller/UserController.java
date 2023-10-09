package com.example.pladialmserver.user.controller;

import com.example.pladialmserver.global.resolver.Account;
import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.user.dto.TokenDto;
import com.example.pladialmserver.user.dto.request.LoginReq;
import com.example.pladialmserver.user.dto.response.UserPositionRes;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@Api(tags = "유저 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * [토큰 X] 로그인
     */
    @Operation(summary = "로그인", description = "로그인을 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)로그인 성공"),
            @ApiResponse(responseCode = "400", description = "(U0002)이메일 형식을 확인해주세요.\n (U0003)비밀번호 형식을 확인해주세요. \n (U0004)이메일을 입력해주세요.\n (U0005)비밀번호를 입력해주세요. \n (U0006)비밀번호가 일치하지 않습니다. ", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @PostMapping("/login")
    public ResponseCustom<TokenDto> login(@RequestBody @Valid LoginReq loginReq){
        return ResponseCustom.OK(userService.login(loginReq));
    }

    /**
     * 사이드바 사용자 정보
     */
    @Operation(summary = "사용자 정보", description = "사이드바에 필요한 사용자 정보를 불러온다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)사용자 정보 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("/position")
    public ResponseCustom<UserPositionRes> getUserPosition(@Account User user){
        return ResponseCustom.OK(userService.getUserPosition(user));
    }

    /**
     * 로그아웃
     */
    @Operation(summary = "로그아웃", description = "로그아웃을 진행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)로그아웃 성공"),
    })
    @PostMapping("/position")
    public ResponseCustom logout(@Account User user, HttpServletRequest request){
        userService.logout(user, request);
        return ResponseCustom.OK();
    }
}
