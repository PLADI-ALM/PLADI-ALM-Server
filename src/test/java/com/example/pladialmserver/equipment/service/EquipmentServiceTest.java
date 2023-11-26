package com.example.pladialmserver.equipment.service;

import com.example.pladialmserver.equipment.dto.request.RegisterEquipmentReq;
import com.example.pladialmserver.equipment.repository.EquipmentCategoryRepository;
import com.example.pladialmserver.equipment.repository.EquipmentRepository;
import com.example.pladialmserver.user.entity.Role;
import com.example.pladialmserver.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.example.pladialmserver.equipment.service.model.TestEquipmentInfo.setUpRegisterEquipmentInfo;
import static com.example.pladialmserver.user.service.model.TestUserInfo.*;
import static com.example.pladialmserver.user.service.model.TestUserInfo.PASSWORD;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EquipmentServiceTest {

    @Spy
    @InjectMocks
    private EquipmentService equipmentService;
    @Mock
    private EquipmentRepository equipmentRepository;
    @Mock
    private EquipmentCategoryRepository equipmentCategoryRepository;
    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @Test
    @DisplayName("[성공] 구매 비품 추가")
    void registerEquipmentSuccess() {
        // given
        User user = setUpUser(1L, Role.ADMIN, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        RegisterEquipmentReq req = setUpRegisterEquipmentInfo("맥심커피", "10박스", "기타", "S1305", "맥심커피입니다.", "photo/maxim.png");

        // when
        equipmentService.registerEquipment(req, user);

        // verify
        verify(equipmentService, times(1)).registerEquipment(any(RegisterEquipmentReq.class), any(User.class));
        verify(passwordEncoder, times(1)).encode(any(String.class));
    }

    @Test
    void updateEquipment() {
    }

    @Test
    void deleteEquipment() {
    }
}
