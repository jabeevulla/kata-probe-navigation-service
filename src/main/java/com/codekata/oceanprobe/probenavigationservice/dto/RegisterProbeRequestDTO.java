package com.codekata.oceanprobe.probenavigationservice.dto;

import com.codekata.oceanprobe.probenavigationservice.entity.Probe;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterProbeRequestDTO {

    @Schema(description = "Name of the probe", example = "Explorer-1", required = true)
    @NotBlank(message = "Probe name is required")
    private String name;

    @Schema(description = "Initial X position on the grid", example = "0", required = true)
    @NotNull(message = "X position is required")
    private Integer xPosition;

    @Schema(description = "Initial Y position on the grid", example = "0", required = true)
    @NotNull(message = "Y position is required")
    private Integer yPosition;

    @Schema(description = "Initial facing direction (NORTH, SOUTH, EAST, WEST)", example = "NORTH", required = true)
    @NotNull(message = "Direction is required")
    private Probe.Direction direction;
}
