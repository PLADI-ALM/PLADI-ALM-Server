package com.example.pladialmserver.office.repository;

import com.example.pladialmserver.office.entity.Office;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OfficeRepository extends JpaRepository<Office, Long> {
    Optional<Office> findByOfficeId(Long officeId);
    Page<Office> findAllByOfficeIdNotIn(List<Long> ids, Pageable pageable);
    Page<Office> findAll(Pageable pageable);
    @Query("SELECT ofc.office " +
            "FROM OfficeFacility ofc " +
            "WHERE ofc.facility.name = :facilityName")
    Page<Office> findByFacilityName(@Param("facilityName") String facilityName, Pageable pageable);

    @Query("SELECT ofc.office " +
            "FROM OfficeFacility ofc " +
            "WHERE ofc.facility.name = :facilityName" +
            " AND ofc.office.officeId " +
            "NOT IN :bookedOfficeIds")
    Page<Office> findByFacilityNameAndOfficeIdNotIn(@Param("facilityName") String facilityName, @Param("bookedOfficeIds") List<Long> bookedOfficeIds, Pageable pageable);


}
