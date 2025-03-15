package com.codekata.oceanprobe.probenavigationservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveProbeRequestDTO {

    @NotEmpty(message = "Commands cannot be empty")
    private List<String> commands;
}