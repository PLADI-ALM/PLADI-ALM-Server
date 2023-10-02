package com.example.pladialmserver.office.repository;

import com.example.pladialmserver.office.entity.Office;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OfficeRepository extends JpaRepository<Office, Long> {
    Optional<Office> findByOfficeId(Long officeId);
    Page<Office> findAllByOfficeIdNotIn(List<Long> ids, Pageable pageable);
    Page<Office> findAll(Pageable pageable);

}
