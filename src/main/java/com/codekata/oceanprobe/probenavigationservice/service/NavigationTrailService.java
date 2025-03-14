package com.codekata.oceanprobe.probenavigationservice.service;

import com.codekata.oceanprobe.probenavigationservice.entity.NavigationTrail;
import com.codekata.oceanprobe.probenavigationservice.entity.Probe;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NavigationTrailService {
    public NavigationTrail saveMoveAsTrail(UUID probeId, int i, int i1, Probe.Direction direction) {
        return null;
    }

    public List<NavigationTrail> getNavigationTrail(UUID probeId) {
        return null;
    }
}
