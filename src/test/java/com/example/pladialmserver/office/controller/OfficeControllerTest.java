package com.example.pladialmserver.office.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import com.example.pladialmserver.global.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OfficeControllerTest extends ControllerTestSupport {

//    @DisplayName("회의실 목록 조회")
//    @Test
//    @WithMockUser
//    void searchOffice() throws Exception {
//        // given
//        LocalDate date = LocalDate.of(2023, 11, 30);
//        LocalTime startTime = LocalTime.of(12, 0);
//        LocalTime endTime = LocalTime.of(13, 0);
//        String facilityName = "빔 프로젝터";
//        Pageable pageable = PageRequest.of(0, 10);
//
//        // mocking
//        when(officeService.findAvailableOffices(date, startTime, endTime, facilityName, pageable))
//                .thenReturn(new PageImpl<>(Collections.emptyList()));
//
//        // when-then
//        mockMvc.perform(get("/offices")
//                        .param("date", date.toString())
//                        .param("startTime", startTime.toString())
//                        .param("endTime", endTime.toString())
//                        .param("facilityName", facilityName)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }

    @DisplayName("회의실 개별 조회")
    @Test
    void getOffice() throws Exception{
        //given

        //when

        //then

    }
}
