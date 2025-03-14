package com.codekata.oceanprobe.probenavigationservice.controller;

import com.codekata.oceanprobe.probenavigationservice.dto.RegisterProbeRequestDTO;
import com.codekata.oceanprobe.probenavigationservice.dto.RegisterProbeResponseDTO;
import com.codekata.oceanprobe.probenavigationservice.entity.Probe;
import com.codekata.oceanprobe.probenavigationservice.service.ProbeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/probes")
@Tag(name = "Probe Controller", description = "APIs for managing ocean probes")
public class ProbeController {

    @Autowired
    ProbeService probeService;

    @PostMapping("/register")
    @Operation(summary = "Register a new probe", description = "Creates a new probe with the specified name and initial position.")
    public ResponseEntity<RegisterProbeResponseDTO> registerProbe(@Valid @RequestBody RegisterProbeRequestDTO request) throws BadRequestException {
        Probe createdProbe = probeService.registerProbe(request);
        RegisterProbeResponseDTO responseDTO = new RegisterProbeResponseDTO(
                createdProbe.getId(),
                createdProbe.getName(),
                createdProbe.getXPosition(),
                createdProbe.getYPosition(),
                createdProbe.getDirection(),
                "ACTIVE"
        );
        return ResponseEntity.status(201).body(responseDTO);
    }
}
