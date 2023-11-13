package com.example.pladialmserver.product.car.service;

import com.example.pladialmserver.booking.dto.request.SendEmailReq;
import com.example.pladialmserver.booking.entity.CarBooking;
import com.example.pladialmserver.booking.repository.carBooking.CarBookingRepository;
import com.example.pladialmserver.global.Constants;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.global.utils.DateTimeUtil;
import com.example.pladialmserver.global.utils.EmailUtil;
import com.example.pladialmserver.notification.service.PushNotificationService;
import com.example.pladialmserver.product.car.dto.CarRes;
import com.example.pladialmserver.product.car.entity.Car;
import com.example.pladialmserver.product.car.repository.CarRepository;
import com.example.pladialmserver.product.dto.request.ProductReq;
import com.example.pladialmserver.product.dto.response.ProductBookingRes;
import com.example.pladialmserver.product.dto.response.ProductDetailRes;
import com.example.pladialmserver.product.service.ProductService;
import com.example.pladialmserver.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.example.pladialmserver.global.Constants.EmailNotification.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CarService implements ProductService {

    private final CarRepository carRepository;
    private final CarBookingRepository carBookingRepository;
    private final EmailUtil emailUtil;
    private final PushNotificationService notificationService;

    /**
     * 전체 차량 목록 조회 and 예약 가능한 차량 목록 조회
     */
    public Page<CarRes> findAvailableCars(String carName, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        List<Long> bookedCarIds = Collections.emptyList();

        if (startDate != null && endDate != null) {
            bookedCarIds = carBookingRepository.findBookedCarIdsByDate(startDate, endDate);
        }

        return carRepository.findAvailableCars(carName,bookedCarIds,pageable);
    }


    @Override
    public ProductDetailRes getProductDetail(Long carId) {
        Car car = carRepository.findByCarIdAndIsEnable(carId, true)
                .orElseThrow(() -> new BaseException(BaseResponseCode.CAR_NOT_FOUND));
        return ProductDetailRes.toDto(car);
    }

    @Override
    @Transactional
    public void bookProduct(User user, Long resourceId, ProductReq productReq) {
        Car car = carRepository.findById(resourceId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.CAR_NOT_FOUND));

        // 이미 예약된 날짜 여부 확인
        if (carBookingRepository.existsDateTime(car, productReq.getStartDateTime(), productReq.getEndDateTime()))
            throw new BaseException(BaseResponseCode.ALREADY_BOOKED_TIME);
        CarBooking carBooking = carBookingRepository.save(CarBooking.toDto(user, car, productReq));

        // 이메일 전송
        String title = COMPANY_NAME + CAR + SPACE + BOOKING_TEXT + BOOKING_REQUEST;
        emailUtil.sendEmail(car.getUser().getEmail(), title,
                emailUtil.createBookingData(SendEmailReq.toDto(carBooking, NEW_BOOKING_TEXT)), BOOKING_TEMPLATE);
        // 장비 예약 알림
        try {
            notificationService.sendNotification(Constants.NotificationCategory.EQUIPMENT, Constants.Notification.BODY_SUCCESS, car.getUser());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public ProductBookingRes getProductBookingByDate(Long resourceId, LocalDateTime dateTime) {
        Car car = carRepository.findById(resourceId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.CAR_NOT_FOUND));
        return carBookingRepository.findCarBookingByDate(car, dateTime);
    }

    @Override
    public List<String> getProductBookedDate(Long resourceId, String month, LocalDate date) {
        Car car = carRepository.findById(resourceId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.CAR_NOT_FOUND));
        // 예약 현황 조회할 월
        LocalDate standardDate = DateTimeUtil.stringToFirstLocalDate(month);
        return carBookingRepository.getCarBookedDate(car, standardDate, date);
    }
}
