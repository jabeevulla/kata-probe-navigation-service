package com.codekata.oceanprobe.probenavigationservice.controller;

import com.codekata.oceanprobe.probenavigationservice.dto.MoveProbeRequestDTO;
import com.codekata.oceanprobe.probenavigationservice.dto.MoveProbeResponseDTO;
import com.codekata.oceanprobe.probenavigationservice.service.ProbeNavigationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/probes")
@Tag(name = "Probe Navigation API", description = "API to control probe movement on a grid")
public class ProbeNavigationController {
    private static final Logger logger = LoggerFactory.getLogger(ProbeNavigationController.class);
    @Autowired
    ProbeNavigationService probeNavigationService;

    @Operation(summary = "Move the probe", description = "Moves the probe based on a sequence of commands")
    @PostMapping("/{probeId}/move")
    public ResponseEntity<MoveProbeResponseDTO> moveProbe(
            @PathVariable UUID probeId,
            @RequestBody MoveProbeRequestDTO requestDTO) {
        MoveProbeResponseDTO responseDTO = probeNavigationService.moveProbe(probeId, requestDTO);
        logger.info("Prove Move Response");
        logger.info(responseDTO.toString());
        return ResponseEntity.ok(responseDTO);
    }
}