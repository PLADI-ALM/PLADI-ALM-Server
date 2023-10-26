package com.example.pladialmserver.equipment.repository;

import com.example.pladialmserver.equipment.entity.EquipmentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentCategoryRepository extends JpaRepository<EquipmentCategory, Long> {
    EquipmentCategory findByName(String category);
}
