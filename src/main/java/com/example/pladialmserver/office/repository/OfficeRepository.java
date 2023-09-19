package com.example.pladialmserver.office.repository;

import com.example.pladialmserver.office.entity.Office;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfficeRepository extends JpaRepository<Office, Long> {
    Optional<Office> findByOfficeIdAndIsEnable(Long officeId, boolean isEnable);
}
