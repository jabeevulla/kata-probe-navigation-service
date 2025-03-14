package com.codekata.oceanprobe.probenavigationservice.service;

import com.codekata.oceanprobe.probenavigationservice.entity.NavigationTrail;
import com.codekata.oceanprobe.probenavigationservice.entity.Probe;
import com.codekata.oceanprobe.probenavigationservice.exception.DataNotFoundException;
import com.codekata.oceanprobe.probenavigationservice.repository.NavigationTrailRepository;
import com.codekata.oceanprobe.probenavigationservice.repository.ProbeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class NavigationTrailService {

    @Autowired
    private NavigationTrailRepository navigationTrailRepository;

    @Autowired
    private ProbeRepository probeRepository;

    /**
     * Saves a movement as a navigation trail entry.
     *
     * @param probeId UUID of the probe
     * @param x       New X position
     * @param y       New Y position
     * @param direction Direction after move
     * @return Saved NavigationTrail
     */
    public NavigationTrail saveMoveAsTrail(UUID probeId, int x, int y, Probe.Direction direction) {
        // Fetch probe from repository or throw exception if not found
        Probe probe = probeRepository.findById(probeId)
                .orElseThrow(() -> new DataNotFoundException("Probe not found with ID: " + probeId));

        // Create navigation trail entry
        NavigationTrail trail = new NavigationTrail();
        trail.setProbe(probe);
        trail.setXPosition(x);
        trail.setYPosition(y);
        trail.setDirection(direction);
        trail.setMovedAt(LocalDateTime.now());

        return navigationTrailRepository.save(trail);
    }

    /**
     * Fetches all navigation trails for a probe.
     *
     * @param probeId UUID of the probe
     * @return List of NavigationTrail
     */
    public List<NavigationTrail> getNavigationTrail(UUID probeId) {
        return navigationTrailRepository.findByProbeId(probeId);
    }
}
