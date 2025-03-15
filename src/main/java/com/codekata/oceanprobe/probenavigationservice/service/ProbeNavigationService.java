package com.codekata.oceanprobe.probenavigationservice.service;

import com.codekata.oceanprobe.probenavigationservice.dto.MoveProbeRequestDTO;
import com.codekata.oceanprobe.probenavigationservice.dto.MoveProbeResponseDTO;
import com.codekata.oceanprobe.probenavigationservice.dto.NavigationTrailDTO;
import com.codekata.oceanprobe.probenavigationservice.entity.Probe;
import com.codekata.oceanprobe.probenavigationservice.entity.NavigationTrail;
import com.codekata.oceanprobe.probenavigationservice.exception.DataNotFoundException;
import com.codekata.oceanprobe.probenavigationservice.exception.InvalidMoveException;
import com.codekata.oceanprobe.probenavigationservice.exception.ObstacleEncounteredException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProbeNavigationService {

    private final ProbeService probeService;
    private final OceanFloorService oceanFloorService;
    private final NavigationTrailService navigationTrailService;

    public MoveProbeResponseDTO moveProbe(UUID probeId, MoveProbeRequestDTO requestDTO) {
        Probe probe = probeService.getProbeById(probeId);
        if (probe == null) {
            throw new DataNotFoundException("Probe not found");
        }

        List<NavigationTrailDTO> navigationTrail = new ArrayList<>();
        for (String command : requestDTO.getCommands()) {
            executeCommand(probe, command, navigationTrail);
        }

        // Save final position after movement
        probeService.saveOrUpdateProbe(probe);

        return new MoveProbeResponseDTO(
                probe.getId(),
                probe.getXPosition(),
                probe.getYPosition(),
                probe.getDirection(),
                navigationTrail,
                "COMPLETED"
        );
    }

    private void executeCommand(Probe probe, String command, List<NavigationTrailDTO> trail) {
        int newX = probe.getXPosition();
        int newY = probe.getYPosition();

        switch (command) {
            case "F":
                newX = moveForwardX(probe);
                newY = moveForwardY(probe);
                break;
            case "B":
                newX = moveBackwardX(probe);
                newY = moveBackwardY(probe);
                break;
            case "L":
                probe.setDirection(turnLeft(probe.getDirection()));
                return;
            case "R":
                probe.setDirection(turnRight(probe.getDirection()));
                return;
            default:
                throw new InvalidMoveException("Invalid command: " + command);
        }

        if (oceanFloorService.isObstacle(newX, newY)) {
            throw new ObstacleEncounteredException("Obstacle encountered at (" + newX + ", " + newY + "). Move blocked.");
        }

        if (newX < 0 || newY < 0) {
            throw new InvalidMoveException("Invalid move: (" + newX + ", " + newY + ") is out of bounds.");
        }

        // Update probe position
        probe.setXPosition(newX);
        probe.setYPosition(newY);

        // Save navigation trail
        NavigationTrail newTrail = navigationTrailService.saveMoveAsTrail(
                new NavigationTrail(UUID.randomUUID(), probe, newX, newY, probe.getDirection(), LocalDateTime.now())
        );

        trail.add(new NavigationTrailDTO(newX, newY));
    }

    private int moveForwardX(Probe probe) {
        return switch (probe.getDirection()) {
            case EAST -> probe.getXPosition() + 1;
            case WEST -> probe.getXPosition() - 1;
            default -> probe.getXPosition();
        };
    }

    private int moveForwardY(Probe probe) {
        return switch (probe.getDirection()) {
            case NORTH -> probe.getYPosition() + 1;
            case SOUTH -> probe.getYPosition() - 1;
            default -> probe.getYPosition();
        };
    }

    private int moveBackwardX(Probe probe) {
        return switch (probe.getDirection()) {
            case EAST -> probe.getXPosition() - 1;
            case WEST -> probe.getXPosition() + 1;
            default -> probe.getXPosition();
        };
    }

    private int moveBackwardY(Probe probe) {
        return switch (probe.getDirection()) {
            case NORTH -> probe.getYPosition() - 1;
            case SOUTH -> probe.getYPosition() + 1;
            default -> probe.getYPosition();
        };
    }

    private Probe.Direction turnLeft(Probe.Direction currentDirection) {
        return switch (currentDirection) {
            case NORTH -> Probe.Direction.WEST;
            case WEST -> Probe.Direction.SOUTH;
            case SOUTH -> Probe.Direction.EAST;
            case EAST -> Probe.Direction.NORTH;
        };
    }

    private Probe.Direction turnRight(Probe.Direction currentDirection) {
        return switch (currentDirection) {
            case NORTH -> Probe.Direction.EAST;
            case EAST -> Probe.Direction.SOUTH;
            case SOUTH -> Probe.Direction.WEST;
            case WEST -> Probe.Direction.NORTH;
        };
    }
}
