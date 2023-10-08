package com.example.pladialmserver.user.service;

import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.utils.JwtUtil;
import com.example.pladialmserver.user.dto.TokenDto;
import com.example.pladialmserver.user.dto.request.LoginReq;
import com.example.pladialmserver.user.entity.User;
import com.example.pladialmserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.pladialmserver.global.exception.BaseResponseCode.INVALID_PASSWORD;
import static com.example.pladialmserver.global.exception.BaseResponseCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public TokenDto login(LoginReq loginReq) {
        // 이메일 확인
        User user = userRepository.findByEmail(loginReq.getEmail()).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
        // todo: 비밀번호 암호화 필요
        if(!user.getPassword().equals(loginReq.getPassword())) throw new BaseException(INVALID_PASSWORD);

        return jwtUtil.createToken(user.getUserId(), user.getRole());
    }
}
