package com.example.pladialmserver.resource.repository;

import com.example.pladialmserver.resource.entity.ResourceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResourceCategoryRepository extends JpaRepository<ResourceCategory, Long> {

    Optional<ResourceCategory> findByName(String category);
}