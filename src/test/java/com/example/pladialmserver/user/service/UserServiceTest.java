package com.example.pladialmserver.user.service;

import com.example.pladialmserver.global.utils.JwtUtil;
import com.example.pladialmserver.user.dto.TokenDto;
import com.example.pladialmserver.user.dto.request.EmailPWReq;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static com.example.pladialmserver.user.service.model.TestUserInfo.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
        User user = setUpUser(setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
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
    void getUserInfo() {
    }

    @Test
    void sendAssetsEmail() {
    }

    @Test
    void createUser() {
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