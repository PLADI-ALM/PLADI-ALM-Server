package com.example.pladialmserver.infra;

import com.example.pladialmserver.global.ControllerTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


public class HealthCheckControllerTest extends ControllerTestSupport {
//    @Test
//    @DisplayName("health-check")
//    void healthCheck() throws Exception{
//        String test="Server is Up";
//
//        mockMvc.perform(get("/health-check"))
//                .andExpect(status().isOk())
//                .andExpect(content().string(test));
//    }
}
