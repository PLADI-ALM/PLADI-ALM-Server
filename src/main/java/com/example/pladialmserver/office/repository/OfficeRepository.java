package com.example.pladialmserver.office.repository;

import com.example.pladialmserver.office.entity.Office;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OfficeRepository extends JpaRepository<Office, Long>,OfficeCustom {
    Optional<Office> findByOfficeId(Long officeId);
    @Query("SELECT o " +
            "FROM Office o " +
            "WHERE o.isEnable = true AND o.isActive = true AND o.officeId NOT IN :ids")
    Page<Office> findAllByOfficeIdNotInAndIsEnableTrueAndIsActiveTrue(List<Long> ids, Pageable pageable);
    @Query("SELECT o " +
            "FROM Office o " +
            "WHERE o.isEnable = true AND o.isActive = true")
    Page<Office> findAllByIsEnableTrueAndIsActiveTrue(Pageable pageable);
    @Query("SELECT ofc.office " +
            "FROM OfficeFacility ofc " +
            "JOIN ofc.office o " +
            "WHERE o.isEnable = true AND o.isActive = true AND ofc.facility.name = :facilityName")
    Page<Office> findByFacilityNameAAndIsEnableTrueAndIsActiveTrue(@Param("facilityName") String facilityName, Pageable pageable);

    @Query("SELECT ofc.office " +
            "FROM OfficeFacility ofc " +
            "JOIN ofc.office o " +
            "WHERE o.isEnable = true AND o.isActive = true AND ofc.facility.name = :facilityName" +
            " AND ofc.office.officeId " +
            "NOT IN :bookedOfficeIds")
    Page<Office> findByFacilityNameAndOfficeIdNotInIAndIsEnableTrueAndIsActiveTrue(@Param("facilityName") String facilityName, @Param("bookedOfficeIds") List<Long> bookedOfficeIds, Pageable pageable);


    Optional<Office> findByOfficeIdAndIsEnable(Long officeId, boolean isEnable);

    Optional<Office> findByOfficeIdAndIsEnableAndIsActive(Long officeId, boolean isEnable, boolean isActive);
}
