package com.codekata.oceanprobe.probenavigationservice.controller;

import com.codekata.oceanprobe.probenavigationservice.dto.RegisterProbeRequestDTO;
import com.codekata.oceanprobe.probenavigationservice.entity.Probe;
import com.codekata.oceanprobe.probenavigationservice.service.ProbeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ProbeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProbeService probeService;

    @InjectMocks
    private ProbeController probeController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(probeController).build();
    }

    @Test
    public void givenInvalidRequest_whenRegisterProbe_thenReturnBadRequest() throws Exception {
        RegisterProbeRequestDTO requestDTO = new RegisterProbeRequestDTO();
        requestDTO.setName(""); // Invalid name

        mockMvc.perform(post("/api/probes/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenDuplicateProbeName_whenRegisterProbe_thenReturnConflict() throws Exception {
        RegisterProbeRequestDTO requestDTO = new RegisterProbeRequestDTO();
        requestDTO.setName("Explorer-1");
        requestDTO.setXPosition(0);
        requestDTO.setYPosition(0);
        requestDTO.setDirection(Probe.Direction.NORTH);

        Mockito.doThrow(new RuntimeException("Probe name already exists"))
                .when(probeService).saveOrUpdateProbe(any());

        mockMvc.perform(post("/api/probes/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Probe name already exists"));
    }
}
