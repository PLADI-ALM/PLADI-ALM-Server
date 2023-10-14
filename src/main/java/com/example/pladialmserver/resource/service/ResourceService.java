package com.example.pladialmserver.resource.service;


import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.booking.repository.resourceBooking.ResourceBookingRepository;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.global.utils.DateTimeUtil;
import com.example.pladialmserver.resource.dto.request.CreateResourceReq;
import com.example.pladialmserver.resource.dto.request.ResourceReq;
import com.example.pladialmserver.resource.dto.response.AdminResourceCategoryRes;
import com.example.pladialmserver.resource.dto.response.AdminResourcesRes;
import com.example.pladialmserver.resource.dto.response.ResourceDetailRes;
import com.example.pladialmserver.resource.dto.response.ResourceRes;
import com.example.pladialmserver.resource.entity.Resource;
import com.example.pladialmserver.resource.entity.ResourceCategory;
import com.example.pladialmserver.resource.repository.ResourceCategoryRepository;
import com.example.pladialmserver.resource.repository.ResourceRepository;
import com.example.pladialmserver.user.entity.Role;
import com.example.pladialmserver.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResourceService {
    private final ResourceRepository resourceRepository;
    private final ResourceBookingRepository resourceBookingRepository;
    private final ResourceCategoryRepository resourceCategoryRepository;


    // 관리자 권한 확인
    private void checkAdminRole(User user) {
        if(!user.checkRole(Role.ADMIN)) throw new BaseException(BaseResponseCode.NO_AUTHENTICATION);
    }


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
    public void bookResource(User user, Long resourceId, ResourceReq resourceReq) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.RESOURCE_NOT_FOUND));

        // 이미 예약된 날짜 여부 확인
        if(resourceBookingRepository.existsDate(resource, resourceReq.getStartDate(), resourceReq.getEndDate())) throw new BaseException(BaseResponseCode.ALREADY_BOOKED_TIME);;
        resourceBookingRepository.save(ResourceBooking.toDto(user, resource, resourceReq));

    }

    // ===================================================================================================================
    // [관리자]
    // ===================================================================================================================

    /**
     * 자원 카테고리
     */
    public AdminResourceCategoryRes getResourceCategory(User user) {
        // 관리자 권한 확인
        checkAdminRole(user);

        List<ResourceCategory> resourceCategories = resourceCategoryRepository.findAll();
        return AdminResourceCategoryRes.toDto(resourceCategories);

    }

    /**
     * 관리자 자원 목록 조회
     */
    public Page<AdminResourcesRes> getResourcesByAdmin(User user, String keyword, Pageable pageable) {
        // 관리자 권한 확인
        checkAdminRole(user);
        // 자원 조회
        Page<Resource> resources = null;
        if(keyword == "" || keyword == null) {
            resources = resourceRepository.findAll(pageable);
        }else {
            resources = resourceRepository.findByNameContainingOrderByName(keyword, pageable);
        }
        return resources.map(AdminResourcesRes::toDto);
    }

    /**
     * 관리자 자원 추가
     */
    @Transactional
    public void createResourceByAdmin(User user, CreateResourceReq request) {
        // 관리자 권한 확인
        checkAdminRole(user);
        ResourceCategory category = resourceCategoryRepository.findByName(request.getCategory())
                .orElseThrow(() -> new BaseException(BaseResponseCode.RESOURCE_CATEGORY_NOT_FOUND));
        resourceRepository.save(Resource.toDto(request, category));
    }

    /**
     * 관리자 자원 수정
     */
    @Transactional
    public void updateResourceByAdmin(User user, Long resourceId, CreateResourceReq request) {
        // 관리자 권한 확인
        checkAdminRole(user);
        // 자원 유무 확인
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.RESOURCE_NOT_FOUND));
        ResourceCategory category = resourceCategoryRepository.findByName(request.getCategory())
                .orElseThrow(() -> new BaseException(BaseResponseCode.RESOURCE_CATEGORY_NOT_FOUND));
        // 자원 수정
        resource.updateResource(request, category);
    }
}
