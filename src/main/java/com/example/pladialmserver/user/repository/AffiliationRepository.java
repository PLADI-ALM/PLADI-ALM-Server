package com.example.pladialmserver.user.repository;

import com.example.pladialmserver.user.entity.Affiliation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AffiliationRepository extends JpaRepository<Affiliation, Long> {
    Optional<Affiliation> findByNameAndIsEnable(String name, Boolean isEnable);
}
