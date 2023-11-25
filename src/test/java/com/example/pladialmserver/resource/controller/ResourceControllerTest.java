package com.example.pladialmserver.resource.controller;

import com.example.pladialmserver.global.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ResourceControllerTest extends ControllerTestSupport {


    @DisplayName("장비 목록 조회 성공")
    @Test
    @WithMockUser
    void getResource_Success() throws Exception {
        // given
        LocalDateTime startDate = LocalDateTime.of(2023, 11, 30, 13, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 11, 30, 16, 0);
        String resourceName = "맥북 프로";
        Pageable pageable = PageRequest.of(0, 10);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedStartDate = startDate.format(formatter);
        String formattedEndDate = endDate.format(formatter);

        // mocking
        when(resourceService.findAvailableResources(resourceName, startDate, endDate, pageable))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        // when-then
        mockMvc.perform(get("/resources")
                        .param("resourceName", resourceName)
                        .param("startDate", formattedStartDate)
                        .param("endDate", formattedEndDate)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("장비 목록 조회 실패 case1 -> 시작 종료 날짜나 시간이 둘중 하나라도 입력되지 않은 경우")
    @Test
    @WithMockUser
    void getResource_whenDateOrTimeNotProvided_thenThrowsException() throws Exception {
        // given
        String resourceName = "맥북 프로";
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);//endDate 주입 x
        Pageable pageable = PageRequest.of(0, 10);

        // when-then
        mockMvc.perform(get("/resources")
                        .param("resourceName", resourceName)
                        .param("startDate", startDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @DisplayName("장비 목록 조회 실패 case2 -> 과거의 날짜와 시간을 예약으로 시도할 경우")
    @Test
    @WithMockUser
    void getResource_whenPastDateAndTime_thenThrowsException() throws Exception {
        // given
        LocalDateTime startDate = LocalDateTime.now().minusDays(1); // 과거 날짜 주입
        LocalDateTime endDate = startDate.plusHours(2);
        String resourceName = "맥북 프로";
        Pageable pageable = PageRequest.of(0, 10);

        // when-then
        mockMvc.perform(get("/resources")
                        .param("resourceName", resourceName)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("장비 목록 조회 실패 case3 -> 시작 시간이 종료 시간보다 빠른 경우")
    @Test
    @WithMockUser
    void getResource_whenEndTimeBeforeStartTime_thenThrowsException() throws Exception {
        // given
        LocalDateTime startDate = LocalDateTime.of(2023, 10, 22, 16, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 10, 22, 15, 0); // 끝나는 시간이 시작 시간보다 빠른 시간 주입
        String resourceName = "맥북 프로";
        Pageable pageable = PageRequest.of(0, 10);

        // when-then
        mockMvc.perform(get("/resources")
                        .param("resourceName", resourceName)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }





}
