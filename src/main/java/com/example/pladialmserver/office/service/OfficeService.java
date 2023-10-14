package com.example.pladialmserver.office.service;

import com.example.pladialmserver.booking.entity.OfficeBooking;
import com.example.pladialmserver.booking.repository.officeBooking.OfficeBookingRepository;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.office.dto.request.OfficeReq;
import com.example.pladialmserver.office.dto.response.BookedTimeRes;
import com.example.pladialmserver.office.dto.response.BookingStateRes;
import com.example.pladialmserver.office.dto.response.OfficeRes;
import com.example.pladialmserver.office.entity.Facility;
import com.example.pladialmserver.office.entity.Office;
import com.example.pladialmserver.office.entity.OfficeFacility;
import com.example.pladialmserver.office.repository.OfficeRepository;
import com.example.pladialmserver.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OfficeService {
    private final OfficeRepository officeRepository;
    private final OfficeBookingRepository officeBookingRepository;


    /**
     * 전체 회의실 목록 조회 and 예약 가능한 회의실 목록 조회
     */
    public Page<OfficeRes> findAvailableOffices(LocalDate date, LocalTime startTime, LocalTime endTime, String facilityName,Pageable pageable) {

        Page<Office> allOffices;

        if (date != null && startTime != null && endTime != null) {
            // 입력된 날짜와 시간에 이미 예약된 회의실의 ID 목록을 조회
            List<Long> bookedOfficeIds = officeBookingRepository.findBookedOfficeIdsByDateAndTime(date, startTime, endTime);

            // 예약된 회의실을 제외한 회의실 목록을 페이징 처리하여 조회
            if (!bookedOfficeIds.isEmpty()) {
                if (facilityName != null && !facilityName.isEmpty()) {
                    // 시설 이름이 입력되었다면 해당 시설을 포함하는 회의실만 조회
                    allOffices = officeRepository.findByFacilityNameAndOfficeIdNotIn(facilityName, bookedOfficeIds, pageable);
                }else {
                    allOffices = officeRepository.findAllByOfficeIdNotIn(bookedOfficeIds, pageable);
                }
            } else {
                if (facilityName != null && !facilityName.isEmpty()) {
                    // 시설 이름이 입력되었다면 해당 시설을 포함하는 회의실만 조회
                    allOffices = officeRepository.findByFacilityName(facilityName, pageable);
                }else {
                    allOffices = officeRepository.findAll(pageable);
                }
            }
        }else{
            if (facilityName != null && !facilityName.isEmpty()) {
                // 시설 이름이 입력되었다면 해당 시설을 포함하는 회의실만 조회
                allOffices = officeRepository.findByFacilityName(facilityName, pageable);
            }else {
                allOffices = officeRepository.findAll(pageable);
            }
        }

        return allOffices.map(office -> {
            List<Facility> facilities = office.getFacilityList().stream()
                    .map(OfficeFacility::getFacility)
                    .collect(Collectors.toList());
            return OfficeRes.toDto(office, facilities);
        });
    }

    /**
     * 회의실 개별 조회
     */
    public OfficeRes getOffice(Long officeId) {
        Office office = officeRepository.findByOfficeId(officeId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.OFFICE_NOT_FOUND));

        List<Facility> facilities = office.getFacilityList().stream()
                .map(officeFacility -> officeFacility.getFacility())
                .collect(Collectors.toList());

        return OfficeRes.toDto(office, facilities);
    }

    /**
     * 회의실 일자별 예약 현황 조회
     */
    public BookingStateRes getOfficeBookedTimes(Long officeId, LocalDate date) {
        Office office = officeRepository.findById(officeId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.OFFICE_NOT_FOUND));

        List<OfficeBooking> bookings = officeBookingRepository.findByOfficeAndDateAndStatusNot(office, date, BookingStatus.CANCELED);

        return BookingStateRes.toDto(bookings.stream()
                .map(booking -> BookedTimeRes.toDto(booking.getStartTime(), booking.getEndTime()))
                .collect(Collectors.toList()));
    }

    /**
     * 회의실 예약하기
     */
    @Transactional
    public void bookOffice(User user, Long officeId, OfficeReq officeReq) {
        Office office = officeRepository.findByOfficeId(officeId)
                .orElseThrow(() -> new BaseException(BaseResponseCode.OFFICE_NOT_FOUND));

        // 이미 예약되어 있는 시간인지 확인
        if(officeBookingRepository.existsByDateAndTime(officeReq.getDate(), officeReq.getStartTime(), officeReq.getEndTime())) throw new BaseException(BaseResponseCode.ALREADY_BOOKED_TIME);
        officeBookingRepository.save(OfficeBooking.toDto(user, office, officeReq));
    }
}
