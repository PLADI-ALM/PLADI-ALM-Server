package com.example.pladialmserver.equipment.repository;

import com.example.pladialmserver.equipment.entity.Equipment;
import com.example.pladialmserver.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    Optional<Equipment> findByEquipmentIdAndIsEnable(Long equipmentId, Boolean isEnable);
    Page<Equipment> findByNameContainsAndIsEnable(String cond, Pageable pageable, Boolean isEnable);
    Boolean existsByUserAndIsEnable(User user, Boolean isEnable);
}
