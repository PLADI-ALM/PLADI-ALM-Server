package com.example.pladialmserver.booking.service;

import com.example.pladialmserver.booking.dto.request.SendEmailReq;
import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.booking.dto.response.ProductBookingDetailRes;
import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.booking.repository.resourceBooking.ResourceBookingRepository;
import com.example.pladialmserver.global.Constants;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.global.utils.EmailUtil;
import com.example.pladialmserver.notification.service.PushNotificationService;
import com.example.pladialmserver.product.resource.dto.response.AdminProductRes;
import com.example.pladialmserver.user.entity.Role;
import com.example.pladialmserver.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.example.pladialmserver.global.Constants.EmailNotification.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResourceBookingService implements ProductBookingService {
    private final ResourceBookingRepository resourceBookingRepository;
    private final EmailUtil emailUtil;
    private final PushNotificationService notificationService;


    // 장비 예약 목록 조회
    @Override
    public Page<BookingRes> getProductBookings(User user, Pageable pageable) {
        return resourceBookingRepository.getBookingsByUser(user, pageable);
    }

    // 장비 예약 권한 확인
    private ResourceBooking checkResourceBookingAuthentication(User user, Long resourceBookingId, Role role) {
        ResourceBooking resourceBooking = resourceBookingRepository.findById(resourceBookingId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.BOOKING_NOT_FOUND));
        checkRole(role, resourceBooking.getUser(), user);
        return resourceBooking;
    }

    // 권한 확인
    private static void checkRole(Role role, User user, User target) {
        switch (role) {
            case BASIC:
                if (!user.equals(target)) throw new BaseException(BaseResponseCode.NO_AUTHENTICATION);
                break;
            case ADMIN:
                checkAdminRole(target);
                break;
        }
    }

    // 관리자 권환 확인
    private static void checkAdminRole(User user) {
        if (!user.checkRole(Role.ADMIN)) throw new BaseException(BaseResponseCode.NO_AUTHENTICATION);
    }

    // 장비 예약 반납 공통 메서드
    private void returnBookingResource(ResourceBooking resourceBooking) {
        // 사용중 아니라면 -> 사용중 상태에서만 반납이 가능함
        if (!resourceBooking.checkBookingStatus(BookingStatus.USING))
            throw new BaseException(BaseResponseCode.MUST_BE_IN_USE);

        // 장비 반납
        resourceBooking.returnBookingResource();
        resourceBookingRepository.save(resourceBooking);
    }

    /**
     * 장비 예약 개별 조회
     */
    @Override
    public ProductBookingDetailRes getProductBookingDetail(User user, Long resourceBookingId) {
        ResourceBooking resourceBooking = checkResourceBookingAuthentication(user, resourceBookingId, Role.BASIC);
        return ProductBookingDetailRes.toDto(resourceBooking);
    }

    /**
     * 자원 예약 취소
     */
    @Override
    @Transactional
    public void cancelBookingProduct(User user, Long resourceBookingId) {
        ResourceBooking resourceBooking = checkResourceBookingAuthentication(user, resourceBookingId, Role.BASIC);

        // 이미 취소된 예약이면
        if (resourceBooking.checkBookingStatus(BookingStatus.CANCELED))
            throw new BaseException(BaseResponseCode.ALREADY_CANCELED_BOOKING);
        // 취소하려는 예약이 이미 사용이 완료된 경우
        if (resourceBooking.checkBookingStatus(BookingStatus.FINISHED))
            throw new BaseException(BaseResponseCode.ALREADY_FINISHED_BOOKING);

        // 예약 취소
        resourceBooking.changeBookingStatus(BookingStatus.CANCELED);
        resourceBookingRepository.save(resourceBooking);
        // 장비 예약 취소 알림
        try {
            notificationService.sendNotification(Constants.NotificationCategory.EQUIPMENT, Constants.Notification.BODY_CANCELED, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 자원 예약 상태 변경 스케줄링
     */
    @Override
    @Transactional
    @Scheduled(cron = "0 0 * * * *", zone = "GMT+9:00") // 날짜가 바뀔 때(0시)에 스케줄링
    public void checkProductBookingTime() {
        // 오늘 날짜 + 예약중 인 것
        List<ResourceBooking> resourceBookingStartList = resourceBookingRepository.findByStartDateAndStatus(LocalDateTime.now(), BookingStatus.BOOKED);
        // USING 으로 변경
        resourceBookingStartList.forEach(ResourceBooking::startResourceBooking);
        // 저장
        resourceBookingRepository.saveAll(resourceBookingStartList);
    }

    /**
     * 관리자 장비 예약 개별 조회
     */
    @Override
    public ProductBookingDetailRes getProductBookingDetailByAdmin(User user, Long resourceBookingId) {
        ResourceBooking resourceBooking = checkResourceBookingAuthentication(user, resourceBookingId, Role.ADMIN);
        return ProductBookingDetailRes.toDto(resourceBooking);
    }

    /**
     * 관리자 장비 예약 반려
     */
    @Override
    @Transactional
    public void rejectProductBooking(User user, Long resourceBookingId) {
        ResourceBooking resourceBooking = checkResourceBookingAuthentication(user, resourceBookingId, Role.ADMIN);
        // '사용완료'또는'예약취소'인 경우 불가능
        if (resourceBooking.checkBookingStatus(BookingStatus.FINISHED) || resourceBooking.checkBookingStatus(BookingStatus.CANCELED))
            throw new BaseException(BaseResponseCode.INVALID_BOOKING_STATUS);
        // 예약 취소
        resourceBooking.changeBookingStatus(BookingStatus.CANCELED);

        // 이메일 전송
        String title = COMPANY_NAME + RESOURCE + SPACE + BOOKING_TEXT + BOOKING_REJECT;
        emailUtil.sendEmail(resourceBooking.getUser().getEmail(), title,
                emailUtil.createBookingData(SendEmailReq.toDto(resourceBooking, REJECT_BOOKING_TEXT)), BOOKING_TEMPLATE);

        // 장비 예약 반려 알림
        try {
            notificationService.sendNotification(Constants.NotificationCategory.EQUIPMENT, Constants.Notification.BODY_DENIED, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 관리자 장비 예약 허가
     */
    @Override
    @Transactional
    public void allowProductBooking(User user, Long resourceBookingId) {
        ResourceBooking resourceBooking = checkResourceBookingAuthentication(user, resourceBookingId, Role.ADMIN);
        // 예약대기가 아닌 경우
        if (!resourceBooking.checkBookingStatus(BookingStatus.WAITING))
            throw new BaseException(BaseResponseCode.INVALID_BOOKING_STATUS);
        // 이미 예약된 날짜 여부 확인
        if (resourceBookingRepository.existsDateTime(resourceBooking.getResource(), resourceBooking.getStartDate(), resourceBooking.getEndDate()))
            throw new BaseException(BaseResponseCode.ALREADY_BOOKED_TIME);
        // 예약 허가
        resourceBooking.changeBookingStatus(BookingStatus.BOOKED);
        // 이메일 전송
        String title = COMPANY_NAME + RESOURCE + SPACE + BOOKING_TEXT + BOOKING_APPROVE;
        emailUtil.sendEmail(resourceBooking.getUser().getEmail(), title,
                emailUtil.createBookingData(SendEmailReq.toDto(resourceBooking, APPROVE_BOOKING_TEXT)), BOOKING_TEMPLATE);
        // 장비 예약 알림
        try {
            notificationService.sendNotification(Constants.NotificationCategory.EQUIPMENT, Constants.Notification.BODY_SUCCESS, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 관리자 장비 예약 반납
     */
    @Override
    @Transactional
    public void returnBookingProductByAdmin(User user, Long resourceBookingId) {
        ResourceBooking resourceBooking = checkResourceBookingAuthentication(user, resourceBookingId, Role.ADMIN);
        returnBookingResource(resourceBooking);

        // 이메일 전송
        String title = COMPANY_NAME + RESOURCE + SPACE + BOOKING_TEXT + BOOKING_RETURN;
        emailUtil.sendEmail(resourceBooking.getUser().getEmail(), title,
                emailUtil.createBookingData(SendEmailReq.toDto(resourceBooking, RETURN_BOOKING_TEXT)), BOOKING_TEMPLATE);
        // 장비 반납 알림
        try {
            notificationService.sendNotification(Constants.NotificationCategory.EQUIPMENT, Constants.Notification.BODY_RETURNED, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 관리자 장비 예약 목록을 조회
     */
    @Override
    public Page<AdminProductRes> getBookingProducts(User user, Pageable pageable, boolean active) {
        checkAdminRole(user);

        Sort.Order order = active ? Sort.Order.asc("startDate") : Sort.Order.desc("startDate");

        Pageable sortedByDate = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(order));

        Page<ResourceBooking> resourceBookings = resourceBookingRepository.findByStatusIn(
                Arrays.asList(BookingStatus.BOOKED, BookingStatus.USING, BookingStatus.WAITING),
                sortedByDate
        );

        return resourceBookings.map(AdminProductRes::toDto);
    }

}
