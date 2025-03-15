package com.codekata.oceanprobe.probenavigationservice.service;

import com.codekata.oceanprobe.probenavigationservice.entity.OceanFloor;
import com.codekata.oceanprobe.probenavigationservice.repository.OceanFloorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OceanFloorService {

    private static final Logger logger = LoggerFactory.getLogger(OceanFloorService.class);

    @Autowired
    private OceanFloorRepository oceanFloorRepository;

    /**
     * Retrieves ocean floor data from the database and builds a 2D grid.
     * @param width Width of the grid (X-axis)
     * @param height Height of the grid (Y-axis)
     * @return 2D integer array representing the grid (0 = open space, 1 = obstacle)
     */
    public int[][] getOceanFloorGrid(int width, int height) {
        int[][] grid = new int[height][width]; // Initialize empty grid with zeros (default open space)

        logger.info("🔍 Fetching ocean floor data to construct a {}x{} grid...", width, height);

        // Fetch ocean floor data sorted by (y, x)
        List<OceanFloor> cells = oceanFloorRepository.findAllSorted();

        // Populate grid with obstacles (1 = obstacle, 0 = open space)
        for (OceanFloor cell : cells) {
            if (cell.getXPosition() < width && cell.getYPosition() < height) {
                grid[cell.getYPosition()][cell.getXPosition()] = cell.isObstacle() ? 1 : 0;
            }
        }

        // Print grid to logs
        logGrid(grid);
        return grid;
    }

    /**
     * Logs the grid in a human-readable format.
     * @param grid 2D integer array representing the ocean floor
     */
    private void logGrid(int[][] grid) {
        logger.info("\n🌊 Ocean Floor Grid Representation 🌊");
        for (int[] row : grid) {
            StringBuilder rowString = new StringBuilder();
            for (int cell : row) {
                rowString.append(cell == 1 ? "🪨 " : "🌊 ").append(" ");
            }
            logger.info(rowString.toString()); // Log each row
        }
        logger.info("✅ Grid generation complete!");
    }

    public Boolean isObstacle(int i, int i1) {
        return null;
    }
}
