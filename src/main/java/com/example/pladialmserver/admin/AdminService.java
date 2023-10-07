package com.example.pladialmserver.admin;

import com.example.pladialmserver.booking.entity.OfficeBooking;
import com.example.pladialmserver.booking.repository.officeBooking.OfficeBookingRepository;
import com.example.pladialmserver.booking.repository.resourceBooking.ResourceBookingRepository;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final OfficeBookingRepository officeBookingRepository;
    private final ResourceBookingRepository resourceBookingRepository;

    /**
     * 관리자 회의실 예약 목록 조회
     */
    public Page<AdminBookingRes> getBookingOffices(Pageable pageable) {
        Page<OfficeBooking> bookings;

            bookings = officeBookingRepository.findByStatusIn(
                    Arrays.asList(BookingStatus.BOOKED, BookingStatus.USING),
                    pageable
            );

        return bookings.map(AdminBookingRes::toDto);
    }
}
