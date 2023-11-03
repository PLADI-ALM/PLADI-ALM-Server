package com.example.pladialmserver.booking.service;

import com.example.pladialmserver.booking.dto.response.AdminBookingRes;
import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.booking.dto.response.OfficeBookingDetailRes;
import com.example.pladialmserver.booking.entity.OfficeBooking;
import com.example.pladialmserver.booking.repository.officeBooking.OfficeBookingRepository;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.global.utils.EmailUtil;
import com.example.pladialmserver.notification.service.PushNotificationService;
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
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OfficeBookingService {
    private final OfficeBookingRepository officeBookingRepository;
    private final EmailUtil emailUtil;
    private final PushNotificationService notificationService;

    // 회의실 예약 목록 조회
    public Page<BookingRes> getOfficeBookings(User user, Pageable pageable) {
        return officeBookingRepository.getBookingsByUser(user, pageable);
    }

    // 회의실 예약 권한 확인
    private OfficeBooking checkOfficeBookingAuthentication(User user, Long officeBookingId, Role role) {
        OfficeBooking officeBooking = officeBookingRepository.findById(officeBookingId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.BOOKING_NOT_FOUND));
        checkRole(role, officeBooking.getUser(), user);
        return officeBooking;
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

    /**
     * 회의실 예약 개별 조회
     */
    public OfficeBookingDetailRes getOfficeBookingDetailByBasic(User user, Long officeBookingId) {
        OfficeBooking officeBooking = checkOfficeBookingAuthentication(user, officeBookingId, Role.BASIC);
        return OfficeBookingDetailRes.toDto(officeBooking);
    }


    /**
     * 회의실 예약 취소
     */
    @Transactional
    public void cancelBookingOffice(User user, Long officeBookingId) {
        OfficeBooking officeBooking = officeBookingRepository.findById(officeBookingId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.BOOKING_NOT_FOUND));

        // 사용자가 예약한 경우가 아니면
        if (!officeBooking.getUser().equals(user)) throw new BaseException(BaseResponseCode.NO_AUTHENTICATION);
        // 이미 취소된 예약이면
        if (officeBooking.getStatus().equals(BookingStatus.CANCELED))
            throw new BaseException(BaseResponseCode.ALREADY_CANCELED_BOOKING);
        // 취소하려는 예약이 이미 사용이 완료된 경우
        if (officeBooking.getStatus().equals(BookingStatus.FINISHED))
            throw new BaseException(BaseResponseCode.ALREADY_FINISHED_BOOKING);

        // 예약 취소
        officeBooking.cancelBookingOffice();
        officeBookingRepository.save(officeBooking);
        // 회의실 예약 취소 알림
        try {
            notificationService.sendCancelBookingNotification(officeBooking, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 회의실 예약 상태 변경 스케줄링
     */
    @Transactional
    @Scheduled(cron = "0 0 0/1 * * *", zone = "GMT+9:00") // 매시간 정각에 스케줄링
    public void checkBookingTime() {
        // 매시간 정각에 예약이 끝나는 회의실을 찾아서
        List<OfficeBooking> checkETList = officeBookingRepository.findByStatusAndDateAndEndTime(BookingStatus.USING);
        // FINISHED로 변경
        checkETList.forEach(OfficeBooking::finishBookingOffice);
        // 저장
        officeBookingRepository.saveAll(checkETList);

        // 매시간 정각에 예약이 시작되는 회의실을 찾아서
        List<OfficeBooking> checkSTList = officeBookingRepository.findByStatusAndDateAndStartTime(BookingStatus.BOOKED);
        // USING 으로 변경
        checkSTList.forEach(OfficeBooking::usingBookingOffice);
        // 저장
        officeBookingRepository.saveAll(checkSTList);
    }

    /**
     * 관리자 회의실 예약 목록 조회
     */
    public Page<AdminBookingRes> getBookingOffices(User user, Pageable pageable) {
        // 권한 확인
        checkAdminRole(user);
        Pageable sortedByDateAndStartTimeAsc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Order.asc("date"), Sort.Order.asc("startTime")));

        Page<OfficeBooking> bookings = officeBookingRepository.findByStatusIn(
                Arrays.asList(BookingStatus.BOOKED, BookingStatus.USING),
                sortedByDateAndStartTimeAsc
        );

        return bookings.map(AdminBookingRes::toDto);
    }

    /**
     * 관리자 회의실 예약 개별 조회
     */
    public OfficeBookingDetailRes getOfficeBookingDetailByAdmin(User user, Long officeBookingId) {
        OfficeBooking officeBooking = checkOfficeBookingAuthentication(user, officeBookingId, Role.ADMIN);
        return OfficeBookingDetailRes.toDto(officeBooking);
    }

    /**
     * 관리자 회의실 예약 반려
     */
    @Transactional
    public void cancelBookingOfficeByAdmin(User user, Long officeBookingId) {
        OfficeBooking officeBooking = officeBookingRepository.findById(officeBookingId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.BOOKING_NOT_FOUND));
        //관리자인지 확인
        checkAdminRole(user);
        // 이미 취소된 예약이면
        if (officeBooking.getStatus().equals(BookingStatus.CANCELED))
            throw new BaseException(BaseResponseCode.ALREADY_CANCELED_BOOKING);
        // 취소하려는 예약이 이미 사용이 완료된 경우
        if (officeBooking.getStatus().equals(BookingStatus.FINISHED))
            throw new BaseException(BaseResponseCode.ALREADY_FINISHED_BOOKING);

        // 예약 취소
        officeBooking.cancelBookingOffice();
        officeBookingRepository.save(officeBooking);
        // 회의실 예약 반려 알림
        try {
            notificationService.sendCancelBookingNotification(officeBooking, user);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
