package com.codekata.oceanprobe.probenavigationservice.service;

import com.codekata.oceanprobe.probenavigationservice.entity.Probe;
import com.codekata.oceanprobe.probenavigationservice.exception.DataNotFoundException;
import com.codekata.oceanprobe.probenavigationservice.repository.ProbeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProbeService {
    private static final Logger logger = LoggerFactory.getLogger(ProbeService.class);

    @Autowired
    ProbeRepository probeRepository;
    public Probe getProbeById(UUID probeId) {
       return probeRepository.findById(probeId)
                .orElseThrow(()->new DataNotFoundException("Probe not found with ID: " + probeId));
    }

    public Probe saveOrUpdateProbe(UUID id, Probe probeData) {
        Optional<Probe> existingProbeOpt = probeRepository.findById(id);

        Probe probe = existingProbeOpt.orElseGet(() -> {
            Probe newProbe = new Probe();
            newProbe.setId(id); // Set the ID for the new probe
            return newProbe;
        });

        // Update properties for both new and existing probes
        probe.setName(probeData.getName());
        probe.setXPosition(probeData.getXPosition());
        probe.setYPosition(probeData.getYPosition());
        probe.setDirection(probeData.getDirection());

        return probeRepository.save(probe);
    }
}
