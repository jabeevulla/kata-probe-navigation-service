package com.codekata.oceanprobe.probenavigationservice.database;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class H2DatabaseTest {
    @Autowired
    private DataSource dataSource;

    @Test
    public void testH2DatabaseConnection() throws Exception{
        assertNotNull(dataSource.getConnection());
        System.out.println("✅ Successfully connected to H2 Database!");
    }
}
