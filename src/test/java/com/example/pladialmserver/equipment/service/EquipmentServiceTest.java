package com.example.pladialmserver.equipment.service;

import com.example.pladialmserver.equipment.dto.request.RegisterEquipmentReq;
import com.example.pladialmserver.equipment.dto.request.UpdateEquipmentReq;
import com.example.pladialmserver.equipment.entity.Equipment;
import com.example.pladialmserver.equipment.entity.EquipmentCategory;
import com.example.pladialmserver.equipment.repository.EquipmentCategoryRepository;
import com.example.pladialmserver.equipment.repository.EquipmentRepository;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.user.entity.Role;
import com.example.pladialmserver.user.entity.User;
import com.example.pladialmserver.user.repository.user.UserRepository;
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

import static com.example.pladialmserver.equipment.service.model.TestEquipmentInfo.*;
import static com.example.pladialmserver.user.service.model.TestUserInfo.*;
import static com.example.pladialmserver.user.service.model.TestUserInfo.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipmentServiceTest {

    @Spy
    @InjectMocks
    private EquipmentService equipmentService;
    @Mock
    private EquipmentRepository equipmentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EquipmentCategoryRepository equipmentCategoryRepository;
    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @Test
    @DisplayName("[성공] 구매 비품 추가")
    void registerEquipment_SUCCESS() {
        // given
        User user = setUpUser(1L, Role.ADMIN, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        RegisterEquipmentReq req = setUpRegisterEquipmentInfo("맥심커피", "10박스", "기타", "S1305", "맥심커피입니다.", "photo/maxim.png");
        EquipmentCategory category = setUpEquipmentCategory(req.getCategory());

        // when
        doReturn(category).when(equipmentCategoryRepository).findByName(req.getCategory());
        equipmentService.registerEquipment(req, user);

        // then
        assertThat(req.getCategory()).isEqualTo(category.getName());

        // verify
        verify(equipmentCategoryRepository, times(1)).findByName(any(String.class));
        verify(equipmentService, times(1)).registerEquipment(any(RegisterEquipmentReq.class), any(User.class));
        verify(passwordEncoder, times(1)).encode(any(String.class));
    }

    @Test
    @DisplayName("[성공] 비품 정보 수정")
    void updateEquipment_SUCCESS() {
        // given
        Long equipmentId = 1L;
        User user = setUpUser(1L, Role.ADMIN, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        UpdateEquipmentReq req = setUpUpdateEquipmentReq("비품2", "10개", 1L, 2L, "S1350", "비품2입니다.", "기타", "photo/maxim.png");
        EquipmentCategory category = setUpEquipmentCategory(req.getCategory());
        Equipment equipment = setUpEquipment("비품1", "10개", "photo/maxim.png", "S1350", "비품1입니다.", category, user);

        // when
        doReturn(Optional.of(equipment)).when(equipmentRepository).findByEquipmentIdAndIsEnable(equipmentId, true);
        doReturn(Optional.of(user)).when(userRepository).findByUserIdAndIsEnable(req.getUserId(), true);
        equipmentService.updateEquipment(equipmentId, req, user);

        // verify
        verify(equipmentService, times(1)).updateEquipment(any(Long.class), any(UpdateEquipmentReq.class), any(User.class));
        verify(passwordEncoder, times(1)).encode(any(String.class));
    }

    @Test
    @DisplayName("[실패] 비품 정보 수정 - 존재하지 않는 비품일 경우")
    void updateEquipment_EQUIPMENT_NOT_FOUND() {
        // given
        User user = setUpUser(1L, Role.ADMIN, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        UpdateEquipmentReq req = setUpUpdateEquipmentReq("비품2", "10개", 1L, 2L, "S1350", "비품2입니다.", "기타", "photo/maxim.png");
        Long fakeId = 9999L;
        // when
        BaseException exception = assertThrows(BaseException.class, () -> {
            equipmentService.updateEquipment(fakeId, req, user);
        });
        // then
        assertThat(exception.getBaseResponseCode()).isEqualTo(BaseResponseCode.EQUIPMENT_NOT_FOUND);

    }

    @Test
    @DisplayName("[성공] 비품 정보 삭제")
    void deleteEquipment_SUCCESS() {
        // given
        Long equipmentId = 1L;
        User user = setUpUser(1L, Role.ADMIN, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        Equipment equipment = setUpEquipment("비품1", "10개", "photo/maxim.png", "S1350", "비품1입니다.", new EquipmentCategory("기타"), user);

        // when
        doReturn(Optional.of(equipment)).when(equipmentRepository).findByEquipmentIdAndIsEnable(equipmentId, true);
        equipmentService.deleteEquipment(equipmentId, user);

        // verify
        verify(equipmentService, times(1)).deleteEquipment(equipmentId, user);
    }

    @Test
    @DisplayName("[실패] 비품 정보 삭제 - 관리자가 아닌 경우")
    void deleteEquipment_NO_AUTHENTICATION() {
        // given
        User admin = setUpUser(1L, Role.ADMIN, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        User basic = setUpUser(2L, Role.BASIC, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        Equipment equipment = setUpEquipment("비품1", "10개", "photo/maxim.png", "S1350", "비품1입니다.", new EquipmentCategory("기타"), admin);

        // when
        doReturn(Optional.of(equipment)).when(equipmentRepository).findByEquipmentIdAndIsEnable(equipment.getEquipmentId(), true);
        BaseException exception = assertThrows(BaseException.class, () -> {
            equipmentService.deleteEquipment(equipment.getEquipmentId(), basic);
        });

        // then
        assertThat(exception.getBaseResponseCode()).isEqualTo(BaseResponseCode.NO_AUTHENTICATION);
    }
}
