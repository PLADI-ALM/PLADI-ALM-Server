package com.example.pladialmserver.booking.service;

import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.booking.dto.response.ProductBookingDetailRes;
import com.example.pladialmserver.product.resource.dto.response.AdminProductRes;
import com.example.pladialmserver.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductBookingService {
    Page<BookingRes> getProductBookings(User user, Pageable pageable);

    ProductBookingDetailRes getProductBookingDetail(User user, Long productId);

    void cancelBookingProduct(User user, Long productId);

    void checkProductBookingTime();

    ProductBookingDetailRes getProductBookingDetailByAdmin(User user, Long productId);

    void rejectProductBooking(User user, Long productId);

    void returnBookingProductByAdmin(User user, Long productId);

    Page<AdminProductRes> getBookingProducts(User user, Pageable pageable, boolean active);
}
