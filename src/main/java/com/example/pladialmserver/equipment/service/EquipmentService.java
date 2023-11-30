package com.example.pladialmserver.equipment.service;

import com.example.pladialmserver.equipment.dto.request.RegisterEquipmentReq;
import com.example.pladialmserver.equipment.dto.request.UpdateEquipmentReq;
import com.example.pladialmserver.equipment.dto.response.EquipmentCategoryRes;
import com.example.pladialmserver.equipment.dto.response.GetEquipmentRes;
import com.example.pladialmserver.equipment.dto.response.SearchEquipmentRes;
import com.example.pladialmserver.equipment.entity.Equipment;
import com.example.pladialmserver.equipment.entity.EquipmentCategory;
import com.example.pladialmserver.equipment.repository.EquipmentCategoryRepository;
import com.example.pladialmserver.equipment.repository.EquipmentRepository;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.user.entity.Role;
import com.example.pladialmserver.user.entity.User;
import com.example.pladialmserver.user.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentCategoryRepository equipmentCategoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public void registerEquipment(RegisterEquipmentReq registerEquipmentReq, User user) {
        EquipmentCategory category = extractCategory(registerEquipmentReq.getCategory());
        equipmentRepository.save(Equipment.toEntity(registerEquipmentReq, category, user));
    }

    public Page<SearchEquipmentRes> searchEquipment(String cond, Pageable pageable) {
        Page<Equipment> equipments;

        if(StringUtils.hasText(cond)) equipments = equipmentRepository.findByNameContainsAndIsEnable(cond, pageable, true);
        else equipments = equipmentRepository.findAll(pageable);
        return equipments.map(SearchEquipmentRes::toDto);
    }

    @Transactional
    public void updateEquipment(Long equipmentId, UpdateEquipmentReq updateEquipmentReq, User user) {
        Equipment equipment = equipmentRepository.findByEquipmentIdAndIsEnable(equipmentId, true).orElseThrow(() -> new BaseException(BaseResponseCode.EQUIPMENT_NOT_FOUND));

        User register = userRepository.findByUserIdAndIsEnable(updateEquipmentReq.getUserId(), true).orElseThrow(() -> new BaseException(BaseResponseCode.USER_NOT_FOUND));
        EquipmentCategory updateCategory = extractCategory(updateEquipmentReq.getCategory());
        equipment.toUpdateInfo(updateEquipmentReq, register, updateCategory);
    }

    private EquipmentCategory extractCategory(String name) {
        EquipmentCategory category = equipmentCategoryRepository.findByName(name);

        if(category == null) {
            category = EquipmentCategory.toEntity(name);
            equipmentCategoryRepository.save(category);
        }
        return category;
    }

    @Transactional
    public void deleteEquipment(Long equipmentId, User user) {
        Equipment equipment = equipmentRepository.findByEquipmentIdAndIsEnable(equipmentId, true).orElseThrow(() -> new BaseException(BaseResponseCode.EQUIPMENT_NOT_FOUND));
        if(!user.getRole().equals(Role.ADMIN)) throw new BaseException(BaseResponseCode.NO_AUTHENTICATION);

        equipmentRepository.delete(equipment);
    }

    public EquipmentCategoryRes getEquipmentCategories(User user) {
        List<EquipmentCategory> categories = equipmentCategoryRepository.findByIsEnable(true);
        return EquipmentCategoryRes.toDto(categories);
    }

    public GetEquipmentRes getEquipment(Long equipmentId) {
        Equipment equipment = equipmentRepository.findByEquipmentIdAndIsEnable(equipmentId, true).orElseThrow(() -> new BaseException(BaseResponseCode.EQUIPMENT_NOT_FOUND));
        return GetEquipmentRes.toDto(equipment);
    }
}
