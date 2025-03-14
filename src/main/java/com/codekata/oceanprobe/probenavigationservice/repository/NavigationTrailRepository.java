package com.codekata.oceanprobe.probenavigationservice.repository;

import com.codekata.oceanprobe.probenavigationservice.entity.NavigationTrail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NavigationTrailRepository extends JpaRepository<NavigationTrail, UUID> {
    List<NavigationTrail> findByProbeId(UUID id);
}
