package com.example.pladialmserver.product.car.repository;

import com.example.pladialmserver.product.car.dto.CarRes;
import com.example.pladialmserver.product.resource.dto.response.ResourceRes;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface CarCustom {

Page<CarRes> findAvailableCars(String carName, LocalDateTime startDate, LocalDateTime endDate, org.springframework.data.domain.Pageable pageable);


}
