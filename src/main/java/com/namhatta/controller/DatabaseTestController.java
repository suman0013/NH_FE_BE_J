package com.namhatta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/database-test")
public class DatabaseTestController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/connection")
    public Map<String, Object> testConnection() {
        Map<String, Object> response = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            response.put("status", "SUCCESS");
            response.put("connected", true);
            response.put("database", connection.getCatalog());
            response.put("url", connection.getMetaData().getURL());
            
            // Test a simple query on existing devotees table
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM devotees")) {
                if (rs.next()) {
                    response.put("devotees_count", rs.getInt("count"));
                }
            }
            
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("connected", false);
            response.put("error", e.getMessage());
        }
        
        return response;
    }
}