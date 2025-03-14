package com.codekata.oceanprobe.probenavigationservice.repository;

import com.codekata.oceanprobe.probenavigationservice.entity.OceanFloor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OceanFloorRepository extends JpaRepository<OceanFloor, Long> {
}
