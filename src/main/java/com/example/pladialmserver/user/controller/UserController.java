package com.example.pladialmserver.user.controller;

import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.user.dto.TokenDto;
import com.example.pladialmserver.user.dto.request.LoginReq;
import com.example.pladialmserver.user.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "유저 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseCustom<TokenDto> login(@RequestBody @Valid LoginReq loginReq){
        return ResponseCustom.OK(userService.login(loginReq));
    }

}
