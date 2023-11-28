package com.example.pladialmserver.equipment.dto.response;

import com.example.pladialmserver.equipment.entity.EquipmentCategory;
import lombok.Builder;
import lombok.Data;

@Data
public class CategoryNameRes {
    private String name;

    @Builder
    public CategoryNameRes(String name) {
        this.name = name;
    }

    public static CategoryNameRes toDto(EquipmentCategory category) {
        return CategoryNameRes.builder()
                .name(category.getName())
                .build();
    }
}
