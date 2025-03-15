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
     * @param navigationTrail
     * @return
     */
    public NavigationTrail saveMoveAsTrail(NavigationTrail navigationTrail) {
        // Fetch probe from repository or throw exception if not found
        Probe probe = probeRepository.findById(navigationTrail.getProbe().getId())
                .orElseThrow(() -> new DataNotFoundException("Probe not found with ID: " + navigationTrail.getProbe().getId()));

        // Create navigation trail entry
        NavigationTrail trail = new NavigationTrail();
        trail.setProbe(probe);
        trail.setXPosition(navigationTrail.getProbe().getXPosition());
        trail.setYPosition(navigationTrail.getProbe().getYPosition());
        trail.setDirection(navigationTrail.getProbe().getDirection());
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
