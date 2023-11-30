package com.example.pladialmserver.user.service;

import com.example.pladialmserver.equipment.repository.EquipmentRepository;
import com.example.pladialmserver.global.Constants;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.utils.EmailUtil;
import com.example.pladialmserver.global.utils.JwtUtil;
import com.example.pladialmserver.notification.entity.PushNotification;
import com.example.pladialmserver.notification.repository.PushNotificationRepository;
import com.example.pladialmserver.product.car.repository.CarRepository;
import com.example.pladialmserver.product.resource.repository.ResourceRepository;
import com.example.pladialmserver.user.dto.TokenDto;
import com.example.pladialmserver.user.dto.request.*;
import com.example.pladialmserver.user.dto.response.DepartmentListDto;
import com.example.pladialmserver.user.dto.response.NotificationRes;
import com.example.pladialmserver.user.dto.response.UserNameRes;
import com.example.pladialmserver.user.dto.response.UserRes;
import com.example.pladialmserver.user.entity.Affiliation;
import com.example.pladialmserver.user.entity.Department;
import com.example.pladialmserver.user.entity.Role;
import com.example.pladialmserver.user.entity.User;
import com.example.pladialmserver.user.repository.AffiliationRepository;
import com.example.pladialmserver.user.repository.DepartmentRepository;
import com.example.pladialmserver.user.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import static com.example.pladialmserver.global.Constants.EmailNotification.*;
import static com.example.pladialmserver.global.exception.BaseResponseCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PushNotificationRepository notificationRepository;
    private final CarRepository carRepository;
    private final EquipmentRepository equipmentRepository;
    private final ResourceRepository resourceRepository;
    private final AffiliationRepository affiliationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailUtil emailUtil;

    // 로그인
    @Transactional
    public TokenDto login(EmailPWReq loginReq) {
        User user = userRepository.findByEmailAndIsEnable(loginReq.getEmail(), true).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
        if(!passwordEncoder.matches(loginReq.getPassword(), user.getPassword())) throw new BaseException(INVALID_PASSWORD);
        if (StringUtils.hasText(loginReq.getFcmToken())) user.updateFcmToken(loginReq.getFcmToken());
        return jwtUtil.createToken(user.getUserId(), user.getRole());
    }

    // 사용자 정보
    public UserNameRes getUserName(User user) {
        return UserNameRes.toDto(user);
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
        String code = RandomStringUtils.random(5, true, true);
        emailUtil.sendEmail(verifyEmailReq.getEmail(), COMPANY_NAME + FIND_EMAIL_CODE_TITLE, emailUtil.createEmailCodeData(code), EMAIL_TEMPLATE);
        emailUtil.setEmailCodeInRedis(verifyEmailReq.getEmail(), code);
    }

    // 이메일 인증 코드 확인
    public void checkEmailCode(CheckEmailCodeReq checkEmailCodeReq) {
        userRepository.findByEmailAndIsEnable(checkEmailCodeReq.getEmail(), true).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
        if(!checkEmailCodeReq.getCode().equals(emailUtil.verifiedCode(checkEmailCodeReq.getEmail()))) throw new BaseException(EMAIL_CODE_NOT_FOUND);
    }


    // 비밀번호 재설정
    @Transactional
    public void resetPassword(ResetPWReq resetPasswordReq) {
        User user = userRepository.findByEmailAndIsEnable(resetPasswordReq.getEmail(), true).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
        user.updatePassword(passwordEncoder.encode(resetPasswordReq.getPassword()));
        userRepository.save(user);
    }

    public ResponsibilityListRes getResponsibilityList(String name) {
        return ResponsibilityListRes.toDto(userRepository.findAllByName(name));
    }
    // 직원 접근 탈퇴
    public void resignUser(User user) {
        // 탈퇴하려는 회원이 장비, 차량 관리자인 경우 애러처리
        if (resourceRepository.existsByUserAndIsEnable(user, true)) throw new BaseException(EXISTS_RESOURCE_ADMIN_USER);
        if (carRepository.existsByUserAndIsEnable(user, true)) throw new BaseException(EXISTS_CAR_ADMIN_USER);
        if (equipmentRepository.existsByUserAndIsEnable(user, true)) throw new BaseException(EXISTS_CAR_ADMIN_USER);
        jwtUtil.deleteRefreshToken(user.getUserId());
        userRepository.delete(user);
    }

    // 직원 정보 수정
    @Transactional
    public void updateUser(User user, UpdateUserReq updateUserReq) {
        if(userRepository.existsByPhoneAndUserIdNotAndIsEnable(updateUserReq.getPhone(), user.getUserId(), true)) throw new BaseException(EXISTS_PHONE);
        user.updateUser(updateUserReq);
        userRepository.save(user);
    }

    // 직원 개별 정보
    public UserRes getUserInfo(User user) {
        return UserRes.toDto(user);
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
        Department department = departmentRepository.findByNameAndIsEnable(createUserReq.getDepartment(), true).orElseThrow(() -> new BaseException(DEPARTMENT_NOT_FOUND));
        Affiliation affiliation = affiliationRepository.findByNameAndIsEnable(createUserReq.getAffiliation(), true).orElseThrow(() -> new BaseException(AFFILIATION_NOT_FOUND));
        // 비밀번호 암호화
        createUserReq.setPassword(passwordEncoder.encode(createUserReq.getPassword()));
        // 사용자 저장
        User user = User.toEntity(createUserReq, department, affiliation);
        userRepository.save(user);
    }

    // 직원 수정
    @Transactional
    public void updateUserByAdmin(User admin, Long userId, AdminUpdateUserReq updateUserReq) {
        if (!admin.checkRole(Role.ADMIN)) throw new BaseException(NO_AUTHENTICATION);

        User user = userRepository.findByUserIdAndIsEnable(userId, true).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
        if(userRepository.existsByPhoneAndUserIdNotAndIsEnable(updateUserReq.getPhone(), user.getUserId(), true)) throw new BaseException(EXISTS_PHONE);
        Department department = departmentRepository.findByNameAndIsEnable(updateUserReq.getDepartment(), true).orElseThrow(() -> new BaseException(DEPARTMENT_NOT_FOUND));
        Affiliation affiliation = affiliationRepository.findByNameAndIsEnable(updateUserReq.getAffiliation(), true).orElseThrow(() -> new BaseException(AFFILIATION_NOT_FOUND));

        user.updateUser(UpdateUserReq.toDto(updateUserReq));
        user.updateRole(updateUserReq.getRole());
        user.updateAffiliation(affiliation);
        user.updateDepartment(department);
        userRepository.save(user);
    }

    // 부서 리스트
    public DepartmentListDto getDepartmentList() {
        return DepartmentListDto.toDto(departmentRepository.findAll());
    }

    // 직원 계정 목록 조회
    public Page<UserRes> getUserList(User admin, String name, String department, String affiliation, Pageable pageable) {
        if (!admin.checkRole(Role.ADMIN)) throw new BaseException(NO_AUTHENTICATION);
        Department dpmEntity = null;
        Affiliation affEntity = null;
        if(StringUtils.hasText(department)) dpmEntity = departmentRepository.findByNameAndIsEnable(department, true).orElseThrow(() -> new BaseException(DEPARTMENT_NOT_FOUND));
        if(StringUtils.hasText(affiliation)) affEntity = affiliationRepository.findByNameAndIsEnable(affiliation, true).orElseThrow(() -> new BaseException(AFFILIATION_NOT_FOUND));
        return userRepository.findAllByName(name, dpmEntity, affEntity, pageable).map(UserRes::toDto);
    }

    // 직원 개별 정보
    public UserRes getUserInfoByAdmin(User admin, Long userId) {
        if (!admin.checkRole(Role.ADMIN)) throw new BaseException(NO_AUTHENTICATION);
        User user = userRepository.findByUserIdAndIsEnable(userId, true).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
        return UserRes.toDto(user);
    }

    // 직원 탈퇴
    @Transactional
    public void resignUserByAdmin(User admin, Long userId) {
        if (!admin.checkRole(Role.ADMIN)) throw new BaseException(NO_AUTHENTICATION);
        User user = userRepository.findByUserIdAndIsEnable(userId, true).orElseThrow(() -> new BaseException(USER_NOT_FOUND));
        resignUser(user);
    }

    public Page<NotificationRes> getUserNotification(User user, Pageable pageable) {
        Page<PushNotification> notifications = notificationRepository.findByUserAndIsEnableOrderByCreatedAtDesc(user, true, pageable);
        return notifications.map(NotificationRes::toDto);
    }
}
