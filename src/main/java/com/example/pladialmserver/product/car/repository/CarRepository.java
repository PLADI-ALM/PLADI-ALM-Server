package com.example.pladialmserver.product.car.repository;

import com.example.pladialmserver.product.car.entity.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long>,CarCustom {
    @Query("SELECT c " +
            "FROM Car c " +
            "WHERE c.name = :carName " +
            "AND c.isEnable = true AND c.isActive = true AND c.carId NOT IN :carIds")
    Page<Car> findByNameAndCarIdNotInAAndIsEnableTrueAAndIsActiveTrue(String carName, List<Long> carIds, Pageable pageable);
    @Query("SELECT c " +
            "FROM Car c " +
            "WHERE c.name LIKE %:carName% " +
            "AND c.isEnable = true AND c.isActive = true")
    Page<Car> findByNameContainingAAndIsEnableTrueAndIsActiveTrue(String carName, Pageable pageable);
    @Query("SELECT c " +
            "FROM Car c " +
            "WHERE c.isEnable = true AND c.isActive = true")
    Page<Car> findAllByIsEnableTrueAndIsActiveTrue(Pageable pageable);

    Optional<Car> findByCarIdAndIsEnableAndIsActive(Long carId, boolean isEnable, boolean isActive);
}
