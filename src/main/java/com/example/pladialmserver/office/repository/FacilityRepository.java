package com.example.pladialmserver.office.repository;

import com.example.pladialmserver.office.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacilityRepository extends JpaRepository<Facility, Long> {

    Optional<Facility> findByName(String facilityName);
}
