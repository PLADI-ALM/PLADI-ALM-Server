package com.example.pladialmserver.office.service;

import com.example.pladialmserver.booking.repository.officeBooking.OfficeBookingRepository;
import com.example.pladialmserver.global.IntegrationTestSupport;
import com.example.pladialmserver.office.repository.FacilityRepository;
import com.example.pladialmserver.office.repository.OfficeFacilityRepository;
import com.example.pladialmserver.office.repository.OfficeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OfficeServiceTest extends IntegrationTestSupport {
    @Autowired private OfficeService officeService;
    @Autowired private OfficeRepository officeRepository;
    @Autowired private OfficeBookingRepository officeBookingRepository;
    @Autowired private FacilityRepository facilityRepository;
    @Autowired private OfficeFacilityRepository officeFacilityRepository;

    @AfterEach
    void tearDown(){
        officeRepository.deleteAllInBatch();
        officeBookingRepository.deleteAllInBatch();;
        facilityRepository.deleteAllInBatch();;
        officeFacilityRepository.deleteAllInBatch();;
    }

    @DisplayName("회의실 개별 조회")
    @Test
    void getOffice() throws Exception{
        //given

        //when

        //then
    }

}
