package com.codekata.oceanprobe.probenavigationservice.service;

import com.codekata.oceanprobe.probenavigationservice.dto.MoveProbeRequestDTO;
import com.codekata.oceanprobe.probenavigationservice.dto.MoveProbeResponseDTO;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProbeNavigationService {
    public MoveProbeResponseDTO moveProbe(UUID probeId, MoveProbeRequestDTO requestDTO) {
        return null;
    }
}
