package com.codekata.oceanprobe.probenavigationservice.controller;

import com.codekata.oceanprobe.probenavigationservice.dto.MoveProbeRequestDTO;
import com.codekata.oceanprobe.probenavigationservice.dto.MoveProbeResponseDTO;
import com.codekata.oceanprobe.probenavigationservice.entity.Probe;
import com.codekata.oceanprobe.probenavigationservice.exception.DataNotFoundException;
import com.codekata.oceanprobe.probenavigationservice.exception.GlobalExceptionHandler;
import com.codekata.oceanprobe.probenavigationservice.exception.ObstacleEncounteredException;
import com.codekata.oceanprobe.probenavigationservice.service.ProbeNavigationService;
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

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)  // Use Mockito JUnit 5 extension
public class ProbeNavigationControllerTest {

    private MockMvc mockMvc;

    @Mock  // Replace @MockBean with @Mock
    private ProbeNavigationService probeNavigationService;

    @InjectMocks  // Inject the mock service into the controller
    private ProbeNavigationController probeNavigationController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(probeNavigationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void givenValidRequest_whenMoveProbe_thenReturnOk() throws Exception {
        // Given: A valid movement request
        UUID probeId = UUID.randomUUID();
        MoveProbeRequestDTO requestDTO = new MoveProbeRequestDTO(List.of("F", "R", "B", "L"));
        MoveProbeResponseDTO responseDTO = new MoveProbeResponseDTO(
                probeId, 2, 3, Probe.Direction.NORTH, List.of(), "COMPLETED"
        );

        // Mock service to return valid response
        Mockito.when(probeNavigationService.moveProbe(eq(probeId), any()))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/probes/{probeId}/move", probeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.probeId").value(probeId.toString()))
                .andExpect(jsonPath("$.direction").value("NORTH"))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    public void givenNonExistentProbe_whenMoveProbe_thenReturnNotFound() throws Exception {
        // Given: Probe does not exist
        UUID probeId = UUID.randomUUID();
        MoveProbeRequestDTO requestDTO = new MoveProbeRequestDTO(List.of("F", "R"));

        // Mock service to throw DataNotFoundException
        Mockito.when(probeNavigationService.moveProbe(eq(probeId), any()))
                .thenThrow(new DataNotFoundException("Probe not found"));

        mockMvc.perform(post("/api/v1/probes/{probeId}/move", probeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Probe not found"));
    }

    @Test
    public void givenMoveBlockedByObstacle_whenMoveProbe_thenReturnUnprocessableEntity() throws Exception {
        // Given: Movement blocked by obstacle
        UUID probeId = UUID.randomUUID();
        MoveProbeRequestDTO requestDTO = new MoveProbeRequestDTO(List.of("F"));

        // Mock service to throw ObstacleEncounteredException
        Mockito.when(probeNavigationService.moveProbe(eq(probeId), any()))
                .thenThrow(new ObstacleEncounteredException("Obstacle encountered. Move blocked."));

        mockMvc.perform(post("/api/v1/probes/{probeId}/move", probeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Obstacle encountered. Move blocked."));
    }
}
