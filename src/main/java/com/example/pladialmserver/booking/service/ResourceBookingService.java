package com.example.pladialmserver.booking.service;

import com.example.pladialmserver.booking.dto.response.BookingRes;
import com.example.pladialmserver.booking.dto.response.ResourceBookingDetailRes;
import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.booking.repository.resourceBooking.ResourceBookingRepository;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.example.pladialmserver.global.Constants.Email.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResourceBookingService {
    private final ResourceBookingRepository resourceBookingRepository;
    private final EmailUtil emailUtil;


    // 장비 예약 목록 조회
    public Page<BookingRes> getResourceBookings(User user, Pageable pageable) {
        return resourceBookingRepository.getBookingsByUser(user, pageable);
    }

    // 장비 예약 권한 확인
    private ResourceBooking checkResourceBookingAuthentication(User user, Long resourceBookingId, Role role) {
        ResourceBooking resourceBooking = resourceBookingRepository.findById(resourceBookingId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.BOOKING_NOT_FOUND));
        checkRole(role, resourceBooking.getUser(), user);
        return resourceBooking;
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

    // 장비 예약 반납 공통 메서드
    private void returnBookingResource(ResourceBooking resourceBooking) {
        // 사용중 아니라면 -> 사용중 상태에서만 반납이 가능함
        if (!resourceBooking.checkBookingStatus(BookingStatus.USING))
            throw new BaseException(BaseResponseCode.MUST_BE_IN_USE);

        // 장비 반납
        resourceBooking.returnBookingResource();
        resourceBookingRepository.save(resourceBooking);
    }

    /**
     * 장비 예약 개별 조회
     */
    public ResourceBookingDetailRes getResourceBookingDetail(User user, Long resourceBookingId) {
        ResourceBooking resourceBooking = checkResourceBookingAuthentication(user, resourceBookingId, Role.BASIC);
        return ResourceBookingDetailRes.toDto(resourceBooking);
    }

    /**
     * 자원 예약 취소
     */
    @Transactional
    public void cancelBookingResource(User user, Long resourceBookingId) {
        ResourceBooking resourceBooking = checkResourceBookingAuthentication(user, resourceBookingId, Role.BASIC);

        // 이미 취소된 예약이면
        if (resourceBooking.checkBookingStatus(BookingStatus.CANCELED))
            throw new BaseException(BaseResponseCode.ALREADY_CANCELED_BOOKING);
        // 취소하려는 예약이 이미 사용이 완료된 경우
        if (resourceBooking.checkBookingStatus(BookingStatus.FINISHED))
            throw new BaseException(BaseResponseCode.ALREADY_FINISHED_BOOKING);

        // 예약 취소
        resourceBooking.changeBookingStatus(BookingStatus.CANCELED);
        resourceBookingRepository.save(resourceBooking);
    }

    /**
     * 자원 예약 반납
     */
    @Transactional
    public void returnBookingResourceByBasic(User user, Long resourceBookingId) {
        ResourceBooking resourceBooking = checkResourceBookingAuthentication(user, resourceBookingId, Role.BASIC);
        returnBookingResource(resourceBooking);
    }


    /**
     * 자원 예약 상태 변경 스케줄링
     */
    @Transactional
    @Scheduled(cron = "0 0 * * * *", zone = "GMT+9:00") // 날짜가 바뀔 때(0시)에 스케줄링
    public void checkResourceBookingTime() {
        // 오늘 날짜 + 예약중 인 것
        List<ResourceBooking> resourceBookingStartList = resourceBookingRepository.findByStartDateAndStatus(LocalDate.now(), BookingStatus.BOOKED);
        // USING 으로 변경
        resourceBookingStartList.forEach(ResourceBooking::startResourceBooking);
        // 저장
        resourceBookingRepository.saveAll(resourceBookingStartList);
    }

    /**
     * 관리자 장비 예약 개별 조회
     */
    public ResourceBookingDetailRes getResourceBookingDetailByAdmin(User user, Long resourceBookingId) {
        ResourceBooking resourceBooking = checkResourceBookingAuthentication(user, resourceBookingId, Role.ADMIN);
        return ResourceBookingDetailRes.toDto(resourceBooking);
    }

    /**
     * 관리자 장비 예약 반려
     */
    @Transactional
    public void rejectResourceBooking(User user, Long resourceBookingId) {
        ResourceBooking resourceBooking = checkResourceBookingAuthentication(user, resourceBookingId, Role.ADMIN);
        // '사용완료'또는'예약취소'인 경우 불가능
        if (resourceBooking.checkBookingStatus(BookingStatus.FINISHED) || resourceBooking.checkBookingStatus(BookingStatus.CANCELED))
            throw new BaseException(BaseResponseCode.INVALID_BOOKING_STATUS);
        // 예약 취소
        resourceBooking.changeBookingStatus(BookingStatus.CANCELED);

        // 이메일 전송
        String title = COMPANY_NAME + RESOURCE + SPACE + BOOKING_TEXT + BOOKING_REJECT;
        emailUtil.sendEmail(resourceBooking.getUser().getEmail(), title,
                emailUtil.createBookingData(resourceBooking.getUser(), REJECT_BOOKING_TEXT, resourceBooking.getResource().getName(), resourceBooking.getStartDate(), resourceBooking.getEndDate()), BOOKING_TEMPLATE);
    }

    /**
     * 관리자 장비 예약 허가
     */
    @Transactional
    public void allowResourceBooking(User user, Long resourceBookingId) {
        ResourceBooking resourceBooking = checkResourceBookingAuthentication(user, resourceBookingId, Role.ADMIN);
        // 예약대기가 아닌 경우
        if (!resourceBooking.checkBookingStatus(BookingStatus.WAITING))
            throw new BaseException(BaseResponseCode.INVALID_BOOKING_STATUS);
        // 이미 예약된 날짜 여부 확인
        // TODO 기획 변경으로 인한 수정
//        if(resourceBookingRepository.existsDate(resourceBooking.getResource(), resourceBooking.getStartDate(), resourceBooking.getEndDate())) throw new BaseException(BaseResponseCode.ALREADY_BOOKED_TIME);;

        // 예약 허가
        resourceBooking.changeBookingStatus(BookingStatus.BOOKED);

        // 이메일 전송
        String title = COMPANY_NAME + RESOURCE + SPACE + BOOKING_TEXT + BOOKING_APPROVE;
        emailUtil.sendEmail(resourceBooking.getUser().getEmail(), title,
                emailUtil.createBookingData(resourceBooking.getUser(), APPROVE_BOOKING_TEXT, resourceBooking.getResource().getName(), resourceBooking.getStartDate(), resourceBooking.getEndDate()), BOOKING_TEMPLATE);
    }

    /**
     * 관리자 장비 예약 반납
     */
    @Transactional
    public void returnBookingResourceByAdmin(User user, Long resourceBookingId) {
        ResourceBooking resourceBooking = checkResourceBookingAuthentication(user, resourceBookingId, Role.ADMIN);
        returnBookingResource(resourceBooking);

        // 이메일 전송
        String title = COMPANY_NAME + RESOURCE + SPACE + BOOKING_TEXT + BOOKING_RETURN;
        emailUtil.sendEmail(resourceBooking.getUser().getEmail(), title,
                emailUtil.createBookingData(resourceBooking.getUser(), RETURN_BOOKING_TEXT, resourceBooking.getResource().getName(), resourceBooking.getStartDate(), resourceBooking.getEndDate()), BOOKING_TEMPLATE);
    }

    /**
     * 관리자 장비 예약 목록을 조회
     */
    public Page<AdminResourceRes> getBookingResources(User user, Pageable pageable, boolean active) {
        checkAdminRole(user);

        Sort.Order order = active ? Sort.Order.asc("startDate") : Sort.Order.desc("startDate");

        Pageable sortedByDate = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(order));

        Page<ResourceBooking> resourceBookings = resourceBookingRepository.findByStatusIn(
                Arrays.asList(BookingStatus.BOOKED, BookingStatus.USING, BookingStatus.WAITING),
                sortedByDate
        );

        return resourceBookings.map(AdminResourceRes::toDto);
    }

}
