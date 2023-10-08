package com.example.pladialmserver.booking.service;

import com.example.pladialmserver.booking.dto.response.AdminBookingRes;
import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.booking.dto.response.OfficeBookingDetailRes;
import com.example.pladialmserver.booking.dto.response.ResourceBookingDetailRes;
import com.example.pladialmserver.booking.entity.OfficeBooking;
import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.booking.repository.officeBooking.OfficeBookingRepository;
import com.example.pladialmserver.booking.repository.resourceBooking.ResourceBookingRepository;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.user.entity.User;
import com.example.pladialmserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingService {
    private final UserRepository userRepository;
    private final OfficeBookingRepository officeBookingRepository;
    private final ResourceBookingRepository resourceBookingRepository;

    /**
     * 예약 목록 조회
     */
    public Page<BookingRes> getBookings(Long userId, String category, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.USER_NOT_FOUND));

        // TODO : QueryDSL로 상수 없애기
        if(category.equals("office")) {
            return officeBookingRepository.getBookingsByUser(user, pageable);
        }
        else if(category.equals("resource")) {
            return resourceBookingRepository.getBookingsByUser(user, pageable);
        } else {
            throw new BaseException(BaseResponseCode.BAD_REQUEST);
        }
    }

    /**
     * 회의실 예약 개별 조회
     */
    public OfficeBookingDetailRes getOfficeBookingDetail(Long userId, Long officeBookingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.USER_NOT_FOUND));
        OfficeBooking officeBooking = officeBookingRepository.findById(officeBookingId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.BOOKING_NOT_FOUND));
        if(!officeBooking.getUser().equals(user)) throw new BaseException(BaseResponseCode.NO_AUTHENTICATION);

        return OfficeBookingDetailRes.toDto(officeBooking);
    }


    /**
     * 회의실 예약 취소
     */
    @Transactional
    public void cancelBookingOffice(Long officeBookingId) {
        // todo: 로그인 기능 생성 후 변경 예정
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new BaseException(BaseResponseCode.USER_NOT_FOUND));
        OfficeBooking officeBooking = officeBookingRepository.findById(officeBookingId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.BOOKING_NOT_FOUND));

        // 사용자가 예약한 경우가 아니면
        if(!officeBooking.getUser().equals(user)) throw new BaseException(BaseResponseCode.NO_AUTHENTICATION);
        // 이미 취소된 예약이면
        if(officeBooking.getStatus().equals(BookingStatus.CANCELED)) throw new BaseException(BaseResponseCode.ALREADY_CANCELED_BOOKING);
        // 취소하려는 예약이 이미 사용이 완료된 경우
        if(officeBooking.getStatus().equals(BookingStatus.FINISHED)) throw new BaseException(BaseResponseCode.ALREADY_FINISHED_BOOKING);

        // 예약 취소
        officeBooking.cancelBookingOffice();
        officeBookingRepository.save(officeBooking);
    }

    /**
     * 회의실 예약 상태 변경 스케줄링
     */
    @Transactional
    @Scheduled(cron="0 0 * * * *", zone="GMT+9:00") // 매시간 정각에 스케줄링
    public void checkBookingTime(){
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
     * 자원 예약 개별 조회
     */
    public ResourceBookingDetailRes getResourceBookingDetail(Long userId, Long resourceBookingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.USER_NOT_FOUND));
        ResourceBooking resourceBooking = resourceBookingRepository.findById(resourceBookingId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.BOOKING_NOT_FOUND));
        if(!resourceBooking.getUser().equals(user)) throw new BaseException(BaseResponseCode.NO_AUTHENTICATION);

        return ResourceBookingDetailRes.toDto(resourceBooking);
    }

    /**
     * 자원 예약 취소
     */
    @Transactional
    public void cancelBookingResource(Long resourceBookingId) {
        // todo: 로그인 기능 생성 후 변경 예정
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new BaseException(BaseResponseCode.USER_NOT_FOUND));
        ResourceBooking resourceBooking = resourceBookingRepository.findById(resourceBookingId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.BOOKING_NOT_FOUND));

        // 사용자가 예약한 경우가 아니면
        if(!resourceBooking.getUser().equals(user)) throw new BaseException(BaseResponseCode.NO_AUTHENTICATION);
        // 이미 취소된 예약이면
        if(resourceBooking.getStatus().equals(BookingStatus.CANCELED)) throw new BaseException(BaseResponseCode.ALREADY_CANCELED_BOOKING);
        // 취소하려는 예약이 이미 사용이 완료된 경우
        if(resourceBooking.getStatus().equals(BookingStatus.FINISHED)) throw new BaseException(BaseResponseCode.ALREADY_FINISHED_BOOKING);

        // 예약 취소
        resourceBooking.cancelBookingResource();
        resourceBookingRepository.save(resourceBooking);
    }

    @Transactional
    public void returnBookingResource(Long resourceBookingId) {
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new BaseException(BaseResponseCode.USER_NOT_FOUND));
        ResourceBooking resourceBooking = resourceBookingRepository.findById(resourceBookingId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.BOOKING_NOT_FOUND));

        // 사용자가 예약한 경우가 아니면
        if(!resourceBooking.getUser().equals(user)) throw new BaseException(BaseResponseCode.NO_AUTHENTICATION);
        // 사용중 아니라면 -> 사용중 상태에서만 반납이 가능함
        if(!resourceBooking.getStatus().equals(BookingStatus.USING)) throw new BaseException(BaseResponseCode.MUST_BE_IN_USE);

        // 예약 반납
        resourceBooking.returnBookingResource();
        resourceBookingRepository.save(resourceBooking);
    }

    /**
     * 관리자 회의실 예약 목록 조회
     */
    public Page<AdminBookingRes> getBookingOffices(Pageable pageable) {
        Pageable sortedByDateAndStartTimeAsc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Order.asc("date"), Sort.Order.asc("startTime")));

        Page<OfficeBooking>  bookings = officeBookingRepository.findByStatusIn(
                Arrays.asList(BookingStatus.BOOKED, BookingStatus.USING),
                sortedByDateAndStartTimeAsc
        );

        return bookings.map(AdminBookingRes::toDto);
    }

    /**
     * 관리자 자원 예약 반려
     */
    @Transactional
    public void rejectResourceBooking(Long userId, Long resourceBookingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.USER_NOT_FOUND));
        ResourceBooking resourceBooking = resourceBookingRepository.findById(resourceBookingId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.BOOKING_NOT_FOUND));

        // 유저-예약 연결 여부
        if(!resourceBooking.getUser().equals(user)) throw new BaseException(BaseResponseCode.NO_AUTHENTICATION);
        // 예약대기가 아닌 경우
        if(!resourceBooking.getStatus().equals(BookingStatus.WAITING)) throw new BaseException(BaseResponseCode.INVALID_REJECT_BOOKING_STATUS);

        // 예약 취소
        resourceBooking.cancelBookingResource();
    }
}
