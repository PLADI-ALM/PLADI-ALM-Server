package com.example.pladialmserver.equipment.service.model;

import com.example.pladialmserver.equipment.dto.request.RegisterEquipmentReq;

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
}
