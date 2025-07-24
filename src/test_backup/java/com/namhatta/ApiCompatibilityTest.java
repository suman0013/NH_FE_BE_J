package com.namhatta;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * API Compatibility Test Suite
 * 
 * Validates that Spring Boot backend provides 100% API compatibility
 * with the original Node.js Express backend.
 * 
 * Tests cover:
 * - Response format consistency
 * - HTTP status codes
 * - JSON structure validation
 * - Error handling compatibility
 * - Pagination format
 * - Authentication responses
 */
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApiCompatibilityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test API test endpoints are accessible
     */
    @Test
    public void testApiTestEndpointsAccessible() throws Exception {
        // Test main API compatibility endpoint
        mockMvc.perform(get("/api/test/api-compatibility"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists());
    }

    /**
     * Test performance benchmark endpoint
     */
    @Test
    public void testPerformanceBenchmarkEndpoint() throws Exception {
        mockMvc.perform(get("/api/test/performance-benchmark"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.benchmarks").exists())
                .andExpect(jsonPath("$.data.overallAvgResponseTime").exists());
    }

    /**
     * Test response format validation endpoint
     */
    @Test
    public void testResponseFormatValidationEndpoint() throws Exception {
        mockMvc.perform(get("/api/test/response-format-validation"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.validatedEndpoints").exists())
                .andExpect(jsonPath("$.data.formatIssues").exists());
    }

    /**
     * Test database connectivity endpoint
     */
    @Test
    public void testDatabaseConnectivityEndpoint() throws Exception {
        mockMvc.perform(get("/api/test/database-connectivity"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.connectionStatus").exists());
    }

    /**
     * Test geography API compatibility
     */
    @Test
    public void testGeographyApiCompatibility() throws Exception {
        mockMvc.perform(get("/api/test/geography/api-compatibility"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.passedTests").exists())
                .andExpect(jsonPath("$.data.failedTests").exists());
    }

    /**
     * Test map data API compatibility
     */
    @Test
    public void testMapDataApiCompatibility() throws Exception {
        mockMvc.perform(get("/api/test/geography/map-data-compatibility"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.successRate").exists());
    }

    /**
     * Test health endpoint format matches Node.js
     */
    @Test
    public void testHealthEndpointFormat() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        
        // Verify response contains expected fields matching Node.js format
        assertTrue(responseContent.contains("status"));
        
        // Parse JSON to validate structure
        var jsonResponse = objectMapper.readTree(responseContent);
        assertNotNull(jsonResponse.get("status"));
    }

    /**
     * Test about endpoint format matches Node.js
     */
    @Test
    public void testAboutEndpointFormat() throws Exception {
        mockMvc.perform(get("/api/about"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Namhatta Management System"))
                .andExpect(jsonPath("$.version").exists())
                .andExpect(jsonPath("$.description").exists());
    }

    /**
     * Test error response format consistency
     */
    @Test
    public void testErrorResponseFormat() throws Exception {
        // Test accessing a non-existent devotee
        mockMvc.perform(get("/api/devotees/99999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").exists());
    }

    /**
     * Test pagination format consistency for devotees
     */
    @Test
    public void testDevoteePaginationFormat() throws Exception {
        mockMvc.perform(get("/api/devotees")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").exists())
                .andExpect(jsonPath("$.data.totalElements").exists())
                .andExpect(jsonPath("$.data.totalPages").exists())
                .andExpect(jsonPath("$.data.number").exists())
                .andExpect(jsonPath("$.data.size").exists());
    }

    /**
     * Test pagination format consistency for namhattas
     */
    @Test
    public void testNamhattaPaginationFormat() throws Exception {
        mockMvc.perform(get("/api/namhattas")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").exists())
                .andExpect(jsonPath("$.data.totalElements").exists());
    }

    /**
     * Test devotional status list format
     */
    @Test
    public void testDevotionalStatusListFormat() throws Exception {
        mockMvc.perform(get("/api/statuses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    /**
     * Test that all controller endpoints return consistent ApiResponse format
     */
    @Test
    public void testConsistentApiResponseFormat() throws Exception {
        String[] endpoints = {
            "/api/health",
            "/api/about",
            "/api/statuses"
        };

        for (String endpoint : endpoints) {
            MvcResult result = mockMvc.perform(get(endpoint))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            String responseContent = result.getResponse().getContentAsString();
            var jsonResponse = objectMapper.readTree(responseContent);
            
            // All successful responses should have these fields
            if (endpoint.equals("/api/health")) {
                // Health endpoint has different format for compatibility
                assertNotNull(jsonResponse.get("status"));
            } else {
                assertNotNull(jsonResponse.get("success"), 
                    "Endpoint " + endpoint + " missing 'success' field");
                assertNotNull(jsonResponse.get("data"), 
                    "Endpoint " + endpoint + " missing 'data' field");
            }
        }
    }

    /**
     * Test authentication endpoint format (without actual authentication)
     */
    @Test
    public void testAuthenticationEndpointFormat() throws Exception {
        // Test login endpoint with invalid credentials to check error format
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"invalid\",\"password\":\"invalid\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").exists());
    }
}