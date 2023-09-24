package com.example.pladialmserver.booking.service;

import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.booking.dto.response.OfficeBookingDetailRes;
import com.example.pladialmserver.booking.entity.OfficeBooking;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.user.entity.User;
import com.example.pladialmserver.user.repository.UserRepository;
import com.example.pladialmserver.booking.repository.OfficeBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingService {
    private final UserRepository userRepository;
    private final OfficeBookingRepository officeBookingRepository;

    /**
     * 예약 목록 조회
     */
    public Page<BookingRes> getBookings(Long userId, String category, Pageable pageable) {
        // TODO 비품 테이블명 정한 후, category 분리 로직 추가
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.USER_NOT_FOUND));
        return officeBookingRepository.getBookingsByUser(user, pageable);
    }

    /**
     * 회의실 예약 개별 조회
     */
    public OfficeBookingDetailRes getOfficeBookingDetail(Long officeBookingId) {
        OfficeBooking officeBooking = officeBookingRepository.findById(officeBookingId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.BOOKING_NOT_FOUND));

        return OfficeBookingDetailRes.toDto(officeBooking);
    }

}
