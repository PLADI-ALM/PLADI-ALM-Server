package com.example.pladialmserver.product.service;

import com.example.pladialmserver.product.dto.request.ProductReq;
import com.example.pladialmserver.product.dto.response.ProductBookingRes;
import com.example.pladialmserver.product.dto.response.ProductDetailRes;
import com.example.pladialmserver.product.dto.request.CreateProductReq;
import com.example.pladialmserver.product.dto.response.AdminProductDetailsRes;
import com.example.pladialmserver.product.dto.response.AdminProductsRes;
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

    List<String> getProductBookedDate(Long carId, String month);

    Page<AdminProductsRes> getProductByAdmin(User user, String keyword, Pageable pageable);

    List<String> getProductBookedTime(Long productId, LocalDate date);

    void createProductByAdmin(User user, CreateProductReq request);

    void updateProductByAdmin(User user, Long productId, CreateProductReq request);

    void deleteProductByAdmin(User user, Long productId);

    AdminProductDetailsRes getAdminProductsDetails(User user, Long productId);

    void activateProductByAdmin(User user, Long productId);

    List<ProductBookingRes> getProductBookedInfo(Long productId, LocalDate date);
}
