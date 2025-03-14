package com.codekata.oceanprobe.probenavigationservice.service;

import com.codekata.oceanprobe.probenavigationservice.entity.OceanFloor;
import com.codekata.oceanprobe.probenavigationservice.repository.OceanFloorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OceanFloorServiceTest {

    @Mock
    private OceanFloorRepository oceanFloorRepository;

    @InjectMocks
    private OceanFloorService oceanFloorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenOceanFloorData_whenGetOceanFloorGrid_thenReturnCorrectGrid() {
        // Given: A predefined dataset representing the ocean floor
        List<OceanFloor> mockOceanFloorData = Arrays.asList(
                new OceanFloor(1L, 0, 0, false),
                new OceanFloor(2L, 1, 0, false),
                new OceanFloor(3L, 2, 0, true),  // Obstacle
                new OceanFloor(4L, 3, 0, false),
                new OceanFloor(5L, 4, 0, false),

                new OceanFloor(6L, 0, 1, false),
                new OceanFloor(7L, 1, 1, true),  // Obstacle
                new OceanFloor(8L, 2, 1, false),
                new OceanFloor(9L, 3, 1, true),  // Obstacle
                new OceanFloor(10L, 4, 1, false),

                new OceanFloor(11L, 0, 2, false),
                new OceanFloor(12L, 1, 2, false),
                new OceanFloor(13L, 2, 2, false),
                new OceanFloor(14L, 3, 2, true), // Obstacle
                new OceanFloor(15L, 4, 2, false)
        );

        when(oceanFloorRepository.findAllSorted()).thenReturn(mockOceanFloorData);

        // When: Fetching the ocean floor grid
        int[][] grid = oceanFloorService.getOceanFloorGrid(5, 3);

        // Then: Ensure the grid has correct dimensions
        assertEquals(3, grid.length, "Grid height should be 3");
        assertEquals(5, grid[0].length, "Grid width should be 5");

        // Then: Verify the grid content (0 = open space, 1 = obstacle)
        assertEquals(0, grid[0][0], "Expected (0,0) to be open space");
        assertEquals(0, grid[0][1], "Expected (1,0) to be open space");
        assertEquals(1, grid[0][2], "Expected (2,0) to be an obstacle");
        assertEquals(0, grid[0][3], "Expected (3,0) to be open space");
        assertEquals(0, grid[0][4], "Expected (4,0) to be open space");

        assertEquals(0, grid[1][0], "Expected (0,1) to be open space");
        assertEquals(1, grid[1][1], "Expected (1,1) to be an obstacle");
        assertEquals(0, grid[1][2], "Expected (2,1) to be open space");
        assertEquals(1, grid[1][3], "Expected (3,1) to be an obstacle");
        assertEquals(0, grid[1][4], "Expected (4,1) to be open space");

        assertEquals(0, grid[2][0], "Expected (0,2) to be open space");
        assertEquals(0, grid[2][1], "Expected (1,2) to be open space");
        assertEquals(0, grid[2][2], "Expected (2,2) to be open space");
        assertEquals(1, grid[2][3], "Expected (3,2) to be an obstacle");
        assertEquals(0, grid[2][4], "Expected (4,2) to be open space");

        // Verify that the repository method was called
        verify(oceanFloorRepository, times(1)).findAllSorted();
    }
}
