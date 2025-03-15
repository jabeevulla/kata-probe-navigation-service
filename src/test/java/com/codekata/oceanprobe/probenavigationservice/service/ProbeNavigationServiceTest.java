package com.codekata.oceanprobe.probenavigationservice.service;

import com.codekata.oceanprobe.probenavigationservice.dto.MoveProbeRequestDTO;
import com.codekata.oceanprobe.probenavigationservice.dto.MoveProbeResponseDTO;
import com.codekata.oceanprobe.probenavigationservice.entity.OceanFloor;
import com.codekata.oceanprobe.probenavigationservice.entity.Probe;
import com.codekata.oceanprobe.probenavigationservice.exception.DataNotFoundException;
import com.codekata.oceanprobe.probenavigationservice.exception.ObstacleEncounteredException;
import com.codekata.oceanprobe.probenavigationservice.exception.InvalidMoveException;
import com.codekata.oceanprobe.probenavigationservice.service.ProbeNavigationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProbeNavigationServiceTest {

    @InjectMocks
    private ProbeNavigationService probeNavigationService;

    @Mock
    private ProbeService probeService;

    @Mock
    private OceanFloorService oceanFloorService;

    @Mock
    private NavigationTrailService navigationTrailService;

    private UUID probeId;
    private Probe probe;
    private OceanFloor obstacle;

    @BeforeEach
    void setUp() {
        probeId = UUID.randomUUID();
        probe = new Probe(probeId, "Explorer-1", 2, 2, Probe.Direction.NORTH, LocalDateTime.now(), LocalDateTime.now());
        obstacle = new OceanFloor(1L, 2, 3, true);
    }

    @Test
    public void givenValidRequest_whenMoveProbe_thenReturnUpdatedPosition() {
        // Given: Valid move commands
        MoveProbeRequestDTO requestDTO = new MoveProbeRequestDTO(List.of("F", "L", "F"));

        // Mock repository calls
        when(probeService.getProbeById(probeId)).thenReturn(probe);
        when(oceanFloorService.isObstacle(anyInt(), anyInt())).thenReturn(false);
        when(navigationTrailService.saveMoveAsTrail(any())).thenReturn(any());

        // When: Moving probe
        MoveProbeResponseDTO responseDTO = probeNavigationService.moveProbe(probeId, requestDTO);

        // Then: Verify updated position
        assertEquals(1, responseDTO.getXPosition());
        assertEquals(3, responseDTO.getYPosition());
        assertEquals(Probe.Direction.WEST, responseDTO.getDirection());
        assertEquals("COMPLETED", responseDTO.getStatus());
        verify(navigationTrailService, times(2)).saveMoveAsTrail(any());
    }

    @Test
    public void givenNonExistentProbe_whenMoveProbe_thenThrowNotFoundException() {
        // Given: Probe does not exist
        MoveProbeRequestDTO requestDTO = new MoveProbeRequestDTO(List.of("F", "R"));

        // Mock repository to throw exception
        when(probeService.getProbeById(probeId)).thenThrow(new DataNotFoundException("Probe not found"));

        // Then: Expect an exception
        assertThrows(DataNotFoundException.class, () -> probeNavigationService.moveProbe(probeId, requestDTO));
    }

    @Test
    public void givenMoveBlockedByObstacle_whenMoveProbe_thenThrowObstacleEncounteredException() {
        // Given: Move that encounters an obstacle
        MoveProbeRequestDTO requestDTO = new MoveProbeRequestDTO(List.of("F"));

        // Mock repository responses
        when(probeService.getProbeById(probeId)).thenReturn(probe);
        when(oceanFloorService.isObstacle(2, 3)).thenReturn(true);

        // Then: Expect obstacle error
        assertThrows(ObstacleEncounteredException.class, () -> probeNavigationService.moveProbe(probeId, requestDTO));
    }

    @Test
    public void givenMoveOutOfBounds_whenMoveProbe_thenThrowInvalidMoveException() {
        // Given: Move out of grid bounds
        MoveProbeRequestDTO requestDTO = new MoveProbeRequestDTO(List.of("F"));

        // Mock repository responses
        probe.setXPosition(0);
        probe.setYPosition(0);
        probe.setDirection(Probe.Direction.SOUTH);
        when(probeService.getProbeById(probeId)).thenReturn(probe);
        when(oceanFloorService.isObstacle(anyInt(), anyInt())).thenReturn(false);

        // Then: Expect out-of-bounds error
        assertThrows(InvalidMoveException.class, () -> probeNavigationService.moveProbe(probeId, requestDTO));
    }
}
