package com.example.pladialmserver.product.resource.service;


import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.booking.repository.resourceBooking.ResourceBookingRepository;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.global.utils.DateTimeUtil;
import com.example.pladialmserver.product.dto.request.ProductReq;
import com.example.pladialmserver.product.dto.response.ProductBookingRes;
import com.example.pladialmserver.product.dto.response.ProductDetailRes;
import com.example.pladialmserver.product.resource.dto.request.CreateResourceReq;
import com.example.pladialmserver.product.resource.dto.response.AdminResourcesDetailsRes;
import com.example.pladialmserver.product.resource.dto.response.AdminResourcesRes;
import com.example.pladialmserver.product.resource.dto.response.ResourceRes;
import com.example.pladialmserver.product.resource.dto.response.ResourcesList;
import com.example.pladialmserver.product.resource.entity.Resource;
import com.example.pladialmserver.product.resource.repository.ResourceRepository;
import com.example.pladialmserver.product.service.ProductService;
import com.example.pladialmserver.user.entity.Role;
import com.example.pladialmserver.user.entity.User;
import com.example.pladialmserver.user.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResourceService implements ProductService {
    private final ResourceRepository resourceRepository;
    private final ResourceBookingRepository resourceBookingRepository;
    private final UserRepository userRepository;


    // 관리자 권한 확인
    private void checkAdminRole(User user) {
        if(!user.checkRole(Role.ADMIN)) throw new BaseException(BaseResponseCode.NO_AUTHENTICATION);
    }


    /**
     * 전체 장비 목록 조회 and 예약 가능한 장비 목록 조회
     */
    public Page<ResourceRes> findAvailableResources(String resourceName, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return resourceRepository.findAvailableResources(resourceName, startDate, endDate, pageable);
    }


    /**
     * 장비 개별 조회
     */
    @Override
    public ProductDetailRes getProductDetail(Long resourceId) {
        Resource resource = resourceRepository.findByResourceIdAndIsEnableAndIsActive(resourceId, true,true)
                .orElseThrow(() -> new BaseException(BaseResponseCode.RESOURCE_NOT_FOUND));
        return ProductDetailRes.toDto(resource);
    }


    /**
     * 장비 기간별 예약 현황 조회
     */
    public List<String> getResourceBookedDate(Long resourceId, String month, LocalDate date) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.RESOURCE_NOT_FOUND));
        // 예약 현황 조회할 월
        LocalDate standardDate = DateTimeUtil.stringToFirstLocalDate(month);
        return resourceBookingRepository.getResourceBookedDate(resource, standardDate, date);
    }


    /**
     * 장비 예약
     */
    @Override
    @Transactional
    public void bookProduct(User user, Long resourceId, ProductReq productReq) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.RESOURCE_NOT_FOUND));

        // 이미 예약된 날짜 여부 확인
        if (resourceBookingRepository.existsDateTime(resource, productReq.getStartDateTime(), productReq.getEndDateTime()))
            throw new BaseException(BaseResponseCode.ALREADY_BOOKED_TIME);
        ;
        resourceBookingRepository.save(ResourceBooking.toDto(user, resource, productReq));
    }

    // ===================================================================================================================
    // [관리자]
    // ===================================================================================================================

    /**
     * 관리자 장비 목록 조회
     */
    public Page<AdminResourcesRes> getResourcesByAdmin(User user, String resourceName, Pageable pageable) {
        // 관리자 권한 확인
        checkAdminRole(user);
        // 장비 조회
        return resourceRepository.search(resourceName, pageable);
    }

    /**
     * 관리자 장비 추가
     */
    @Transactional
    public void createResourceByAdmin(User user, CreateResourceReq request) {
        // 관리자 권한 확인
        checkAdminRole(user);
        User responsibility = userRepository.findByUserIdAndIsEnable(request.getResponsibility(), true)
                .orElseThrow(() -> new BaseException(BaseResponseCode.USER_NOT_FOUND));
        resourceRepository.save(Resource.toDto(request, responsibility));
    }

    /**
     * 관리자 장비 수정
     */
    @Transactional
    public void updateResourceByAdmin(User user, Long resourceId, CreateResourceReq request) {
        // 관리자 권한 확인
        checkAdminRole(user);
        // 장비 유무 확인
        Resource resource = resourceRepository.findByResourceIdAndIsEnable(resourceId, true)
                .orElseThrow(() -> new BaseException(BaseResponseCode.RESOURCE_NOT_FOUND));
        User responsibility = userRepository.findByUserIdAndIsEnable(request.getResponsibility(), true)
                .orElseThrow(() -> new BaseException(BaseResponseCode.USER_NOT_FOUND));
        // 장비 수정
        resource.updateResource(request, responsibility);
    }

    /**
     * 관리자 장비 삭제
     */
    @Transactional
    public void deleteResourceByAdmin(User user, Long resourceId) {
        // 관리자 권한 확인
        checkAdminRole(user);
        // 장비 유무 확인
        Resource resource = resourceRepository.findByResourceIdAndIsEnable(resourceId,true)
                .orElseThrow(() -> new BaseException(BaseResponseCode.RESOURCE_NOT_FOUND));
        // 장비 예약 내역 상태 확인
        List<BookingStatus> bookingStatus = new ArrayList<>(Arrays.asList(BookingStatus.WAITING, BookingStatus.BOOKED, BookingStatus.USING));
        if(resourceBookingRepository.existsByResourceAndStatusIn(resource, bookingStatus)) throw new BaseException(BaseResponseCode.INVALID_STATUS_BY_RESOURCE_DELETION);
        // 장비 삭제
        resourceRepository.delete(resource);
    }

    public AdminResourcesDetailsRes getAdminResourcesDetails(User user, Long resourceId) {
        // 관리자 권한 확인
        checkAdminRole(user);

        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() ->new BaseException(BaseResponseCode.RESOURCE_NOT_FOUND));

        List<ResourceBooking> resourceBookings=resourceBookingRepository.findAllByResourceOrderByStartDateDesc(resource);

        List<ResourcesList> resourcesLists = resourceBookings.stream()
                .map(ResourcesList::toDto)
                .collect(Collectors.toList());

        return AdminResourcesDetailsRes.toDto(resource, resourcesLists);
    }

    // 관리자 장비 활성화/비활성화
    @Transactional
    public void activateResourceByAdmin(User user, Long resourceId) {
        // 관리자 권한 확인
        checkAdminRole(user);
        // 장비 유무 확인
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.RESOURCE_NOT_FOUND));
        resource.activateResource();
    }

    // 해당 날짜의 장비 예약 내역 조회
    @Override
    public List<ProductBookingRes> getProductBookingByDate(Long resourceId, LocalDate date) {
        // 장비 유무 확인
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.RESOURCE_NOT_FOUND));
        return resourceBookingRepository.findResourceBookingByDate(resource, date);
    }
}