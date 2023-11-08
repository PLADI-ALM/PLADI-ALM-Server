package com.example.pladialmserver.product.car.repository;

import com.example.pladialmserver.product.car.dto.CarRes;
import com.example.pladialmserver.product.resource.dto.response.ResourceRes;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface CarCustom {

Page<CarRes> findAvailableCars(String carName, List<Long> bookedCarIds, org.springframework.data.domain.Pageable pageable);


}
