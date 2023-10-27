package com.example.pladialmserver.resource.repository;

import com.example.pladialmserver.resource.dto.response.AdminResourcesRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResourceCustom {
    Page<AdminResourcesRes> search(String keyword, Pageable pageable);
}
