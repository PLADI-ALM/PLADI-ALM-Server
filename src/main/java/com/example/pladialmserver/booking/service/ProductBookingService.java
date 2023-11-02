package com.example.pladialmserver.booking.service;

import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.booking.dto.response.ResourceBookingDetailRes;
import com.example.pladialmserver.product.resource.dto.response.AdminResourceRes;
import com.example.pladialmserver.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductBookingService {
    Page<BookingRes> getProductBookings(User user, Pageable pageable);

    ResourceBookingDetailRes getProductBookingDetail(User user, Long productId);

    void cancelBookingProduct(User user, Long productId);

    void returnBookingProductByBasic(User user, Long productId);

    void checkProductBookingTime();

    ResourceBookingDetailRes getProductBookingDetailByAdmin(User user, Long productId);

    void rejectProductBooking(User user, Long productId);

    void allowProductBooking(User user, Long productId);

    void returnBookingProductByAdmin(User user, Long productId);

    Page<AdminResourceRes> getBookingProducts(User user, Pageable pageable, boolean active);
}
