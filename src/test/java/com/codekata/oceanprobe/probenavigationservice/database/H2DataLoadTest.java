package com.codekata.oceanprobe.probenavigationservice.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

@SpringBootTest
public class H2DataLoadTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testOceanFloorDataLoaded() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             ResultSet resultSet = connection.createStatement().executeQuery("SELECT COUNT(*) FROM ocean_floor")) {

            resultSet.next();
            int count = resultSet.getInt(1);
            assertTrue(count > 0, "✅ Expected ocean floor data to be populated");
            System.out.println("✅ Ocean floor data correctly loaded: " + count + " records found.");
        }
    }
}
