package com.example.pladialmserver.user.controller;

import com.example.pladialmserver.global.feign.dto.UserReq;
import com.example.pladialmserver.global.response.ResponseCustom;
import com.example.pladialmserver.user.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "유저 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/add")
    public ResponseCustom<?> addUser(@RequestBody UserReq userReq) {
        userService.addUser(userReq);
        return ResponseCustom.OK();
    }
}
