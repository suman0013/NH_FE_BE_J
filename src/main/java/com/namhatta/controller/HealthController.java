package com.namhatta.controller;

import com.namhatta.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class HealthController {
    
    @Autowired
    private DataSource dataSource;
    
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> health() {
        Map<String, Object> healthInfo = new HashMap<>();
        
        try {
            // Test database connection
            try (Connection conn = dataSource.getConnection()) {
                healthInfo.put("database", "connected");
                healthInfo.put("database_url", conn.getMetaData().getURL());
            }
        } catch (Exception e) {
            healthInfo.put("database", "disconnected");
            healthInfo.put("database_error", e.getMessage());
        }
        
        healthInfo.put("application", "Spring Boot Namhatta Management System");
        healthInfo.put("version", "1.0.0");
        healthInfo.put("status", "running");
        healthInfo.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(ApiResponse.success("Health check completed", healthInfo));
    }
}