package com.codekata.oceanprobe.probenavigationservice.repository;

import com.codekata.oceanprobe.probenavigationservice.entity.OceanFloor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OceanFloorRepository extends JpaRepository<OceanFloor, Long> {

    /**
     * Fetches all ocean floor grid entries, ordered row-wise (y-position first, then x-position).
     */
    @Query("SELECT o FROM OceanFloor o ORDER BY o.yPosition ASC, o.xPosition ASC")
    List<OceanFloor> findAllSorted();
}
