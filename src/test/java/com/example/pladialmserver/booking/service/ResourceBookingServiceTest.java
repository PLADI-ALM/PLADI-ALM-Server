package com.example.pladialmserver.booking.service;

import com.example.pladialmserver.booking.dto.request.SendEmailReq;
import com.example.pladialmserver.booking.dto.response.ProductBookingDetailRes;
import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.booking.repository.resourceBooking.ResourceBookingRepository;
import com.example.pladialmserver.global.Constants;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.global.utils.DateTimeUtil;
import com.example.pladialmserver.global.utils.EmailUtil;
import com.example.pladialmserver.notification.service.PushNotificationService;
import com.example.pladialmserver.user.dto.TokenDto;
import com.example.pladialmserver.user.entity.Role;
import com.example.pladialmserver.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.example.pladialmserver.booking.service.model.TestResourceBookingInfo.setUpResourceBooking;
import static com.example.pladialmserver.global.Constants.EmailNotification.*;
import static com.example.pladialmserver.user.service.model.TestUserInfo.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceBookingServiceTest {

    @InjectMocks
    private ResourceBookingService resourceBookingService;

    @Mock
    private ResourceBookingRepository resourceBookingRepository;

    @Mock
    private EmailUtil emailUtil;

    @Mock
    private PushNotificationService notificationService;

    @Spy
    BCryptPasswordEncoder passwordEncoder;

    @Test
    void getProductBookings() {
    }

    @Test
    @DisplayName("[성공] 장비 예약 개별 조회")
    void getProductBookingDetail_SUCCESS() {
        // given
        User basicUser = setUpUser(1L, Role.BASIC, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        User adminUser = setUpUser(2L, Role.ADMIN, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        ResourceBooking resourceBooking = setUpResourceBooking(basicUser, adminUser, BookingStatus.WAITING);
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
    @DisplayName("[실패] 장비 예약 개별 조회 - 본인 장비 예약이 아닌 경우")
    void getProductBookingDetail_NO_AUTHENTICATION() {
        // given
        User basicUser = setUpUser(1L, Role.BASIC, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        User adminUser = setUpUser(2L, Role.ADMIN, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        User fakeUser = setUpUser(3L, Role.BASIC, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        ResourceBooking resourceBooking = setUpResourceBooking(basicUser, adminUser, BookingStatus.WAITING);
        // when
        doReturn(Optional.of(resourceBooking)).when(resourceBookingRepository).findById(resourceBooking.getResourceBookingId());
        BaseException exception = assertThrows(BaseException.class, () -> {
            resourceBookingService.getProductBookingDetail(fakeUser, resourceBooking.getResourceBookingId());
        });
        // then
        assertThat(exception.getBaseResponseCode()).isEqualTo(BaseResponseCode.NO_AUTHENTICATION);
    }

    @Test
    @DisplayName("[실패] 장비 예약 개별 조회 - 존재하지 않는 예약인 경우")
    void getProductBookingDetail_BOOKING_NOT_FOUND() {
        // given
        User basicUser = setUpUser(1L, Role.BASIC, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        User adminUser = setUpUser(2L, Role.ADMIN, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        User fakeUser = setUpUser(3L, Role.BASIC, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        ResourceBooking resourceBooking = setUpResourceBooking(basicUser, adminUser, BookingStatus.WAITING);
        // when
        BaseException exception = assertThrows(BaseException.class, () -> {
            resourceBookingService.getProductBookingDetail(fakeUser, resourceBooking.getResourceBookingId()+1);
        });
        // then
        assertThat(exception.getBaseResponseCode()).isEqualTo(BaseResponseCode.BOOKING_NOT_FOUND);
    }

    @Test
    @DisplayName("[성공] 장비 예약 취소")
    void cancelBookingProduct_SUCCESS() throws IOException {
        // given
        User basicUser = setUpUser(1L, Role.BASIC, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        User adminUser = setUpUser(2L, Role.ADMIN, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        ResourceBooking resourceBooking = setUpResourceBooking(basicUser, adminUser, BookingStatus.USING);

        // when
        doReturn(Optional.of(resourceBooking)).when(resourceBookingRepository).findById(resourceBooking.getResourceBookingId());
        String title = COMPANY_NAME + RESOURCE + SPACE + BOOKING_TEXT + BOOKING_CANCEL;
        Map<String, String> bookingData = new HashMap<>();
        doNothing().when(emailUtil).sendEmail(resourceBooking.getUser().getEmail(), title, bookingData, BOOKING_TEMPLATE);
        doNothing().when(notificationService).sendNotification(eq(Constants.NotificationCategory.EQUIPMENT), eq(Constants.Notification.BODY_CANCELED), any(User.class));

        resourceBookingService.cancelBookingProduct(basicUser, resourceBooking.getResourceBookingId());

        // then
        assertThat(resourceBooking.getStatus()).isEqualTo(BookingStatus.CANCELED);
    }

    @Test
    @DisplayName("[실패] 장비 예약 취소 - 이미 취소된 예약인 경우")
    void cancelBookingProduct_ALREADY_CANCELED_BOOKING() {
        // given
        User basicUser = setUpUser(1L, Role.BASIC, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        User adminUser = setUpUser(2L, Role.ADMIN, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        ResourceBooking resourceBooking = setUpResourceBooking(basicUser, adminUser, BookingStatus.CANCELED);

        // when
        doReturn(Optional.of(resourceBooking)).when(resourceBookingRepository).findById(resourceBooking.getResourceBookingId());

        BaseException exception = assertThrows(BaseException.class, () -> {
            resourceBookingService.cancelBookingProduct(basicUser, resourceBooking.getResourceBookingId());
        });
        // then
        assertThat(exception.getBaseResponseCode()).isEqualTo(BaseResponseCode.ALREADY_CANCELED_BOOKING);
    }

    @Test
    @DisplayName("[실패] 장비 예약 취소 - 이미 사용 완료된 예약인 경우")
    void cancelBookingProduct_ALREADY_FINISHED_BOOKING() {
        // given
        User basicUser = setUpUser(1L, Role.BASIC, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        User adminUser = setUpUser(2L, Role.ADMIN, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        ResourceBooking resourceBooking = setUpResourceBooking(basicUser, adminUser, BookingStatus.FINISHED);

        // when
        doReturn(Optional.of(resourceBooking)).when(resourceBookingRepository).findById(resourceBooking.getResourceBookingId());

        BaseException exception = assertThrows(BaseException.class, () -> {
            resourceBookingService.cancelBookingProduct(basicUser, resourceBooking.getResourceBookingId());
        });
        // then
        assertThat(exception.getBaseResponseCode()).isEqualTo(BaseResponseCode.ALREADY_FINISHED_BOOKING);
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

}