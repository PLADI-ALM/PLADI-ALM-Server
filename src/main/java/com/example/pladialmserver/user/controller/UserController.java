package com.example.pladialmserver.user.controller;

import com.example.pladialmserver.global.resolver.Account;
import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.user.dto.TokenDto;
import com.example.pladialmserver.user.dto.request.*;
import com.example.pladialmserver.user.dto.response.NotificationRes;
import com.example.pladialmserver.user.dto.response.UserNameRes;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@Api(tags = "유저 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "로그인 (장채은) [토큰 X]", description = "로그인을 한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)로그인 성공"),
            @ApiResponse(responseCode = "400", description = "(U0002)이메일 형식을 확인해주세요.\n (U0003)비밀번호 형식을 확인해주세요. \n (U0004)이메일을 입력해주세요.\n (U0005)비밀번호를 입력해주세요. \n (U0006)비밀번호가 일치하지 않습니다. ", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @PostMapping("/login")
    public ResponseCustom<TokenDto> login(@RequestBody @Valid EmailPWReq loginReq){
        return ResponseCustom.OK(userService.login(loginReq));
    }

    @Operation(summary = "사용자 정보 (장채은)", description = "사이드바에 필요한 사용자 정보를 불러온다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)사용자 정보 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("/position")
    public ResponseCustom<UserNameRes> getUserName(@Account User user){
        return ResponseCustom.OK(userService.getUserName(user));
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

    @Operation(summary = "토큰 재발급 (장채은) [토큰 X]", description = "토큰 재발급을 진행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)토큰 재발급 성공"),
            @ApiResponse(responseCode = "403", description = "(G0001)잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @PostMapping("/reissuance")
    public ResponseCustom reissue(@RequestBody @Valid TokenDto tokenDto){
        return ResponseCustom.OK(userService.reissue(tokenDto));
    }

    @Operation(summary = "이메일 인증번호 전송 (장채은) [토큰 X]", description = "이메일 인증번호를 전송한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)이메일 인증번호 전송 성공"),
            @ApiResponse(responseCode = "400", description = "(U0002)이메일 형식을 확인해주세요.\n (U0004)이메일을 입력해주세요.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "500", description = "(U0015)이메일을 보낼 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @PostMapping("/email")
    public ResponseCustom verifyEmail(@RequestBody @Valid VerifyEmailReq verifyEmailReq){
        userService.verifyEmail(verifyEmailReq);
        return ResponseCustom.OK();
    }


    @Operation(summary = "이메일 인증번호 코드 확인 (장채은) [토큰 X]", description = "이메일에 전송된 코드가 맞는지 확인한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)이메일 인증번호 확인 성공"),
            @ApiResponse(responseCode = "400", description = "(U0002)이메일 형식을 확인해주세요.\n (U0004)이메일을 입력해주세요.\n (U0016)이메일 코드를 입력해주세요.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "500", description = "(U0017)이메일 코드가 일치하지 않습니다.\n (U0018)없거나 이미 만료된 이메일 코드입니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @PostMapping("/email-code")
    public ResponseCustom checkEmailCode(@RequestBody @Valid CheckEmailCodeReq checkEmailCodeReq){
        userService.checkEmailCode(checkEmailCodeReq);
        return ResponseCustom.OK();
    }

    @Operation(summary = "비밀번호 재설정 (장채은) [토큰 X]", description = "비밀번호를 재설정한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)비밀번호 재설정 성공", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @PatchMapping("/password")
    public ResponseCustom resetPassword(@RequestBody @Valid EmailPWReq resetPasswordReq){
        userService.resetPassword(resetPasswordReq);
        return ResponseCustom.OK();
    }

    @Operation(summary = " 관리책임자 리스트 (장채은)", description = "관리책임자 리스트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("/managers")
    public ResponseCustom<ResponsibilityListRes> getResponsibilityList(
            @Account User user,
            @Parameter(description = "(String) 성명 검색", example = "홍길동") @RequestParam(name = "name", required = false) String name) {
        return ResponseCustom.OK(userService.getResponsibilityList(name));
    }

    @Operation(summary = "직원 탈퇴 (장채은)", description = "직원을 탈퇴한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)직원 탈퇴 성공"),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @DeleteMapping("")
    public ResponseCustom resignUser(@Account User user){
        userService.resignUser(user);
        return ResponseCustom.OK();
    }

    @Operation(summary = "사용자 푸쉬 알림 조회 (김민기)", description = "사용자 푸쉬 알림 내역을 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)요청에 성공했습니다."),
            @ApiResponse(responseCode = "403", description = "(G0002)접근권한이 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("/notification")
    public ResponseCustom<Page<NotificationRes>> getUserNotification(
            @Account User user,
            Pageable pageable
    )
    {
        return ResponseCustom.OK(userService.getUserNotification(user, pageable));
    }

    @Operation(summary = " 직원 수정 (장채은)", description = "직원을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)직원 수정 성공"),
            @ApiResponse(responseCode = "400", description = "(U0007)성명을 입력해주세요. \n U0008)부서를 입력해주세요. \n(U0010)휴대폰 번호 형식을 확인해주세요. \n(U0009)휴대폰번호를 입력해주세요. ", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다. \n", content = @Content(schema = @Schema(implementation = ResponseCustom.class))),
            @ApiResponse(responseCode = "409", description = "(U0019)존재하는 휴대폰번호입니다. [자신 휴대폰번호 제외]", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @PatchMapping("")
    public ResponseCustom updateUser(@Account User user,
                                     @RequestBody @Valid UpdateUserReq updateUserReq) {
        userService.updateUser(user, updateUserReq);
        return ResponseCustom.OK();
    }

    @Operation(summary = "직원 개별 조회 (장채은)", description = "개별 직원의 정보를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "(S0001)직원 개별 조회 성공"),
            @ApiResponse(responseCode = "404", description = "(U0001)사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ResponseCustom.class)))
    })
    @GetMapping("")
    public ResponseCustom<UserRes> getUserInfo(@Account User user){
        return ResponseCustom.OK(userService.getUserInfo(user));
    }
}
