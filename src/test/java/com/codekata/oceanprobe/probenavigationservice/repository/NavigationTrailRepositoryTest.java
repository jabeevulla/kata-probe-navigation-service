package com.codekata.oceanprobe.probenavigationservice.repository;

import com.codekata.oceanprobe.probenavigationservice.entity.NavigationTrail;
import com.codekata.oceanprobe.probenavigationservice.entity.Probe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class NavigationTrailRepositoryTest {

    @Autowired
    private NavigationTrailRepository navigationTrailRepository;

    @Autowired
    private ProbeRepository probeRepository;

    private Probe probe;

    @BeforeEach
    void setUp() {
        // Given: A probe is created
        UUID probId = UUID.randomUUID();
        probe = new Probe();
        probe.setId(probId);
        probe.setName("Explorer-1");
        probe.setXPosition(0);
        probe.setYPosition(0);
        probe.setDirection(Probe.Direction.NORTH);

        probe = probeRepository.save(probe); // Save the probe first
    }

    @Test
    public void givenNavigationTrail_whenSaved_thenTrailExistsInDatabase() {
        // Given: A navigation trail entry
        NavigationTrail trail = new NavigationTrail();
        trail.setProbe(probe);
        trail.setXPosition(1);
        trail.setYPosition(1);
        trail.setDirection(Probe.Direction.EAST);
        trail.setMovedAt(LocalDateTime.now());

        // When: Saving the navigation trail
        NavigationTrail savedTrail = navigationTrailRepository.save(trail);

        // Then: Verify the trail entry exists
        assertNotNull(savedTrail.getId(), "Navigation Trail ID should be generated");
        assertEquals(probe.getId(), savedTrail.getProbe().getId(), "Probe ID should match");
        assertEquals(1, savedTrail.getXPosition(), "X position should be 1");
        assertEquals(1, savedTrail.getYPosition(), "Y position should be 1");
        assertEquals(Probe.Direction.EAST, savedTrail.getDirection(), "Direction should be EAST");
    }

    @Test
    public void givenMultipleNavigationTrails_whenSaved_thenFetchAllForProbe() {
        // Given: Multiple navigation trails for the same probe
        NavigationTrail trail1 = new NavigationTrail(probe.getId(), probe, 1, 1, Probe.Direction.EAST, LocalDateTime.now());
        NavigationTrail trail2 = new NavigationTrail(probe.getId(), probe, 2, 1, Probe.Direction.SOUTH, LocalDateTime.now().plusSeconds(10));

        navigationTrailRepository.save(trail1);
        navigationTrailRepository.save(trail2);

        // When: Fetching navigation trails for the probe
        List<NavigationTrail> trails = navigationTrailRepository.findByProbeId(probe.getId());

        // Then: Verify the correct number of entries
        assertEquals(2, trails.size(), "There should be 2 navigation trail entries for the probe");

        // Verify individual trail data
        assertEquals(1, trails.get(0).getXPosition());
        assertEquals(1, trails.get(0).getYPosition());
        assertEquals(Probe.Direction.EAST, trails.get(0).getDirection());

        assertEquals(2, trails.get(1).getXPosition());
        assertEquals(1, trails.get(1).getYPosition());
        assertEquals(Probe.Direction.SOUTH, trails.get(1).getDirection());
    }
}
