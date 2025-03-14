package com.codekata.oceanprobe.probenavigationservice.dto;

import com.codekata.oceanprobe.probenavigationservice.entity.Probe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveProbeResponseDTO {
    private UUID probeId;
    private int xPosition;
    private int yPosition;
    private Probe.Direction direction;
    private List<NavigationTrailDTO> navigationTrail;
    private String status;
}
