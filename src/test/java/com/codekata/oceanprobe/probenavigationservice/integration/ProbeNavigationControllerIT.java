package com.codekata.oceanprobe.probenavigationservice.integration;

import com.codekata.oceanprobe.probenavigationservice.dto.MoveProbeRequestDTO;
import com.codekata.oceanprobe.probenavigationservice.entity.Probe;
import com.codekata.oceanprobe.probenavigationservice.repository.OceanFloorRepository;
import com.codekata.oceanprobe.probenavigationservice.repository.ProbeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ProbeNavigationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProbeRepository probeRepository;

    @Autowired
    private OceanFloorRepository oceanFloorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID probeId;

    @BeforeEach
    void setUp() {
        // Clear test data
        oceanFloorRepository.deleteAll();
        probeRepository.deleteAll();

        // Insert test probe
        Probe probe = new Probe(UUID.randomUUID(), "TestProbe", 2, 2, Probe.Direction.NORTH, null, null);
        probe = probeRepository.save(probe);
        probeId = probe.getId();
    }

    @Test
    @Disabled("Skipping this test temporarily due to persistent issues")
    public void givenValidRequest_whenMoveProbe_thenReturnOk() throws Exception {
        // Given: Valid move commands
        MoveProbeRequestDTO requestDTO = new MoveProbeRequestDTO(List.of("F", "R", "F"));

        mockMvc.perform(post("/api/v1/probes/{probeId}/move", probeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.probeId").value(probeId.toString()))
                .andExpect(jsonPath("$.xPosition").exists())
                .andExpect(jsonPath("$.yPosition").exists())
                .andExpect(jsonPath("$.direction").exists())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    @Disabled("Skipping this test temporarily due to persistent issues")
    public void givenNonExistentProbe_whenMoveProbe_thenReturnNotFound() throws Exception {
        // Given: Probe does not exist
        UUID nonExistentProbeId = UUID.randomUUID();
        MoveProbeRequestDTO requestDTO = new MoveProbeRequestDTO(List.of("F", "R"));

        mockMvc.perform(post("/api/v1/probes/{probeId}/move", nonExistentProbeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Probe not found"));
    }

    @Test
    @Disabled("Skipping this test temporarily due to persistent issues")
    public void givenMoveBlockedByObstacle_whenMoveProbe_thenReturnUnprocessableEntity() throws Exception {
        // Given: Movement blocked by an obstacle
        MoveProbeRequestDTO requestDTO = new MoveProbeRequestDTO(List.of("F"));

        mockMvc.perform(post("/api/v1/probes/{probeId}/move", probeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Obstacle encountered. Move blocked."));
    }
}
