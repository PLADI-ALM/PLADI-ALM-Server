package com.example.pladialmserver.car.service;

import com.example.pladialmserver.booking.repository.carBooking.CarBookingRepository;
import com.example.pladialmserver.car.dto.CarRes;
import com.example.pladialmserver.car.dto.response.CarDetailRes;
import com.example.pladialmserver.car.entity.Car;
import com.example.pladialmserver.car.repository.CarRepository;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final CarBookingRepository carBookingRepository;

    /**
     * 전체 차량 목록 조회 and 예약 가능한 차량 목록 조회
     */
    public Page<CarRes> findAvailableCars(String carName, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        Page<Car> allCars;

        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new BaseException(BaseResponseCode.END_DATE_BEFORE_START_DATE);
        }
        if (carName != null && startDate != null && endDate != null) {
            List<Long> bookedCarIds = carBookingRepository.findBookedCarIdsByDateAndCarName(startDate, endDate, carName);

            if (!bookedCarIds.isEmpty()) {
                allCars = carRepository.findByNameAndCarIdNotInAAndIsEnableTrueAAndIsActiveTrue(carName, bookedCarIds, pageable);
            } else {
                allCars = carRepository.findByNameContainingAAndIsEnableTrueAndIsActiveTrue(carName, pageable);
            }
        } else {
            allCars = carRepository.findAllByIsEnableTrueAndIsActiveTrue(pageable);
        }

        return allCars.map(CarRes::toDto);
    }

    public CarDetailRes getCarDetail(Long carId) {
        Car car = carRepository.findByCarIdAndIsEnableAndIsActive(carId, true, true)
                .orElseThrow(() -> new BaseException(BaseResponseCode.CAR_NOT_FOUND));
        return CarDetailRes.toDto(car);
    }


}
