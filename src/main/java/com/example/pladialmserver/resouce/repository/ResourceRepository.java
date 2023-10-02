package com.example.pladialmserver.resouce.repository;

import com.example.pladialmserver.resouce.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
}
