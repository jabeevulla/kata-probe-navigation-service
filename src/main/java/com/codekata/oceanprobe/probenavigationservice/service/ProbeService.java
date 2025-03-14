package com.codekata.oceanprobe.probenavigationservice.service;

import com.codekata.oceanprobe.probenavigationservice.dto.RegisterProbeRequestDTO;
import com.codekata.oceanprobe.probenavigationservice.entity.Probe;
import com.codekata.oceanprobe.probenavigationservice.exception.BadRequestException;
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
    private ProbeRepository probeRepository;

    /**
     * Find Probe by ID
     * @param probeId
     * @return
     */
    public Probe getProbeById(UUID probeId) {
       return probeRepository.findById(probeId)
                .orElseThrow(()->new DataNotFoundException("Probe not found with ID: " + probeId));
    }

    /**
     * Create new Probe or update existing Probe
     * @param probeData
     * @return
     */
    public Probe saveOrUpdateProbe(Probe probeData) {
        Optional<Probe> existingProbeOpt = probeRepository.findById(probeData.getId());

        Probe probe = existingProbeOpt.orElseGet(() -> {
            Probe newProbe = new Probe();
            newProbe.setId(UUID.randomUUID());
            return newProbe;
        });

        // Update properties for both new and existing probes
        probe.setName(probeData.getName());
        probe.setXPosition(probeData.getXPosition());
        probe.setYPosition(probeData.getYPosition());
        probe.setDirection(probeData.getDirection());
        return probeRepository.save(probe);
    }


    /**
     * Registers a new probe with the given details.
     *
     * @param request RegisterProbeRequestDTO containing probe details
     * @return Probe entity after saving
     * @throws BadRequestException if request fields are missing
     */
    public Probe registerProbe(RegisterProbeRequestDTO request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BadRequestException("Probe name is required");
        }
        if (request.getXPosition() == null || request.getYPosition() == null) {
            throw new BadRequestException("Probe coordinates (xPosition, yPosition) are required");
        }
        if (request.getDirection() == null) {
            throw new BadRequestException("Probe direction is required");
        }

        // Check if a probe with the same name already exists
        if (probeRepository.existsByName(request.getName())) {
            throw new BadRequestException("Probe name already exists: " + request.getName());
        }

        Probe probe = new Probe();
        probe.setId(UUID.randomUUID());
        probe.setName(request.getName());
        probe.setXPosition(request.getXPosition());
        probe.setYPosition(request.getYPosition());
        probe.setDirection(request.getDirection());

        return probeRepository.save(probe);
    }
}
