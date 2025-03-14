package com.codekata.oceanprobe.probenavigationservice.integration;

import com.codekata.oceanprobe.probenavigationservice.dto.RegisterProbeRequestDTO;
import com.codekata.oceanprobe.probenavigationservice.entity.Probe;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // Reset DB for each test
public class ProbeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenValidRequest_whenRegisterProbe_thenReturnCreated() throws Exception {
        // Given: A valid request DTO
        RegisterProbeRequestDTO requestDTO = new RegisterProbeRequestDTO();
        requestDTO.setName("Explorer-1");
        requestDTO.setXPosition(0);
        requestDTO.setYPosition(0);
        requestDTO.setDirection(Probe.Direction.NORTH);

        mockMvc.perform(post("/api/probes/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Explorer-1"))
                .andExpect(jsonPath("$.xPosition").value(0))
                .andExpect(jsonPath("$.yPosition").value(0))
                .andExpect(jsonPath("$.direction").value("NORTH"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }
}
