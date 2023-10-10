package com.example.pladialmserver.resource.service;


import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.booking.repository.resourceBooking.ResourceBookingRepository;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.global.utils.DateTimeUtil;
import com.example.pladialmserver.resource.dto.request.ResourceReq;
import com.example.pladialmserver.resource.dto.response.AdminResourceRes;
import com.example.pladialmserver.resource.dto.response.ResourceDetailRes;
import com.example.pladialmserver.resource.dto.response.ResourceRes;
import com.example.pladialmserver.resource.entity.Resource;
import com.example.pladialmserver.resource.repository.ResourceRepository;
import com.example.pladialmserver.user.entity.User;
import com.example.pladialmserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResourceService {
    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;
    private final ResourceBookingRepository resourceBookingRepository;



    /**
     * 전체 자원 목록 조회 and 예약 가능한 자원 목록 조회
     */
    public Page<ResourceRes> findAvailableResources(String resourceName, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<Resource> allResources;

        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new BaseException(BaseResponseCode.END_DATE_BEFORE_START_DATE);
        }
        if (resourceName != null && startDate != null && endDate != null) {
            List<Long> bookedResourceIds = resourceBookingRepository.findBookedResourceIdsByDateAndResourceName(startDate, endDate, resourceName);

            if (!bookedResourceIds.isEmpty()) {
                allResources = resourceRepository.findAllByResourceIdNotIn(bookedResourceIds,pageable);
            } else {
                allResources = resourceRepository.findByNameContaining(resourceName,pageable);
            }
        } else {
            allResources = resourceRepository.findAll(pageable);
        }

        return allResources.map(ResourceRes::toDto);
    }


    /**
     * 자원 개별 조회
     */
    public ResourceDetailRes getResourceDetail(Long resourceId) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.RESOURCE_NOT_FOUND));
        return ResourceDetailRes.toDto(resource);
    }


    /**
     * 자원 기간별 예약 현황 조회
     */
    public List<String> getResourceBookedDate(Long resourceId, String month) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.RESOURCE_NOT_FOUND));
        // 예약 현황 조회할 월
        LocalDate standardDate = DateTimeUtil.stringToFirstLocalDate(month);
        return resourceBookingRepository.getResourceBookedDate(resource, standardDate);
    }



    /**
     * 자원 예약
     */
    @Transactional
    public void bookResource(Long userId, Long resourceId, ResourceReq resourceReq) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.USER_NOT_FOUND));
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.RESOURCE_NOT_FOUND));

        // 이미 예약된 날짜 여부 확인
        if(resourceBookingRepository.existsDate(resource, resourceReq.getStartDate(), resourceReq.getEndDate())) throw new BaseException(BaseResponseCode.ALREADY_BOOKED_TIME);;
        resourceBookingRepository.save(ResourceBooking.toDto(user, resource, resourceReq));

    }


}
