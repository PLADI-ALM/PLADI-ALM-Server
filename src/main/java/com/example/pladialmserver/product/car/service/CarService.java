package com.example.pladialmserver.product.car.service;

import com.example.pladialmserver.booking.dto.request.SendEmailReq;
import com.example.pladialmserver.booking.entity.CarBooking;
import com.example.pladialmserver.booking.repository.carBooking.CarBookingRepository;
import com.example.pladialmserver.global.Constants;
import com.example.pladialmserver.global.entity.BookingStatus;
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
import com.example.pladialmserver.product.resource.dto.request.CreateProductReq;
import com.example.pladialmserver.product.resource.dto.response.AdminProductDetailsRes;
import com.example.pladialmserver.product.resource.dto.response.AdminProductsRes;
import com.example.pladialmserver.product.resource.dto.response.ProductList;
import com.example.pladialmserver.product.service.ProductService;
import com.example.pladialmserver.user.entity.Role;
import com.example.pladialmserver.user.entity.User;
import com.example.pladialmserver.user.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.pladialmserver.global.Constants.EmailNotification.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CarService implements ProductService {

    private final CarRepository carRepository;
    private final CarBookingRepository carBookingRepository;
    private final UserRepository userRepository;
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
        // 차량 예약 알림
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
    public List<String> getProductBookedDate(Long resourceId, String month) {
        Car car = carRepository.findById(resourceId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.CAR_NOT_FOUND));
        // 예약 현황 조회할 월
        LocalDate standardDate = DateTimeUtil.stringToFirstLocalDate(month);
        return carBookingRepository.getCarBookedDate(car, standardDate);
    }

    @Override
    public Page<AdminProductsRes> getProductByAdmin(User user, String carname, Pageable pageable) {
        // 관리자 권한 확인
        checkAdminRole(user);
        // 차량 조회
        return carRepository.search(carname, pageable);
    }

    @Override
    public List<String> getProductBookedTime(Long carId, LocalDate date) {
        // 차량 유무 확인
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.CAR_NOT_FOUND));
        return carBookingRepository.getBookedTime(car, date);
    }

    @Override
    @Transactional
    public void createProductByAdmin(User user, CreateProductReq request) {
        // 관리자 권한 확인
        checkAdminRole(user);
        User responsibility = userRepository.findByUserIdAndIsEnable(request.getResponsibility(), true)
                .orElseThrow(() -> new BaseException(BaseResponseCode.USER_NOT_FOUND));
        carRepository.save(Car.toDto(request, responsibility));
    }

    @Override
    @Transactional
    public void updateProductByAdmin(User user, Long carId, CreateProductReq request) {
        // 관리자 권한 확인
        checkAdminRole(user);
        // 차량 유무 확인
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.CAR_NOT_FOUND));
        User responsibility = userRepository.findByUserIdAndIsEnable(request.getResponsibility(), true)
                .orElseThrow(() -> new BaseException(BaseResponseCode.USER_NOT_FOUND));
        // 차량 수정
        car.updateCar(request, responsibility);
    }

    @Override
    @Transactional
    public void deleteProductByAdmin(User user, Long carId) {
        // 관리자 권한 확인
        checkAdminRole(user);
        // 차량 유무 확인
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.CAR_NOT_FOUND));
        // 차량 예약 내역 상태 확인
        List<BookingStatus> bookingStatus = new ArrayList<>(Arrays.asList(BookingStatus.WAITING, BookingStatus.BOOKED, BookingStatus.USING));
        if (carBookingRepository.existsByCarAndStatusIn(car, bookingStatus))
            throw new BaseException(BaseResponseCode.INVALID_STATUS_BY_RESOURCE_DELETION);
        // 차량 삭제
        carRepository.delete(car);
    }

    @Override
    public AdminProductDetailsRes getAdminProductsDetails(User user, Long carId) {
        // 관리자 권한 확인
        checkAdminRole(user);

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.CAR_NOT_FOUND));

        List<CarBooking> carBookingLists = carBookingRepository.findAllByCarOrderByStartDateDesc(car);

        List<ProductList> productLists = carBookingLists.stream()
                .map(ProductList::toDto)
                .collect(Collectors.toList());

        return AdminProductDetailsRes.toDto(car, productLists);
    }

    @Override
    @Transactional
    public void activateProductByAdmin(User user, Long carId) {
        // 차량 유무 확인
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.CAR_NOT_FOUND));

        //차량 책임자 확인
        checkCarOwner(user, car.getUser());
        car.activateResource();
    }

    // 관리자 권한 확인
    private void checkAdminRole(User user) {
        if(!user.checkRole(Role.ADMIN)) throw new BaseException(BaseResponseCode.NO_AUTHENTICATION);
    }

    // 차량 책임자 확인
    private void checkCarOwner(User currentUser, User carOwner) {
        if (!currentUser.equals(carOwner)) {
            throw new BaseException(BaseResponseCode.NO_AUTHENTICATION);
        }
    }
}
