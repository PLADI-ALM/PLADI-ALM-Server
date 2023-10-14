package com.example.pladialmserver.resource.repository;

import com.example.pladialmserver.resource.entity.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Page<Resource> findAllByResourceIdNotIn(List<Long> resourceIds, Pageable pageable);

    Page<Resource> findByNameContaining(String resourceName,Pageable pageable);
    Page<Resource> findByNameContainingOrderByName(String resourceName,Pageable pageable);

}
