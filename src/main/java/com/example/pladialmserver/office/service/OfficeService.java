package com.example.pladialmserver.office.service;

import com.example.pladialmserver.booking.entity.OfficeBooking;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.user.entity.User;
import com.example.pladialmserver.user.repository.UserRepository;
import com.example.pladialmserver.office.dto.request.OfficeReq;
import com.example.pladialmserver.office.dto.response.BookedTimeRes;
import com.example.pladialmserver.office.dto.response.OfficeRes;
import com.example.pladialmserver.office.entity.*;
import com.example.pladialmserver.booking.repository.OfficeBookingRepository;
import com.example.pladialmserver.office.repository.OfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OfficeService {
    private final UserRepository userRepository;
    private final OfficeRepository officeRepository;
    private final OfficeBookingRepository officeBookingRepository;

    public List<OfficeRes> findAvailableOffices(LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<Office> allOffices = officeRepository.findAll();

        if (date != null && startTime != null && endTime != null) {
            // 입력된 날짜와 시간에 이미 예약된 회의실을 조회
            List<OfficeBooking> bookedOffices = officeBookingRepository.findByDateAndTime(date, startTime, endTime);

            // 이미 예약된 회의실의 ID 목록을 추출
            List<Long> bookedOfficeIds = bookedOffices.stream()
                    .map(booking -> booking.getOffice().getOfficeId())
                    .collect(Collectors.toList());

            // 예약된 회의실을 제외한 회의실 목록을 필터링
            allOffices = allOffices.stream()
                    .filter(office -> !bookedOfficeIds.contains(office.getOfficeId()))
                    .collect(Collectors.toList());
        }

        // 반환할 결과 리스트를 생성
        List<OfficeRes> result = new ArrayList<>();

        for (Office office : allOffices) {
            List<Facility> facilities = office.getFacilityList().stream()
                    .map(OfficeFacility::getFacility)
                    .collect(Collectors.toList());

            List<String> imgUrls = office.getImgList().stream()
                    .map(OfficeImg::getImgUrl)
                    .collect(Collectors.toList());

            result.add(OfficeRes.toDto(office, facilities, imgUrls));
        }

        return result;
    }

    public OfficeRes getOffice(Long officeId) {
        Office office = officeRepository.findByOfficeId(officeId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.OFFICE_NOT_FOUND));

        List<Facility> facilities = office.getFacilityList().stream()
                .map(officeFacility -> officeFacility.getFacility())
                .collect(Collectors.toList());

        List<String> imgUrls = office.getImgList().stream()
                .map(OfficeImg::getImgUrl)
                .collect(Collectors.toList());

        return OfficeRes.toDto(office, facilities,imgUrls);
    }

    /**
     * 회의실 일자별 예약 현황 조회
     */
    public List<BookedTimeRes> getOfficeBookedTimes(Long officeId, LocalDate date) {
        Office office = officeRepository.findById(officeId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.OFFICE_NOT_FOUND));

        List<OfficeBooking> bookings = officeBookingRepository.findByOfficeAndDate(office, date);

        return bookings.stream()
                .map(booking -> BookedTimeRes.toDto(booking.getStartTime(), booking.getEndTime()))
                .collect(Collectors.toList());
    }

    /**
     * 회의실 예약하기
     */
    @Transactional
    public void bookOffice(Long officeId, OfficeReq officeReq) {
        // todo: user 로직 생성 후 변경 예정
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new BaseException(BaseResponseCode.USER_NOT_FOUND));
        Office office = officeRepository.findByOfficeId(officeId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.OFFICE_NOT_FOUND));

        // 이미 예약되어 있는 시간인지 확인
        if(officeBookingRepository.existsByDateAndTime(officeReq.getDate(), officeReq.getStartTime(), officeReq.getEndTime())) throw new BaseException(BaseResponseCode.ALREADY_BOOKED_TIME);
        officeBookingRepository.save(OfficeBooking.toDto(user, office, officeReq));
    }
}