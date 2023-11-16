package com.example.pladialmserver.product.car.repository;

import com.example.pladialmserver.product.car.dto.CarRes;
import com.example.pladialmserver.product.resource.dto.response.AdminResourcesRes;
import com.example.pladialmserver.product.resource.dto.response.ResourceRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface CarCustom {

Page<CarRes> findAvailableCars(String carName, List<Long> bookedCarIds, org.springframework.data.domain.Pageable pageable);


    Page<AdminResourcesRes> search(String carname, Pageable pageable);
}
