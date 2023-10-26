package com.example.pladialmserver.equipment.service;

import com.example.pladialmserver.equipment.dto.request.RegisterEquipmentReq;
import com.example.pladialmserver.equipment.dto.response.SearchEquipmentRes;
import com.example.pladialmserver.equipment.entity.Equipment;
import com.example.pladialmserver.equipment.entity.EquipmentCategory;
import com.example.pladialmserver.equipment.repository.EquipmentCategoryRepository;
import com.example.pladialmserver.equipment.repository.EquipmentRepository;
import com.example.pladialmserver.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentCategoryRepository equipmentCategoryRepository;

    @Transactional
    public void registerEquipment(RegisterEquipmentReq registerEquipmentReq, User user) {
        EquipmentCategory category = equipmentCategoryRepository.findByName(registerEquipmentReq.getCategory());

        if(category == null) {
            category = EquipmentCategory.toEntity(registerEquipmentReq.getCategory());
            equipmentCategoryRepository.save(category);
        }

        equipmentRepository.save(Equipment.toEntity(registerEquipmentReq, category, user));
    }

    public Page<SearchEquipmentRes> searchEquipment(String cond, Pageable pageable) {
        Page<Equipment> equipments;

        if(StringUtils.hasText(cond)) equipments = equipmentRepository.findByNameContains(cond, pageable);
        else equipments = equipmentRepository.findAll(pageable);
        return equipments.map(SearchEquipmentRes::toDto);
    }
}
