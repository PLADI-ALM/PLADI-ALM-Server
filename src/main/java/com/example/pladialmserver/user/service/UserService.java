package com.example.pladialmserver.user.service;

import com.example.pladialmserver.global.Constants;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.feign.feignClient.ArchivingServerClient;
import com.example.pladialmserver.global.utils.EmailUtil;
import com.example.pladialmserver.global.utils.JwtUtil;
import com.example.pladialmserver.user.dto.TokenDto;
import com.example.pladialmserver.user.dto.request.*;
import com.example.pladialmserver.user.dto.response.CompanyRankListRes;
import com.example.pladialmserver.user.dto.response.UserPositionRes;
import com.example.pladialmserver.user.dto.response.UserRes;
import com.example.pladialmserver.user.entity.Department;
import com.example.pladialmserver.user.entity.Role;
import com.example.pladialmserver.user.entity.User;
import com.example.pladialmserver.user.repository.DepartmentRepository;
import com.example.pladialmserver.user.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final ArchivingServerClient archivingServerClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailUtil emailUtil;

    // 로그인
    public TokenDto login(EmailPWReq loginReq) {
        User user = userRepository.findByEmailAndIsEnable(loginReq.getEmail(), true).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
        if(!passwordEncoder.matches(loginReq.getPassword(), user.getPassword())) throw new BaseException(INVALID_PASSWORD);
        return jwtUtil.createToken(user.getUserId(), user.getRole());
    }

    // 사용자 정보
    public UserPositionRes getUserPosition(User user) {
        return UserPositionRes.toDto(user);
    }

    // 토큰 만료
    public void setExpiredToken(User user, HttpServletRequest request) {
        String bearerToken = request.getHeader(Constants.JWT.AUTHORIZATION_HEADER);
        bearerToken = JwtUtil.replaceBearerToken(bearerToken);
        jwtUtil.setBlackListToken(bearerToken, Constants.JWT.LOGOUT);
        jwtUtil.deleteRefreshToken(user.getUserId());
    }

    // 토큰 재발급
    public TokenDto reissue(TokenDto tokenDto) {
        // parsing
        tokenDto.setAccessToken(JwtUtil.replaceBearerToken(tokenDto.getAccessToken()));
        tokenDto.setRefreshToken(JwtUtil.replaceBearerToken(tokenDto.getRefreshToken()));

        // user
        Long userId = jwtUtil.getUserIdFromJWT(tokenDto.getAccessToken());
        User user = userRepository.findByUserIdAndIsEnable(userId, true).orElseThrow(() -> new BaseException(USER_NOT_FOUND));

        // refreshToken 검증 밀 db 삭제
        jwtUtil.validateToken(tokenDto.getRefreshToken());
        jwtUtil.validateRefreshToken(userId, tokenDto.getRefreshToken());
        jwtUtil.deleteRefreshToken(userId);

        // 재발급
        return jwtUtil.createToken(user.getUserId(), user.getRole());
    }

    // 이메일 인증 전송
    public void verifyEmail(VerifyEmailReq verifyEmailReq) {
        userRepository.findByEmailAndIsEnable(verifyEmailReq.getEmail(), true).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
        emailUtil.sendEmail(verifyEmailReq.getEmail());
    }

    // 이메일 인증 코드 확인
    public void checkEmailCode(CheckEmailCodeReq checkEmailCodeReq) {
        userRepository.findByEmailAndIsEnable(checkEmailCodeReq.getEmail(), true).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
        if(!checkEmailCodeReq.getCode().equals(emailUtil.verifiedCode(checkEmailCodeReq.getEmail()))) throw new BaseException(EMAIL_CODE_NOT_FOUND);
    }


    // 비밀번호 재설정
    @Transactional
    public void resetPassword(EmailPWReq resetPasswordReq) {
        User user = userRepository.findByEmailAndIsEnable(resetPasswordReq.getEmail(), true).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
        user.updatePassword(passwordEncoder.encode(resetPasswordReq.getPassword()));
        userRepository.save(user);
    }

    // ===================================================================================================================
    // [관리자-사용자]
    // ===================================================================================================================

    // 직원 등록
    @Transactional
    public void createUser(User admin, CreateUserReq createUserReq) {
        if (!admin.checkRole(Role.ADMIN)) throw new BaseException(NO_AUTHENTICATION);
        // 이메일 및 휴대폰번호 중복 확인
        if(userRepository.existsByEmailAndIsEnable(createUserReq.getEmail(), true)) throw new BaseException(EXISTS_EMAIL);
        if(userRepository.existsByPhoneAndIsEnable(createUserReq.getPhone(), true)) throw new BaseException(EXISTS_PHONE);
        // 회원 생성 리소스 접근
        Department department = departmentRepository.findByName(createUserReq.getDepartment()).orElseThrow(() -> new BaseException(DEPARTMENT_NOT_FOUND));
        // 비밀번호 암호화
        createUserReq.setPassword(passwordEncoder.encode(createUserReq.getPassword()));
        // 사용자 저장
        userRepository.save(User.toEntity(createUserReq, department));
    }

    // 직원 수정
    @Transactional
    public void updateUser(User admin, Long userId, UpdateUserReq updateUserReq) {
        if (!admin.checkRole(Role.ADMIN)) throw new BaseException(NO_AUTHENTICATION);
        // 정보 변경 사용자 정보 확인
        User user = userRepository.findByUserIdAndIsEnable(userId, true).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
        if(userRepository.existsByPhoneAndUserIdNotAndIsEnable(updateUserReq.getPhone(), user.getUserId(), true)) throw new BaseException(EXISTS_PHONE);
        Department department = departmentRepository.findByName(updateUserReq.getDepartment()).orElseThrow(() -> new BaseException(DEPARTMENT_NOT_FOUND));
        // 수정 및 저장
        user.updateUser(updateUserReq, department);
        userRepository.save(user);
    }

    // 부서 및 직책 리스트
    public CompanyRankListRes getCompanyRankList() {
        return CompanyRankListRes.toDto(departmentRepository.findAll());
    }

    // 직원 계정 목록 조회
    public Page<UserRes> getUserList(User admin, String name, Pageable pageable) {
        if (!admin.checkRole(Role.ADMIN)) throw new BaseException(NO_AUTHENTICATION);
        return userRepository.findAllByName(name, pageable).map(UserRes::toDto);
    }

    // 직원 개별 정보
    public UserRes getUserInfo(User admin, Long userId) {
        if (!admin.checkRole(Role.ADMIN)) throw new BaseException(NO_AUTHENTICATION);
        User user = userRepository.findByUserIdAndIsEnable(userId, true).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
        return UserRes.toDto(user);
    }

    // 직원 탈퇴
    @Transactional
    public void resignUser(User admin, Long userId) {
        if (!admin.checkRole(Role.ADMIN)) throw new BaseException(NO_AUTHENTICATION);
        User user = userRepository.findByUserIdAndIsEnable(userId, true).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
        jwtUtil.deleteRefreshToken(user.getUserId());
        userRepository.delete(user);
    }

}
