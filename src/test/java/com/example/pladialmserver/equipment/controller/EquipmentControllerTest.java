package com.example.pladialmserver.equipment.controller;

import com.example.pladialmserver.global.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EquipmentControllerTest extends ControllerTestSupport {

    @DisplayName("비품 검색 조회")
    @Test
    @WithMockUser
    void searchEquipment() throws Exception {
        // given
        String cond = "비품";
        PageRequest pageable = PageRequest.of(0, 10);

        //when
        when(equipmentService.searchEquipment(cond, pageable)).thenReturn(new PageImpl<>(Collections.emptyList()));

        //when-then
        mockMvc.perform(get("/equipments")
                .param("cond", cond.toString())
                .param("pageable", pageable.toString())
                .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void updateEquipment() {
    }

    @Test
    void deleteEquipment() {
    }
}
