package com.example.pladialmserver.product.service;

import com.example.pladialmserver.product.dto.response.ProductDetailRes;

public interface ProductService {
    ProductDetailRes getProductDetail(Long carId);

}
