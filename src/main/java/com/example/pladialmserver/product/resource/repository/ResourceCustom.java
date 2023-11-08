package com.example.pladialmserver.product.resource.repository;

import com.example.pladialmserver.product.resource.dto.response.AdminResourcesRes;
import com.example.pladialmserver.product.resource.dto.response.ResourceRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ResourceCustom {
    Page<AdminResourcesRes> search(String keyword, Pageable pageable);

    Page<ResourceRes> findAvailableResources(String resourceName, List<Long> bookedResourceIds, org.springframework.data.domain.Pageable pageable);
}
