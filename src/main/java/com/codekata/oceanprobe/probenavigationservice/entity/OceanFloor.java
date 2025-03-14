package com.codekata.oceanprobe.probenavigationservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ocean_floor")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OceanFloor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "x_position", nullable = false)
    private int xPosition;

    @Column(name = "y_position", nullable = false)
    private int yPosition;

    @Column(name = "is_obstacle", nullable = false)
    private boolean isObstacle;
}
