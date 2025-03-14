package com.codekata.oceanprobe.probenavigationservice.database;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class H2DatabaseTest {
    @Autowired
    private DataSource dataSource;

    @Test
    public void testH2DatabaseConnection() throws Exception{
        assertNotNull(dataSource.getConnection());
        System.out.println("✅ Successfully connected to H2 Database!");
    }

    @Test
    public void testDatabaseAndTablesExist() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            assertTrue(checkTableExists(connection, "probes"), "❌ Table 'probes' does not exist!");
            assertTrue(checkTableExists(connection, "navigation_trail"), "❌ Table 'navigation_trail' does not exist!");
            assertTrue(checkTableExists(connection, "ocean_floor"), "❌ Table 'ocean_floor' does not exist!");

            System.out.println("✅ All required tables exist in H2 Database!");
        }
    }

    private boolean checkTableExists(Connection connection, String tableName) throws SQLException {
        try (ResultSet resultSet = connection.getMetaData().getTables(null, "PUBLIC", tableName.toUpperCase(), new String[]{"TABLE"})) {
            return resultSet.next();
        }
    }
}
