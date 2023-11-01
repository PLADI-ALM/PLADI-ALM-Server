package com.example.pladialmserver.product.service;

import com.example.pladialmserver.product.dto.request.ProductReq;
import com.example.pladialmserver.product.dto.response.ProductBookingRes;
import com.example.pladialmserver.product.dto.response.ProductDetailRes;
import com.example.pladialmserver.user.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface ProductService {
    ProductDetailRes getProductDetail(Long carId);

    void bookProduct(User user, Long id, ProductReq request);

    List<ProductBookingRes> getProductBookingByDate(Long carId, LocalDate date);

    List<String> getProductBookedDate(Long carId, String month, LocalDate date);
}
