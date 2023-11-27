package com.example.pladialmserver.office.repository.facility;

import com.example.pladialmserver.office.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacilityRepository extends JpaRepository<Facility, Long>, FacilityCustom {

    Optional<Facility> findByName(String facilityName);
}
