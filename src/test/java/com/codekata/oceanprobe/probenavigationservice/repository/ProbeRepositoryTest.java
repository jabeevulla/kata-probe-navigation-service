package com.codekata.oceanprobe.probenavigationservice.repository;

import com.codekata.oceanprobe.probenavigationservice.entity.Probe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProbeRepositoryTest {

    @Autowired
    private ProbeRepository probeRepository;

    private Probe savedProbe;

    @BeforeEach
    void setUp() {
        // Given: A probe is created with an initial location
        Probe probe = new Probe();
        probe.setName("Explorer-1");
        probe.setXPosition(2);
        probe.setYPosition(3);
        probe.setDirection(Probe.Direction.NORTH);

        savedProbe = probeRepository.save(probe);
    }

    @Test
    public void givenNewProbe_whenSaved_thenProbeExistsInDatabase() {
        // When: Fetching the probe from the database
        Optional<Probe> foundProbe = probeRepository.findById(savedProbe.getId());

        // Then: Verify the probe exists and matches the saved data
        assertTrue(foundProbe.isPresent(), "Probe should exist in the database");
        assertEquals("Explorer-1", foundProbe.get().getName());
        assertEquals(2, foundProbe.get().getXPosition());
        assertEquals(3, foundProbe.get().getYPosition());
        assertEquals(Probe.Direction.NORTH, foundProbe.get().getDirection());
    }

    @Test
    public void givenExistingProbe_whenUpdated_thenProbeHasNewLocation() {
        // When: Updating probe's position
        savedProbe.setXPosition(4);
        savedProbe.setYPosition(1);
        savedProbe.setDirection(Probe.Direction.EAST);
        probeRepository.save(savedProbe);

        // Then: Fetch updated probe
        Optional<Probe> updatedProbe = probeRepository.findById(savedProbe.getId());
        assertTrue(updatedProbe.isPresent(), "Updated probe should exist");

        // Verify the new location and direction
        assertEquals(4, updatedProbe.get().getXPosition());
        assertEquals(1, updatedProbe.get().getYPosition());
        assertEquals(Probe.Direction.EAST, updatedProbe.get().getDirection());
    }
}
