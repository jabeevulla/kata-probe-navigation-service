package com.codekata.oceanprobe.probenavigationservice.controller;

import com.codekata.oceanprobe.probenavigationservice.dto.RegisterProbeRequestDTO;
import com.codekata.oceanprobe.probenavigationservice.dto.RegisterProbeResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/probes")
@Tag(name = "Probe Controller", description = "APIs for managing ocean probes")
public class ProbeController {

    @PostMapping("/register")
    @Operation(summary = "Register a new probe", description = "Creates a new probe with the specified name and initial position.")
    public ResponseEntity<RegisterProbeResponseDTO> registerProbe(@Valid @RequestBody RegisterProbeRequestDTO request) {
        return null;
    }
}
