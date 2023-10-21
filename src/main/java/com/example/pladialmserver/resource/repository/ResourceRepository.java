package com.example.pladialmserver.resource.repository;

import com.example.pladialmserver.resource.entity.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Page<Resource> findByNameAndResourceIdNotIn(String resourceName, List<Long> resourceIds, Pageable pageable);


    Page<Resource> findByNameContaining(String resourceName,Pageable pageable);
    Page<Resource> findByNameContainingOrderByName(String resourceName,Pageable pageable);
    Optional<Resource> findByResourceIdAndIsEnable(Long resourceId, boolean isEnable);

}
