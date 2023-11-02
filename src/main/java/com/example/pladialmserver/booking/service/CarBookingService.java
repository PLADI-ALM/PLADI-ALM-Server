package com.example.pladialmserver.booking.service;

import com.example.pladialmserver.booking.dto.response.ResourceBookingDetailRes;
import com.example.pladialmserver.booking.entity.CarBooking;
import com.example.pladialmserver.booking.repository.carBooking.CarBookingRepository;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.global.utils.EmailUtil;
import com.example.pladialmserver.product.resource.dto.response.AdminResourceRes;
import com.example.pladialmserver.user.entity.Role;
import com.example.pladialmserver.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CarBookingService {
    private final CarBookingRepository carBookingRepository;
    private final EmailUtil emailUtil;

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
    public ResourceBookingDetailRes getCarBookingDetailByAdmin(User user, Long carBookingId) {
        CarBooking carBooking = checkCarBookingAuthentication(user, carBookingId, Role.ADMIN);
        return ResourceBookingDetailRes.toDto(carBooking);
    }

    /**
     * 관리자 차량 예약 반려
     */
    @Transactional
    public void rejectCarBooking(User user, Long carBookingId) {
        CarBooking carBooking = checkCarBookingAuthentication(user, carBookingId, Role.ADMIN);
        // '사용완료'또는'예약취소'인 경우 불가능
        if (carBooking.checkBookingStatus(BookingStatus.FINISHED) || carBooking.checkBookingStatus(BookingStatus.CANCELED))
            throw new BaseException(BaseResponseCode.INVALID_BOOKING_STATUS);
        // 예약 취소
        carBooking.changeBookingStatus(BookingStatus.CANCELED);
    }

    /**
     * 관리자 차량 예약 허가
     */
    @Transactional
    public void allowCarBooking(User user, Long carBookingId) {
        CarBooking carBooking = checkCarBookingAuthentication(user, carBookingId, Role.ADMIN);
        // 예약대기가 아닌 경우
        if (!carBooking.checkBookingStatus(BookingStatus.WAITING))
            throw new BaseException(BaseResponseCode.INVALID_BOOKING_STATUS);
        // 이미 예약된 날짜 여부 확인
        // TODO 기획 변경으로 인한 수정
//        if(resourceBookingRepository.existsDate(resourceBooking.getResource(), resourceBooking.getStartDate(), resourceBooking.getEndDate())) throw new BaseException(BaseResponseCode.ALREADY_BOOKED_TIME);;

        // 예약 허가
        carBooking.changeBookingStatus(BookingStatus.BOOKED);
    }

    /**
     * 관리자 차량 예약 반납
     */
    @Transactional
    public void returnBookingCarByAdmin(User user, Long carBookingId) {
        CarBooking carBooking = checkCarBookingAuthentication(user, carBookingId, Role.ADMIN);
        returnBookingCar(carBooking);
    }

    /**
     * 관리자 차량 예약 목록을 조회
     */
    public Page<AdminResourceRes> getBookingCars(User user, Pageable pageable, boolean active) {
        checkAdminRole(user);

        Sort.Order order = active ? Sort.Order.asc("startDate") : Sort.Order.desc("startDate");

        Pageable sortedByDate = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(order));

        Page<CarBooking> carBookings = carBookingRepository.findByStatusIn(
                Arrays.asList(BookingStatus.BOOKED, BookingStatus.USING, BookingStatus.WAITING),
                sortedByDate
        );

        return carBookings.map(AdminResourceRes::toDto);
    }

    private void returnBookingCar(CarBooking carBooking) {
        // 사용중 아니라면 -> 사용중 상태에서만 반납이 가능함
        if (!carBooking.checkBookingStatus(BookingStatus.USING))
            throw new BaseException(BaseResponseCode.MUST_BE_IN_USE);

        // 차량 반납
        carBooking.returnBookingCar();
        carBookingRepository.save(carBooking);
    }

}
