package com.codekata.oceanprobe.probenavigationservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "navigation_trail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NavigationTrail {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "probe_id", nullable = false)
    private Probe probe;


    @Column(name = "x_position", nullable = false)
    private int xPosition;

    @Column(name = "y_position", nullable = false)
    private int yPosition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Probe.Direction direction;

    @Column(name = "moved_at")
    private LocalDateTime movedAt = LocalDateTime.now();
}
