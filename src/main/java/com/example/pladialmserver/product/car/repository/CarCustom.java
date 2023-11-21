package com.example.pladialmserver.product.car.repository;

import com.example.pladialmserver.product.car.dto.CarRes;
import com.example.pladialmserver.product.dto.response.AdminProductsRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CarCustom {

Page<CarRes> findAvailableCars(String carName, List<Long> bookedCarIds, org.springframework.data.domain.Pageable pageable);


    Page<AdminProductsRes> search(String carname, Pageable pageable);
}
