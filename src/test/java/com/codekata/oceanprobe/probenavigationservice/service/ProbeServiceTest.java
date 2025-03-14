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

    @Test
    public void givenExistingProbe_whenSaveOrUpdate_thenUpdateProbe() {
        // Given: Probe exists in database
        Probe existingProbe = new Probe();
        existingProbe.setId(probeId);
        existingProbe.setName("Old Probe");
        existingProbe.setXPosition(5);
        existingProbe.setYPosition(1);
        existingProbe.setDirection(Probe.Direction.NORTH);

        when(probeRepository.findById(probeId)).thenReturn(Optional.of(existingProbe));
        when(probeRepository.save(any(Probe.class))).thenReturn(mockProbe);

        // When: Updating the probe
        Probe updatedProbe = probeService.saveOrUpdateProbe(probeId, mockProbe);

        // Then: Verify updated probe attributes
        assertNotNull(updatedProbe);
        assertEquals("Explorer-1", updatedProbe.getName());
        assertEquals(2, updatedProbe.getXPosition());
        assertEquals(3, updatedProbe.getYPosition());
        assertEquals(Probe.Direction.NORTH, updatedProbe.getDirection());

        // Verify repository method calls
        verify(probeRepository, times(1)).findById(probeId);
        verify(probeRepository, times(1)).save(any(Probe.class));
    }

    @Test
    public void givenNewProbe_whenSaveOrUpdate_thenCreateNewProbe() {
        // Given: Probe does not exist
        when(probeRepository.findById(probeId)).thenReturn(Optional.empty());
        when(probeRepository.save(any(Probe.class))).thenReturn(mockProbe);

        // When: Creating a new probe
        Probe newProbe = probeService.saveOrUpdateProbe(probeId, mockProbe);

        // Then: Verify the newly created probe
        assertNotNull(newProbe);
        assertEquals("Explorer-1", newProbe.getName());
        assertEquals(2, newProbe.getXPosition());
        assertEquals(3, newProbe.getYPosition());
        assertEquals(Probe.Direction.NORTH, newProbe.getDirection());

        // Verify repository method calls
        verify(probeRepository, times(1)).findById(probeId);
        verify(probeRepository, times(1)).save(any(Probe.class));
    }
}
