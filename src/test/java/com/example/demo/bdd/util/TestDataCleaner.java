package com.example.demo.bdd.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

            stmt.executeUpdate("DELETE FROM greeting where id > 4");

        } catch (SQLException e) {
            throw new RuntimeException("Failed to reset test data", e);
        }
    }
}