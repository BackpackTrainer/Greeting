package com.example.demo.bdd.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class TestDataCleaner {

    @Value("${test.datasource.url}")
    private String dbUrl;

    @Value("${test.datasource.username}")
    private String dbUsername;

    @Value("${test.datasource.password}")
    private String dbPassword;

    public void resetTestData() {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             Statement stmt = conn.createStatement()) {

            // First truncate the table to clear any existing data
            stmt.executeUpdate("TRUNCATE TABLE greeting");

            // Now repopulate using data.sql via ResourceDatabasePopulator
            DataSource dataSource = new SingleConnectionDataSource(conn, false);
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(new ClassPathResource("data.sql"));
            populator.execute(dataSource);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to reset test data", e);
        }
    }
}
