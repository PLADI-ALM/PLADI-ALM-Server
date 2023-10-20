package com.example.pladialmserver.user.controller;

import com.example.pladialmserver.global.resolver.Account;
import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.user.dto.TokenDto;
import com.example.pladialmserver.user.dto.request.CheckEmailCodeReq;
import com.example.pladialmserver.user.dto.request.LoginReq;
import com.example.pladialmserver.user.dto.request.VerifyEmailReq;
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

    @Operation(summary = "로그인 (장채은)", description = "로그인을 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)로그인 성공"),
            @ApiResponse(responseCode = "400", description = "(U0002)이메일 형식을 확인해주세요.\n (U0003)비밀번호 형식을 확인해주세요. \n (U0004)이메일을 입력해주세요.\n (U0005)비밀번호를 입력해주세요. \n (U0006)비밀번호가 일치하지 않습니다. ", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @PostMapping("/login")
    public ResponseCustom<TokenDto> login(@RequestBody @Valid LoginReq loginReq){
        return ResponseCustom.OK(userService.login(loginReq));
    }

    @Operation(summary = "사용자 정보 (장채은)", description = "사이드바에 필요한 사용자 정보를 불러온다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)사용자 정보 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("/position")
    public ResponseCustom<UserPositionRes> getUserPosition(@Account User user){
        return ResponseCustom.OK(userService.getUserPosition(user));
    }

    @Operation(summary = "로그아웃 (장채은)", description = "로그아웃을 진행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)로그아웃 성공"),
    })
    @PostMapping("/logout")
    public ResponseCustom logout(@Account User user, HttpServletRequest request){
        userService.setExpiredToken(user, request);
        return ResponseCustom.OK();
    }

    @Operation(summary = "토큰 재발급 (장채은)", description = "토큰 재발급을 진행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)토큰 재발급 성공"),
            @ApiResponse(responseCode = "403", description = "(G0001)잘못된 요청입니다.")
    })
    @PostMapping("/reissuance")
    public ResponseCustom reissue(@RequestBody @Valid TokenDto tokenDto){
        return ResponseCustom.OK(userService.reissue(tokenDto));
    }

    @Operation(summary = "이메일 인증번호 전송 (장채은)", description = "이메일 인증번호를 전송한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)이메일 인증번호 전송 성공"),
            @ApiResponse(responseCode = "400", description = "(U0002)이메일 형식을 확인해주세요.\n (U0004)이메일을 입력해주세요."),
            @ApiResponse(responseCode = "500", description = "(U0015)이메일을 보낼 수 없습니다.")
    })
    @PostMapping("/email")
    public ResponseCustom verifyEmail(@RequestBody @Valid VerifyEmailReq verifyEmailReq){
        userService.verifyEmail(verifyEmailReq);
        return ResponseCustom.OK();
    }


    @Operation(summary = "이메일 인증번호 코드 확인 (장채은)", description = "이메일에 전송된 코드가 맞는지 확인한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)이메일 인증번호 확인 성공"),
            @ApiResponse(responseCode = "400", description = "(U0002)이메일 형식을 확인해주세요.\n (U0004)이메일을 입력해주세요.\n (U0016)이메일 코드를 입력해주세요."),
            @ApiResponse(responseCode = "500", description = "(U0017)이메일 코드가 일치하지 않습니다.\n (U0018)없거나 이미 만료된 이메일 코드입니다.")
    })
    @PostMapping("/email-code")
    public ResponseCustom checkEmailCode(@RequestBody @Valid CheckEmailCodeReq checkEmailCodeReq){
        userService.checkEmailCode(checkEmailCodeReq);
        return ResponseCustom.OK();
    }
}
