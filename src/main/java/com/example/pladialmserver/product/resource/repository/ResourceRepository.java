package com.example.pladialmserver.product.resource.repository;

import com.example.pladialmserver.product.resource.entity.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long>, ResourceCustom {
    @Query("SELECT r " +
            "FROM Resource r " +
            "WHERE r.name = :resourceName " +
            "AND r.isEnable = true AND r.isActive = true AND r.resourceId NOT IN :resourceIds")
    Page<Resource> findByNameAndResourceIdNotInAAndIsEnableTrueAAndIsActiveTrue(String resourceName, List<Long> resourceIds, Pageable pageable);
    @Query("SELECT r " +
            "FROM Resource r " +
            "WHERE r.name LIKE %:resourceName% " +
            "AND r.isEnable = true AND r.isActive = true")
    Page<Resource> findByNameContainingAAndIsEnableTrueAndIsActiveTrue(String resourceName, Pageable pageable);

    Page<Resource> findByNameContainingOrderByName(String resourceName, Pageable pageable);

    Optional<Resource> findByResourceIdAndIsEnable(Long resourceId, boolean isEnable);

    Optional<Resource> findByResourceIdAndIsEnableAndIsActive(Long resourceId, boolean isEnable,boolean isActive);

    @Query("SELECT r " +
            "FROM Resource r " +
            "WHERE r.isEnable = true AND r.isActive = true")
    Page<Resource> findAllByIsEnableTrueAndIsActiveTrue(Pageable pageable);

}
