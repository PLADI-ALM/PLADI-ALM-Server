package com.example.pladialmserver.product.resource.repository;

import com.example.pladialmserver.product.dto.response.AdminProductsRes;
import com.example.pladialmserver.product.resource.dto.ResourceRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ResourceCustom {
    Page<AdminProductsRes> search(String keyword, Pageable pageable);

    Page<ResourceRes> findAvailableResources(String resourceName, List<Long> bookedResourceIds, org.springframework.data.domain.Pageable pageable);
}
