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
    public Page<AdminBookingRes> getBookingOffices(String bookingStatus, Pageable pageable) {
        Page<OfficeBooking> bookings;

        if (bookingStatus == null) {
            bookings = officeBookingRepository.findByStatusIn(
                    Arrays.asList(BookingStatus.BOOKED, BookingStatus.USING),
                    pageable
            );
        }
        else {
            try {
                BookingStatus status = BookingStatus.valueOf(bookingStatus.toUpperCase());

                if (status != BookingStatus.BOOKED && status != BookingStatus.USING) {
                    throw new BaseException(BaseResponseCode.BAD_REQUEST);
                }
                bookings = officeBookingRepository.findByStatus(status, pageable);
            }
            catch (IllegalArgumentException e) {
                throw new BaseException(BaseResponseCode.BAD_REQUEST);
            }
        }

        return bookings.map(AdminBookingRes::toDto);
    }
}
