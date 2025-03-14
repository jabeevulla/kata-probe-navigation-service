package com.codekata.oceanprobe.probenavigationservice.controller;

import com.codekata.oceanprobe.probenavigationservice.dto.RegisterProbeRequestDTO;
import com.codekata.oceanprobe.probenavigationservice.dto.RegisterProbeResponseDTO;
import com.codekata.oceanprobe.probenavigationservice.entity.Probe;
import com.codekata.oceanprobe.probenavigationservice.exception.BadRequestException;
import com.codekata.oceanprobe.probenavigationservice.exception.ConflictException;
import com.codekata.oceanprobe.probenavigationservice.exception.GlobalExceptionHandler;
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

import java.time.LocalDateTime;
import java.util.UUID;

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

    private UUID probeId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(probeController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        probeId = UUID.randomUUID();
    }

    @Test
    public void givenInvalidRequest_whenRegisterProbe_thenReturnBadRequest() throws Exception {
        RegisterProbeRequestDTO requestDTO = new RegisterProbeRequestDTO();
        requestDTO.setName(null);

        Mockito.doThrow(new BadRequestException("Probe name is required"))
                .when(probeService).registerProbe(any());

        mockMvc.perform(post("/api/probes/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Probe name is required"));
    }

    @Test
    public void givenDuplicateProbeName_whenRegisterProbe_thenReturnConflict() throws Exception {
        RegisterProbeRequestDTO requestDTO = new RegisterProbeRequestDTO();
        requestDTO.setName("Explorer-1");
        requestDTO.setXPosition(0);
        requestDTO.setYPosition(0);
        requestDTO.setDirection(Probe.Direction.NORTH);

        Mockito.doThrow(new ConflictException("Probe name already exists"))
                .when(probeService).registerProbe(any());

        mockMvc.perform(post("/api/probes/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Probe name already exists"));
    }

    @Test
    public void givenValidRequest_whenRegisterProbe_thenReturnCreated() throws Exception {
        // Given: A valid request DTO
        RegisterProbeRequestDTO requestDTO = new RegisterProbeRequestDTO();
        requestDTO.setName("Explorer-1");
        requestDTO.setXPosition(0);
        requestDTO.setYPosition(0);
        requestDTO.setDirection(Probe.Direction.NORTH);

        // Expected Response DTO
        RegisterProbeResponseDTO responseDTO = new RegisterProbeResponseDTO(
                probeId, "Explorer-1", 0, 0, Probe.Direction.NORTH, "ACTIVE"
        );

        // Mock service to return a valid response
        Mockito.when(probeService.registerProbe(any())).thenReturn(
                new Probe(probeId, "Explorer-1", 0, 0, Probe.Direction.NORTH, LocalDateTime.now(), LocalDateTime.now())
        );

        mockMvc.perform(post("/api/probes/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated()) // ✅ Expect HTTP 201 Created
                .andExpect(jsonPath("$.id").value(probeId.toString()))
                .andExpect(jsonPath("$.name").value("Explorer-1"))
                .andExpect(jsonPath("$.direction").value("NORTH"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }
}
