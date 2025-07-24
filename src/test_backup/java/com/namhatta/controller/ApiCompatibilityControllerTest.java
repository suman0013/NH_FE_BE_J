package com.namhatta.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for ApiCompatibilityController
 */
@WebMvcTest(ApiCompatibilityController.class)
class ApiCompatibilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testHealth_ShouldReturnHealthStatus() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/test/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("OK"))
                .andExpect(jsonPath("$.data.service").value("Spring Boot Namhatta Management System"))
                .andExpect(jsonPath("$.data.version").value("1.0.0"));
    }

    @Test
    void testResponseFormat_ShouldReturnFormattedResponse() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/test/response-format"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.message").value("Response format test"))
                .andExpect(jsonPath("$.data.success").value(true))
                .andExpect(jsonPath("$.data.data").isArray())
                .andExpect(jsonPath("$.data.count").value(3));
    }

    @Test
    void testErrorFormat_ShouldReturnErrorResponse() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/test/error-format"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("This is a test error message"));
    }

    @Test
    void testPaginationFormat_WithDefaultParams_ShouldReturnPaginatedData() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/test/pagination"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.page").value(0))
                .andExpect(jsonPath("$.data.size").value(10))
                .andExpect(jsonPath("$.data.totalElements").value(100))
                .andExpect(jsonPath("$.data.totalPages").value(10))
                .andExpect(jsonPath("$.data.first").value(true))
                .andExpect(jsonPath("$.data.last").value(false));
    }

    @Test
    void testPaginationFormat_WithCustomParams_ShouldRespectParams() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/test/pagination")
                .param("page", "2")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.page").value(2))
                .andExpect(jsonPath("$.data.size").value(5))
                .andExpect(jsonPath("$.data.first").value(false))
                .andExpect(jsonPath("$.data.hasPrevious").value(true));
    }

    @Test
    void getCompatibilityReport_ShouldReturnComprehensiveReport() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/test/compatibility-report"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.summary.totalTests").value(24))
                .andExpect(jsonPath("$.data.summary.passedTests").value(20))
                .andExpect(jsonPath("$.data.summary.failedTests").value(4))
                .andExpect(jsonPath("$.data.summary.successRate").value("83.3%"))
                .andExpect(jsonPath("$.data.testDetails").exists())
                .andExpect(jsonPath("$.data.pendingFeatures").isArray())
                .andExpect(jsonPath("$.data.recommendedAction").value("Continue with frontend integration"));
    }

    @Test
    void testDatabaseConnectivity_ShouldReturnConnectionStatus() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/test/database"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("CONNECTED"))
                .andExpect(jsonPath("$.data.database").value("PostgreSQL"))
                .andExpect(jsonPath("$.data.host").value("Neon serverless"))
                .andExpect(jsonPath("$.data.connectionTest").value("SUCCESS"));
    }
}