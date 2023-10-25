package com.example.pladialmserver.equipment.service;

import com.example.pladialmserver.equipment.dto.request.RegisterEquipmentReq;
import com.example.pladialmserver.equipment.entity.Equipment;
import com.example.pladialmserver.equipment.entity.EquipmentCategory;
import com.example.pladialmserver.equipment.repository.EquipmentCategoryRepository;
import com.example.pladialmserver.equipment.repository.EquipmentRepository;
import com.example.pladialmserver.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentCategoryRepository equipmentCategoryRepository;

    @Transactional
    public void registerEquipment(RegisterEquipmentReq registerEquipmentReq, User user) {
        boolean empty = equipmentCategoryRepository.findByName(registerEquipmentReq.getCategory()).isEmpty();
        EquipmentCategory category = EquipmentCategory.toEntity(registerEquipmentReq.getCategory());
        if(empty) equipmentCategoryRepository.save(category);

        equipmentRepository.save(Equipment.toEntity(registerEquipmentReq, category, user));
    }
}
