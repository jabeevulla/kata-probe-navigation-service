package com.codekata.oceanprobe.probenavigationservice.service;

import com.codekata.oceanprobe.probenavigationservice.entity.Probe;
import com.codekata.oceanprobe.probenavigationservice.exception.DataNotFoundException;
import com.codekata.oceanprobe.probenavigationservice.repository.ProbeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProbeServiceTest {

    @Mock
    private ProbeRepository probeRepository;

    @InjectMocks
    private ProbeService probeService;

    private Probe mockProbe;
    private UUID probeId;

    @BeforeEach
    void setUp() {
        // Given: A mock probe with an ID
        probeId = UUID.randomUUID();
        mockProbe = new Probe();
        mockProbe.setId(probeId);
        mockProbe.setName("Explorer-1");
        mockProbe.setXPosition(2);
        mockProbe.setYPosition(3);
        mockProbe.setDirection(Probe.Direction.NORTH);
    }

    @Test
    public void givenExistingProbe_whenFetchById_thenReturnProbe() {
        // Given: Mock repository behavior
        when(probeRepository.findById(probeId)).thenReturn(Optional.of(mockProbe));

        // When: Fetching the probe
        Probe fetchedProbe = probeService.getProbeById(probeId);

        // Then: Verify the probe is returned correctly
        assertNotNull(fetchedProbe, "Probe should not be null");
        assertEquals(probeId, fetchedProbe.getId());
        assertEquals("Explorer-1", fetchedProbe.getName());
        assertEquals(2, fetchedProbe.getXPosition());
        assertEquals(3, fetchedProbe.getYPosition());
        assertEquals(Probe.Direction.NORTH, fetchedProbe.getDirection());

        // Verify repository method was called
        verify(probeRepository, times(1)).findById(probeId);
    }

    @Test
    public void givenNonExistentProbe_whenFetchById_thenThrowException() {
        // Given: Probe is not found
        when(probeRepository.findById(probeId)).thenReturn(Optional.empty());

        // When & Then: Expect exception
        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
            probeService.getProbeById(probeId);
        });

        assertEquals("Probe not found with ID: " + probeId, exception.getMessage());

        // Verify repository method was called
        verify(probeRepository, times(1)).findById(probeId);
    }
}
