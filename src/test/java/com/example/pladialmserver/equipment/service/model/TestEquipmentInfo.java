package com.example.pladialmserver.equipment.service.model;

import com.example.pladialmserver.equipment.dto.request.RegisterEquipmentReq;
import com.example.pladialmserver.equipment.dto.request.UpdateEquipmentReq;
import com.example.pladialmserver.equipment.entity.Equipment;
import com.example.pladialmserver.equipment.entity.EquipmentCategory;
import com.example.pladialmserver.user.entity.User;

public class TestEquipmentInfo {

    public static RegisterEquipmentReq setUpRegisterEquipmentInfo(String name, String quantity, String category, String location, String description, String imgKey )  {
        return RegisterEquipmentReq.builder()
                .name(name)
                .quantity(quantity)
                .category(category)
                .location(location)
                .description(description)
                .imgKey(imgKey)
                .build();

    }
  
    public static Equipment setUpEquipment(String name, String quantity, String imgKey, String location, String description, EquipmentCategory category, User user) {
        return Equipment.builder()
                .name(name)
                .quantity(quantity)
                .imgKey(imgKey)
                .location(location)
                .description(description)
                .category(category)
                .user(user)
                .build();
    }
  
    public static EquipmentCategory setUpEquipmentCategory(String name) {
        return EquipmentCategory.builder()
                .name(name)
                .build();
    }

    public static UpdateEquipmentReq setUpUpdateEquipmentReq(String name, String quantity, Long register, Long userId, String location, String description, String category, String imgKey) {
        return UpdateEquipmentReq.builder()
                .name(name)
                .quantity(quantity)
                .register(register)
                .userId(userId)
                .location(location)
                .description(description)
                .category(category)
                .imgKey(imgKey)
                .build();

    }
}
