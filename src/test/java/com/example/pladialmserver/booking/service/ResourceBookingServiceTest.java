package com.example.pladialmserver.booking.service;

import com.example.pladialmserver.booking.dto.response.ProductBookingDetailRes;
import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.booking.repository.resourceBooking.ResourceBookingRepository;
import com.example.pladialmserver.global.utils.DateTimeUtil;
import com.example.pladialmserver.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static com.example.pladialmserver.booking.service.model.TestResourceBookingInfo.setUpResourceBooking;
import static com.example.pladialmserver.user.service.model.TestUserInfo.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ResourceBookingServiceTest {

    @InjectMocks
    private ResourceBookingService resourceBookingService;

    @Mock
    private ResourceBookingRepository resourceBookingRepository;

    @Spy
    BCryptPasswordEncoder passwordEncoder;

    @Test
    void getProductBookings() {
    }

    @Test
    @DisplayName("[성공] 장비 예약 개별 조회")
    void getProductBookingDetail() {
        // given
        User basicUser = setUpBasicUser(setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        User adminUser = setUpUser(setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        ResourceBooking resourceBooking = setUpResourceBooking(basicUser, adminUser);
        // when
        doReturn(Optional.of(resourceBooking)).when(resourceBookingRepository).findById(resourceBooking.getResourceBookingId());
        ProductBookingDetailRes res = resourceBookingService.getProductBookingDetail(basicUser, resourceBooking.getResourceBookingId());
        // then
        assertThat(res.getId()).isEqualTo(resourceBooking.getResource().getResourceId());
        assertThat(res.getStatus()).isEqualTo(resourceBooking.getStatus().getValue());
        assertThat(res.getReservatorName()).isEqualTo(resourceBooking.getUser().getName());
        assertThat(res.getReservatorPhone()).isEqualTo(resourceBooking.getUser().getPhone());
        assertThat(res.getStartDate()).isEqualTo(DateTimeUtil.dateTimeToString(resourceBooking.getStartDate()));
        assertThat(res.getEndDate()).isEqualTo(DateTimeUtil.dateTimeToString(resourceBooking.getEndDate()));
        assertThat(res.getReturnDateTime()).isEqualTo(DateTimeUtil.dateTimeToStringNullable(resourceBooking.getReturnDate()));
        assertThat(res.getMemo()).isEqualTo(resourceBooking.getMemo());
    }

    @Test
    void cancelBookingProduct() {
    }

    @Test
    void checkProductBookingTime() {
    }

    @Test
    void getProductBookingDetailByAdmin() {
    }

    @Test
    void rejectProductBooking() {
    }

    @Test
    void allowProductBooking() {
    }

    @Test
    void returnBookingProductByAdmin() {
    }

    @Test
    void getBookingProducts() {
    }

    @Test
    void returnBookingProduct() {
    }

//    @Test
//    @DisplayName("[성공] 관리자 권한 확인")
//    void checkRole() {
//        // given
//        User adminUser = setUpUser(setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
//
//        // when
//        resourceBookingService.check
//
//        // then
//    }
}