package com.example.pladialmserver.user.service;

import com.example.pladialmserver.global.Constants;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.feign.feignClient.ArchivingServerClient;
import com.example.pladialmserver.global.utils.JwtUtil;
import com.example.pladialmserver.user.dto.TokenDto;
import com.example.pladialmserver.user.dto.request.CreateUserReq;
import com.example.pladialmserver.user.dto.request.LoginReq;
import com.example.pladialmserver.user.dto.response.UserPositionRes;
import com.example.pladialmserver.user.entity.Department;
import com.example.pladialmserver.user.entity.Position;
import com.example.pladialmserver.user.entity.Role;
import com.example.pladialmserver.user.entity.User;
import com.example.pladialmserver.user.repository.DepartmentRepository;
import com.example.pladialmserver.user.repository.PositionRepository;
import com.example.pladialmserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import static com.example.pladialmserver.global.exception.BaseResponseCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final ArchivingServerClient archivingServerClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 로그인
    public TokenDto login(LoginReq loginReq) {
        User user = userRepository.findByEmail(loginReq.getEmail()).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
        if(!passwordEncoder.matches(loginReq.getPassword(), user.getPassword())) throw new BaseException(INVALID_PASSWORD);
        return jwtUtil.createToken(user.getUserId(), user.getRole());
    }

    // 사용자 정보
    public UserPositionRes getUserPosition(User user) {
        return UserPositionRes.toDto(user);
    }

    // 로그아웃
    public void logout(User user, HttpServletRequest request) {
        String bearerToken = request.getHeader(Constants.JWT.AUTHORIZATION_HEADER);
        bearerToken = bearerToken.substring(Constants.JWT.BEARER_PREFIX.length());
        jwtUtil.setBlackListToken(bearerToken, Constants.JWT.LOGOUT);
        jwtUtil.deleteRefreshToken(user.getUserId());
    }

    // ===================================================================================================================
    // [관리자-사용자]
    // ===================================================================================================================

    // 직원 등록
    @Transactional
    public void createUser(User admin, CreateUserReq createUserReq) {
        // admin 사용자 확인
        if (!admin.getRole().equals(Role.ADMIN)) throw new BaseException(NO_AUTHENTICATION);
        // 이메일 중복 확인
        if(userRepository.existsByEmail(createUserReq.getEmail())) throw new BaseException(EXISTS_EMAIL);
        // 회원 생성 리소스 접근
        Department department = departmentRepository.findByName(createUserReq.getDepartment()).orElseThrow(() -> new BaseException(DEPARTMENT_NOT_FOUND));
        Position position = positionRepository.findByName(createUserReq.getPosition()).orElseThrow(() -> new BaseException(POSITION_NOT_FOUND));
        // 비밀번호 암호화
        createUserReq.setPassword(passwordEncoder.encode(createUserReq.getPassword()));
        // 사용자 저장
        userRepository.save(User.toEntity(createUserReq, department, position));
    }
}
