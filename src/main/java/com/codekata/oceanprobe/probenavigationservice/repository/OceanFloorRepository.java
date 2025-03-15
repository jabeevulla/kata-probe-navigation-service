package com.codekata.oceanprobe.probenavigationservice.repository;

import com.codekata.oceanprobe.probenavigationservice.entity.OceanFloor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OceanFloorRepository extends JpaRepository<OceanFloor, Long> {

    /**
     * Fetches all ocean floor grid entries, ordered row-wise (y-position first, then x-position).
     */
    @Query("SELECT o FROM OceanFloor o ORDER BY o.yyPosition ASC, o.xxPosition ASC")
    List<OceanFloor> findAllSorted();

    OceanFloor findByXxPositionAndYyPosition(int xPosition, int yPosition);
}
