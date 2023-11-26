package com.example.pladialmserver.user.service;

import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.global.utils.JwtUtil;
import com.example.pladialmserver.user.dto.TokenDto;
import com.example.pladialmserver.user.dto.request.CreateUserReq;
import com.example.pladialmserver.user.dto.request.EmailPWReq;
import com.example.pladialmserver.user.dto.response.UserRes;
import com.example.pladialmserver.user.entity.Affiliation;
import com.example.pladialmserver.user.entity.Department;
import com.example.pladialmserver.user.entity.Role;
import com.example.pladialmserver.user.entity.User;
import com.example.pladialmserver.user.repository.AffiliationRepository;
import com.example.pladialmserver.user.repository.DepartmentRepository;
import com.example.pladialmserver.user.repository.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static com.example.pladialmserver.user.service.model.TestUserInfo.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AffiliationRepository affiliationRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    JwtUtil jwtUtil;
    @Spy
    BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("[성공] 로그인")
    void login(){
        // given
        User user = setUpUser(1L, Role.ADMIN, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        EmailPWReq req = setUpEmailPWReq("test@email.com", "asdf1234!");

        // when
        // stub 생성
        doReturn(Optional.of(user)).when(userRepository).findByEmailAndIsEnable(req.getEmail(), true);
        when(jwtUtil.createToken(user.getUserId(), user.getRole())).thenReturn(TokenDto.toDto("accessToken", "refreshToken", user.getRole()));
        TokenDto dto = userService.login(req);

        // then
        assertThat(req.getEmail()).isEqualTo(user.getEmail());
        assertThat(passwordEncoder.matches(req.getPassword(), user.getPassword())).isTrue();
        assertThat(dto.getAccessToken()).isEqualTo("accessToken");
        assertThat(dto.getRefreshToken()).isEqualTo("refreshToken");

        // verify
        verify(userRepository, times(1)).findByEmailAndIsEnable(any(String.class), any(Boolean.class));
        verify(jwtUtil, times(1)).createToken(any(Long.class), any(Role.class));
        verify(passwordEncoder, times(1)).encode(any(String.class));
    }

    @Test
    @DisplayName("[실패] 로그인")
    void loginFail(){
        // given
        EmailPWReq req = setUpEmailPWReq("test1@email.com", "asdf1234!");
        // when
        doThrow(new BaseException(BaseResponseCode.USER_NOT_FOUND)).when(userRepository).findByEmailAndIsEnable(req.getEmail(), true);
        // then
        BaseException exception = assertThrows(BaseException.class, () -> {
            userService.login(req);
        });
        assertThat(exception.getBaseResponseCode()).isEqualTo(BaseResponseCode.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("[성공] 직원 계정 생성")
    void createUser() {
        // given
        Department department = setUpDepartment();
        Affiliation affiliation = setUpAffiliation();
        User admin = setUpUser(1L, Role.ADMIN, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        CreateUserReq createUserReq = setUpCreateUserReq("test1@email.com", "adsf1234!");

        //when
        doReturn(Optional.of(department)).when(departmentRepository).findByNameAndIsEnable(department.getName(), true);
        doReturn(Optional.of(affiliation)).when(affiliationRepository).findByNameAndIsEnable(affiliation.getName(), true);
        when(userRepository.save(any(User.class))).then(AdditionalAnswers.returnsFirstArg());
        userService.createUser(admin, createUserReq);

        // verify - because of void method
        verify(userRepository, times(1)).existsByEmailAndIsEnable(any(String.class), any(Boolean.class));
        verify(userRepository, times(1)).existsByPhoneAndIsEnable(any(String.class), any(Boolean.class));
        verify(departmentRepository, times(1)).findByNameAndIsEnable(any(String.class), any(Boolean.class));
        verify(affiliationRepository, times(1)).findByNameAndIsEnable(any(String.class), any(Boolean.class));
        verify(passwordEncoder, times(2)).encode(any(String.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("[실패] 직원 계정 생성 - 관리자 접근이 아닌 경우")
    void createUserFail() {
        // given
        User admin = setUpUser(1L, Role.BASIC, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        CreateUserReq createUserReq = setUpCreateUserReq("test1@email.com", "adsf1234!");

        // when
        BaseException exception = assertThrows(BaseException.class, () -> {
            userService.createUser(admin, createUserReq);
        });
        // then
        assertThat(exception.getBaseResponseCode()).isEqualTo(BaseResponseCode.NO_AUTHENTICATION);
    }

    @Test
    @DisplayName("[실패] 직원 계정 생성 - 존재하는 휴대폰 번호인 경우")
    void createUserFail2() {
        // given
        User admin = setUpUser(1L, Role.ADMIN, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        CreateUserReq createUserReq = setUpCreateUserReq("test1@email.com", "adsf1234!");

        // when
        doReturn(true).when(userRepository).existsByPhoneAndIsEnable(createUserReq.getPhone(), true);
        BaseException exception = assertThrows(BaseException.class, () -> {
            userService.createUser(admin, createUserReq);
        });

        // then
        assertThat(exception.getBaseResponseCode()).isEqualTo(BaseResponseCode.EXISTS_PHONE);
    }

    @Test
    @DisplayName("[성공] 직원 개별 정보 (관리자 전용)")
    void getUserInfo() {
        // given
        User admin = setUpUser(1L, Role.ADMIN, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        User user = setUpUser(1L, Role.ADMIN, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));

        // when
        when(userRepository.findByUserIdAndIsEnable(user.getUserId(), true)).thenReturn(Optional.of(user));
        UserRes userRes = userService.getUserInfoByAdmin(admin, user.getUserId());

        // then
        assertThat(userRes.getUserId()).isEqualTo(user.getUserId());

        // verify
        verify(userRepository, times(1)).findByUserIdAndIsEnable(any(Long.class), any(Boolean.class));
    }

    @Test
    @DisplayName("[실패] 직원 개별 정보 (관리자 전용) - 관리자 접근이 아닌 경우")
    void getUserInfoFail() {
        // given
        User admin = setUpUser(1L, Role.BASIC, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));

        // when
        BaseException exception = assertThrows(BaseException.class, () -> {
            userService.getUserInfoByAdmin(admin, 1L);
        });

        // then
        assertThat(exception.getBaseResponseCode()).isEqualTo(BaseResponseCode.NO_AUTHENTICATION);
    }

    @Test
    @DisplayName("[실패] 직원 개별 정보 (관리자 전용) - 사용자를 찾을 수 없는 경우")
    void getUserInfoFail2() {
        // given
        User admin = setUpUser(1L, Role.ADMIN, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));

        // when
        doThrow(new BaseException(BaseResponseCode.USER_NOT_FOUND)).when(userRepository).findByUserIdAndIsEnable(1L, true);
        BaseException exception = assertThrows(BaseException.class, () -> {
            userService.getUserInfoByAdmin(admin, 1L);
        });

//        // then
        assertThat(exception.getBaseResponseCode()).isEqualTo(BaseResponseCode.USER_NOT_FOUND);
    }


    @Test
    void getUserName() {
    }

    @Test
    void setExpiredToken() {
    }

    @Test
    void reissue() {
    }

    @Test
    void verifyEmail() {
    }

    @Test
    void checkEmailCode() {
    }

    @Test
    void resetPassword() {
    }

    @Test
    void getResponsibilityList() {
    }

    @Test
    void resignUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void sendAssetsEmail() {
    }

    @Test
    void updateUserByAdmin() {
    }

    @Test
    void getDepartmentList() {
    }

    @Test
    void getUserList() {
    }

    @Test
    void getUserInfoByAdmin() {
    }

    @Test
    void resignUserByAdmin() {
    }

    @Test
    void getUserNotification() {
    }
}