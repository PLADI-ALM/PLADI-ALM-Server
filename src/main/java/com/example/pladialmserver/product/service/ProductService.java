package com.example.pladialmserver.product.service;

import com.example.pladialmserver.product.dto.request.ProductReq;
import com.example.pladialmserver.product.dto.response.ProductBookingRes;
import com.example.pladialmserver.product.dto.response.ProductDetailRes;
import com.example.pladialmserver.product.resource.dto.response.AdminResourcesRes;
import com.example.pladialmserver.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ProductService {
    ProductDetailRes getProductDetail(Long carId);

    void bookProduct(User user, Long id, ProductReq request);

    ProductBookingRes getProductBookingByDate(Long carId, LocalDateTime dateTime);

    List<String> getProductBookedDate(Long carId, String month, LocalDate date);

    Page<AdminResourcesRes> getResourcesByAdmin(User user, String keyword, Pageable pageable);
}
