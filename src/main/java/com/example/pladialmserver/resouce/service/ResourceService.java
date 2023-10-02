package com.example.pladialmserver.resouce.service;

import com.example.pladialmserver.booking.repository.ResourceBookingRepository;
import com.example.pladialmserver.resouce.repository.ResourceRepository;
import com.example.pladialmserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ResourceService {
    private final UserRepository userRepository;
    private final ResourceRepository resourceRepository;
    private final ResourceBookingRepository resourceBookingRepository;

    /**
     * 자원 목록 조회
     */




    /**
     * 자원 개별 조회
     */




    /**
     * 자원 기간별 예약 현황 조회
     */




    /**
     * 자원 예약
     */

}
