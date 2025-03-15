package com.codekata.oceanprobe.probenavigationservice.repository;

import com.codekata.oceanprobe.probenavigationservice.entity.OceanFloor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@DataJpaTest
public class OceanFloorRepositoryTest {
    @Autowired
    private OceanFloorRepository oceanFloorRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setup(){
        // Given: A clean database and ocean floor setup
        oceanFloorRepository.deleteAll();

        // Insert test data
        oceanFloorRepository.save(new OceanFloor(null, 0, 0, false));
        oceanFloorRepository.save(new OceanFloor(null, 1, 0, false));
        oceanFloorRepository.save(new OceanFloor(null, 2, 0, true));
        oceanFloorRepository.save(new OceanFloor(null, 3, 0, false));
        oceanFloorRepository.save(new OceanFloor(null, 4, 0, false));

        oceanFloorRepository.save(new OceanFloor(null, 0, 1, false));
        oceanFloorRepository.save(new OceanFloor(null, 1, 1, true));
        oceanFloorRepository.save(new OceanFloor(null, 2, 1, false));
        oceanFloorRepository.save(new OceanFloor(null, 3, 1, true));
        oceanFloorRepository.save(new OceanFloor(null, 4, 1, false));

        oceanFloorRepository.save(new OceanFloor(null, 0, 2, false));
        oceanFloorRepository.save(new OceanFloor(null, 1, 2, false));
        oceanFloorRepository.save(new OceanFloor(null, 2, 2, false));
        oceanFloorRepository.save(new OceanFloor(null, 3, 2, true));
        oceanFloorRepository.save(new OceanFloor(null, 4, 2, false));

        entityManager.flush();  // Ensure data is committed
    }

    @Test
    public void givenOceanFloorData_whenFindAllSorted_thenReturnOrderedGrid(){
        // When: Retrieving the ocean floor grid
        List<OceanFloor> oceanFloorList = oceanFloorRepository.findAllSorted();

        // Then: Ensure correct number of entries
        assertEquals(15, oceanFloorList.size(), "Expected 15 rows in the ocean floor grid");

        // Then: Check sorting - first row's first entry should be (0,0)
        assertEquals(0, oceanFloorList.get(0).getYyPosition());
        assertEquals(0, oceanFloorList.get(0).getXxPosition());

        // Then: Obstacles should be identified correctly
        assertFalse(oceanFloorList.get(0).isObstacle(), "Expected (0,0) to be open space");
        assertTrue(oceanFloorList.get(2).isObstacle(), "Expected (2,0) to be an obstacle");
        assertTrue(oceanFloorList.get(6).isObstacle(), "Expected (1,1) to be an obstacle");
    }
}
