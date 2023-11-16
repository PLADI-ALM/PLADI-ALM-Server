package com.example.pladialmserver.booking.service;

import com.example.pladialmserver.booking.dto.request.ReturnProductReq;
import com.example.pladialmserver.booking.dto.request.SendEmailReq;
import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.booking.dto.response.ProductBookingDetailRes;
import com.example.pladialmserver.booking.entity.CarBooking;
import com.example.pladialmserver.booking.repository.carBooking.CarBookingRepository;
import com.example.pladialmserver.global.Constants;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.global.utils.EmailUtil;
import com.example.pladialmserver.notification.service.PushNotificationService;
import com.example.pladialmserver.product.car.entity.Car;
import com.example.pladialmserver.product.car.repository.CarRepository;
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
public class CarBookingService implements ProductBookingService{
    private final CarBookingRepository carBookingRepository;
    private final CarRepository carRepository;
    private final EmailUtil emailUtil;
    private final PushNotificationService notificationService;

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

    private CarBooking checkCarBookingAuthentication(User user, Long carBookingId, Role role) {
        CarBooking carBooking = carBookingRepository.findById(carBookingId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.BOOKING_NOT_FOUND));
        checkRole(role, carBooking.getUser(), user);
        return carBooking;
    }

    /**
     * 관리자 차량 예약 개별 조회
     */
    @Override
    public ProductBookingDetailRes getProductBookingDetailByAdmin(User user, Long carBookingId) {
        CarBooking carBooking = checkCarBookingAuthentication(user, carBookingId, Role.ADMIN);
        return ProductBookingDetailRes.toDto(carBooking);
    }

    /**
     * 관리자 차량 예약 반려
     */
    @Override
    @Transactional
    public void rejectProductBooking(User user, Long carBookingId) {
        CarBooking carBooking = checkCarBookingAuthentication(user, carBookingId, Role.ADMIN);
        // '사용완료'또는'예약취소'인 경우 불가능
        if (carBooking.checkBookingStatus(BookingStatus.FINISHED) || carBooking.checkBookingStatus(BookingStatus.CANCELED))
            throw new BaseException(BaseResponseCode.INVALID_BOOKING_STATUS);
        // 예약 취소
        carBooking.changeBookingStatus(BookingStatus.CANCELED);
        // 이메일 알림
        String title = COMPANY_NAME + CAR + SPACE + BOOKING_TEXT + BOOKING_REJECT;
        emailUtil.sendEmail(carBooking.getUser().getEmail(), title,
                emailUtil.createBookingData(SendEmailReq.toDto(carBooking, REJECT_BOOKING_TEXT)), BOOKING_TEMPLATE);
        // 차랑 예약 반려 알림
        try {
            notificationService.sendNotification(Constants.NotificationCategory.CAR, Constants.Notification.BODY_DENIED, user);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 관리자 차량 예약 반납
     */
    @Override
    @Transactional
    public void returnBookingProductByAdmin(User user, Long carBookingId, ReturnProductReq request) {
        CarBooking carBooking = checkCarBookingAuthentication(user, carBookingId, Role.ADMIN);
        returnBookingCar(carBooking, request);
        sendReturnNotification(carBooking.getUser(), carBooking);
    }

    /**
     * 관리자 차량 예약 목록을 조회
     */
    @Override
    public Page<AdminProductRes> getBookingProducts(User user, Pageable pageable, boolean active) {
        checkAdminRole(user);

        Sort.Order order = active ? Sort.Order.asc("startDate") : Sort.Order.desc("startDate");

        Pageable sortedByDate = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(order));

        Page<CarBooking> carBookings = carBookingRepository.findByStatusIn(
                Arrays.asList(BookingStatus.BOOKED, BookingStatus.USING, BookingStatus.WAITING),
                sortedByDate
        );

        return carBookings.map(AdminProductRes::toDto);
    }

    /**
     * 차량 예약 반납
     */
    @Override
    @Transactional
    public void returnBookingProduct(User user, Long carBookingId, ReturnProductReq request) {
        CarBooking carBooking = checkCarBookingAuthentication(user, carBookingId, Role.BASIC);
        returnBookingCar(carBooking, request);
        sendReturnNotification(carBooking.getCar().getUser(), carBooking);
    }

    private void sendReturnNotification(User user, CarBooking carBooking) {
        String title = COMPANY_NAME + CAR + SPACE + BOOKING_TEXT + BOOKING_RETURN;
        emailUtil.sendEmail(user.getEmail(), title,
                emailUtil.createBookingData(SendEmailReq.toDto(carBooking, RETURN_BOOKING_TEXT)), BOOKING_TEMPLATE);
        // 차랑 예약 반납 알림
        try {
            notificationService.sendNotification(Constants.NotificationCategory.CAR, Constants.Notification.BODY_RETURNED, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void returnBookingCar(CarBooking carBooking, ReturnProductReq request) {
        // 사용중 아니라면 -> 사용중 상태에서만 반납이 가능함
        if (!carBooking.checkBookingStatus(BookingStatus.USING))
            throw new BaseException(BaseResponseCode.MUST_BE_IN_USE);

        // 차량 반납
        carBooking.returnBookingCar(request.getRemark());
        carBookingRepository.save(carBooking);

        Car car = carBooking.getCar();
        car.setLocation(request.getReturnLocation());
        carRepository.save(car);
    }

    @Override
    public Page<BookingRes> getProductBookings(User user, Pageable pageable) {
        return carBookingRepository.getBookingsByUser(user, pageable);
    }

    @Override
    public ProductBookingDetailRes getProductBookingDetail(User user, Long carBookingId) {
        CarBooking carBooking = checkCarBookingAuthentication(user, carBookingId, Role.BASIC);
        return ProductBookingDetailRes.toDto(carBooking);
    }

    @Override
    @Transactional
    public void cancelBookingProduct(User user, Long carBookingId) {
        CarBooking carBooking = checkCarBookingAuthentication(user, carBookingId, Role.BASIC);

        // 이미 취소된 예약이면
        if (carBooking.checkBookingStatus(BookingStatus.CANCELED))
            throw new BaseException(BaseResponseCode.ALREADY_CANCELED_BOOKING);
        // 취소하려는 예약이 이미 사용이 완료된 경우
        if (carBooking.checkBookingStatus(BookingStatus.FINISHED))
            throw new BaseException(BaseResponseCode.ALREADY_FINISHED_BOOKING);

        // 예약 취소
        carBooking.changeBookingStatus(BookingStatus.CANCELED);
        carBookingRepository.save(carBooking);

        // 이메일 전송
        String title = COMPANY_NAME + CAR + SPACE + BOOKING_TEXT + BOOKING_CANCEL;
        emailUtil.sendEmail(carBooking.getUser().getEmail(), title,
                emailUtil.createBookingData(SendEmailReq.toDto(carBooking, CANCEL_BOOKING_TEXT)), BOOKING_TEMPLATE);

        // 차량 예약 취소 알림
        try {
            notificationService.sendNotification(Constants.NotificationCategory.CAR, Constants.Notification.BODY_CANCELED, user);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 * * * *", zone = "GMT+9:00") // 날짜가 바뀔 때(0시)에 스케줄링
    public void checkProductBookingTime() {
        // 오늘 날짜 + 예약중 인 것
        List<CarBooking> carBookingStartList = carBookingRepository.findByStartDateAndStatus(LocalDateTime.now(), BookingStatus.BOOKED);
        // USING 으로 변경
        carBookingStartList.forEach(CarBooking::startCarBooking);
        // 저장
        carBookingRepository.saveAll(carBookingStartList);
    }

}
