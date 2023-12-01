package com.example.pladialmserver.global.entityListener;

import com.example.pladialmserver.booking.repository.carBooking.CarBookingRepository;
import com.example.pladialmserver.booking.repository.officeBooking.OfficeBookingRepository;
import com.example.pladialmserver.booking.repository.resourceBooking.ResourceBookingRepository;
import com.example.pladialmserver.global.utils.BeanUtil;
import com.example.pladialmserver.user.entity.User;

import javax.persistence.PreRemove;

public class UserEntityListener {
    @PreRemove
    public void onUpdate(User user){
        // 회의실 예약 확인 및 예약 취소
        OfficeBookingRepository officeBookingRepository = BeanUtil.getBean(OfficeBookingRepository.class);
        officeBookingRepository.updateBookingStatusForResigning(user);
        // 자원 예약 확인 및 예약 취소
        ResourceBookingRepository resourceBookingRepository = BeanUtil.getBean(ResourceBookingRepository.class);
        resourceBookingRepository.updateBookingStatusForResigning(user);
        // 차량 예약 확인 및 예약 취소
        CarBookingRepository carBookingRepository = BeanUtil.getBean(CarBookingRepository.class);
        carBookingRepository.updateBookingStatusForResigning(user);
    }
}
