package com.example.pladialmserver.product.service;

import com.example.pladialmserver.product.dto.request.ProductReq;
import com.example.pladialmserver.product.dto.response.ProductDetailRes;
import com.example.pladialmserver.user.entity.User;

public interface ProductService {
    ProductDetailRes getProductDetail(Long carId);

    void bookProduct(User user, Long id, ProductReq request);
}
