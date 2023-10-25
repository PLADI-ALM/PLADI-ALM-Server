package com.example.pladialmserver.equipment.entity;

import com.example.pladialmserver.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EquipmentCategory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long equipmentCategoryId;

    @NotNull
    @Size(max = 30)
    private String name;

    @Builder
    public EquipmentCategory(String name) {
        this.name = name;
    }

    public static EquipmentCategory toEntity(String category) {
        return EquipmentCategory.builder()
                .name(category)
                .build();
    }
}
