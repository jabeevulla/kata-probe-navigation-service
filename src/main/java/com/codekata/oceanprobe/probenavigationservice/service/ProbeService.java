package com.codekata.oceanprobe.probenavigationservice.service;

import com.codekata.oceanprobe.probenavigationservice.entity.Probe;
import com.codekata.oceanprobe.probenavigationservice.exception.DataNotFoundException;
import com.codekata.oceanprobe.probenavigationservice.repository.ProbeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProbeService {
    @Autowired
    ProbeRepository probeRepository;
    public Probe getProbeById(UUID probeId) {
       return probeRepository.findById(probeId)
                .orElseThrow(()->new DataNotFoundException("Probe not found with ID: " + probeId));
    }

    public Probe updateProbe(UUID probeId, Probe mockProbe) {
        return null;
    }
}
