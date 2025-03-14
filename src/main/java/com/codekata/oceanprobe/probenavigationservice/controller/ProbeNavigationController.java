package com.codekata.oceanprobe.probenavigationservice.controller;

import com.codekata.oceanprobe.probenavigationservice.dto.MoveProbeRequestDTO;
import com.codekata.oceanprobe.probenavigationservice.dto.MoveProbeResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/probes")
@Tag(name = "Probe Navigation API", description = "API to control probe movement on a grid")
public class ProbeNavigationController {

    @Operation(summary = "Move the probe", description = "Moves the probe based on a sequence of commands")
    @PostMapping("/{probeId}/move")
    public ResponseEntity<MoveProbeResponseDTO> moveProbe(
            @PathVariable UUID probeId,
            @RequestBody MoveProbeRequestDTO requestDTO) {
        // TODO: Implement probe movement logic
        return ResponseEntity.ok().build();
    }
}