package com.example.pladialmserver.resource.repository;

import com.example.pladialmserver.resource.dto.response.AdminResourcesRes;
import com.example.pladialmserver.resource.dto.response.ResourceRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ResourceCustom {
    Page<AdminResourcesRes> search(String keyword, Pageable pageable);

    Page<ResourceRes> findAvailableResources(String resourceName, LocalDateTime startDate, LocalDateTime endDate, org.springframework.data.domain.Pageable pageable);
}
