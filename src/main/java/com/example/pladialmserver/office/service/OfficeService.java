package com.example.pladialmserver.office.service;

import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.office.dto.OfficeRes;
import com.example.pladialmserver.office.entity.Facility;
import com.example.pladialmserver.office.entity.Office;
import com.example.pladialmserver.office.entity.OfficeBooking;
import com.example.pladialmserver.office.repository.OfficeBookingRepository;
import com.example.pladialmserver.office.repository.OfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfficeService {

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
                    .map(officeFacility -> officeFacility.getFacility())
                    .collect(Collectors.toList());

            result.add(OfficeRes.toDto(office, facilities));
        }

        return result;
    }

    public OfficeRes getOffice(Long officeId) {
        Office office = officeRepository.findByOfficeId(officeId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.NOT_FOUND_OFFICE));

        List<Facility> facilities = office.getFacilityList().stream()
                .map(officeFacility -> officeFacility.getFacility())
                .collect(Collectors.toList());

        return OfficeRes.toDto(office, facilities);
    }

}
