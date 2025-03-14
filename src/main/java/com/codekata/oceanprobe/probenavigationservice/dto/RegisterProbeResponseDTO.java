package com.codekata.oceanprobe.probenavigationservice.dto;

import com.codekata.oceanprobe.probenavigationservice.entity.Probe;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class RegisterProbeResponseDTO {

    @Schema(description = "Unique identifier of the probe", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Name of the probe", example = "Explorer-1")
    private String name;

    @Schema(description = "Current X position", example = "0")
    private int xPosition;

    @Schema(description = "Current Y position", example = "0")
    private int yPosition;

    @Schema(description = "Current facing direction", example = "NORTH")
    private Probe.Direction direction;

    @Schema(description = "Status of the probe", example = "ACTIVE")
    private String status;
}