package com.namhatta.controller;

import com.namhatta.service.DashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for DashboardController
 */
@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DashboardService dashboardService;

    private Map<String, Object> mockSummary;
    private Map<String, Object> mockStatusDistribution;
    private List<Map<String, Object>> mockCountryData;

    @BeforeEach
    void setUp() {
        // Mock dashboard summary
        mockSummary = new HashMap<>();
        mockSummary.put("totalDevotees", 150L);
        mockSummary.put("totalNamhattas", 25L);
        mockSummary.put("recentUpdates", Arrays.asList("Update 1", "Update 2"));

        // Mock status distribution
        mockStatusDistribution = new HashMap<>();
        Map<String, Long> distribution = new HashMap<>();
        distribution.put("Bhakta", 50L);
        distribution.put("Initiated", 30L);
        mockStatusDistribution.put("statusDistribution", distribution);

        // Mock country data
        mockCountryData = Arrays.asList(
            Map.of("country", "India", "count", 20L),
            Map.of("country", "USA", "count", 3L)
        );
    }

    @Test
    void getDashboardSummary_ShouldReturnSummaryData() throws Exception {
        // Arrange
        when(dashboardService.getDashboardSummary()).thenReturn(mockSummary);

        // Act & Assert
        mockMvc.perform(get("/api/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalDevotees").value(150))
                .andExpect(jsonPath("$.data.totalNamhattas").value(25))
                .andExpect(jsonPath("$.data.recentUpdates").isArray());

        verify(dashboardService).getDashboardSummary();
    }

    @Test
    void getStatusDistribution_ShouldReturnStatusData() throws Exception {
        // Arrange
        when(dashboardService.getStatusDistribution()).thenReturn(mockStatusDistribution);

        // Act & Assert
        mockMvc.perform(get("/api/dashboard/status-distribution"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.statusDistribution").exists())
                .andExpect(jsonPath("$.data.statusDistribution.Bhakta").value(50))
                .andExpect(jsonPath("$.data.statusDistribution.Initiated").value(30));

        verify(dashboardService).getStatusDistribution();
    }

    @Test
    void getNamhattaCountsByCountry_ShouldReturnCountryData() throws Exception {
        // Arrange
        when(dashboardService.getNamhattaCountsByCountry()).thenReturn(mockCountryData);

        // Act & Assert
        mockMvc.perform(get("/api/dashboard/namhattas-by-country"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].country").value("India"))
                .andExpect(jsonPath("$.data[0].count").value(20))
                .andExpect(jsonPath("$.data[1].country").value("USA"))
                .andExpect(jsonPath("$.data[1].count").value(3));

        verify(dashboardService).getNamhattaCountsByCountry();
    }

    @Test
    void getNamhattaCountsByState_ShouldReturnStateData() throws Exception {
        // Arrange
        List<Map<String, Object>> mockStateData = Arrays.asList(
            Map.of("state", "West Bengal", "count", 15L),
            Map.of("state", "Maharashtra", "count", 8L)
        );
        when(dashboardService.getNamhattaCountsByState()).thenReturn(mockStateData);

        // Act & Assert
        mockMvc.perform(get("/api/dashboard/namhattas-by-state"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].state").value("West Bengal"))
                .andExpect(jsonPath("$.data[0].count").value(15));

        verify(dashboardService).getNamhattaCountsByState();
    }

    @Test
    void getNamhattaCountsByDistrict_ShouldReturnDistrictData() throws Exception {
        // Arrange
        List<Map<String, Object>> mockDistrictData = Arrays.asList(
            Map.of("district", "Kolkata", "count", 10L),
            Map.of("district", "Mumbai", "count", 5L)
        );
        when(dashboardService.getNamhattaCountsByDistrict()).thenReturn(mockDistrictData);

        // Act & Assert
        mockMvc.perform(get("/api/dashboard/namhattas-by-district"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].district").value("Kolkata"))
                .andExpect(jsonPath("$.data[0].count").value(10));

        verify(dashboardService).getNamhattaCountsByDistrict();
    }

    @Test
    void getDashboardSummary_WhenServiceThrowsException_ShouldReturnError() throws Exception {
        // Arrange
        when(dashboardService.getDashboardSummary())
                .thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        mockMvc.perform(get("/api/dashboard"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Database connection failed"));

        verify(dashboardService).getDashboardSummary();
    }

    @Test
    void getStatusDistribution_WhenServiceThrowsException_ShouldReturnError() throws Exception {
        // Arrange
        when(dashboardService.getStatusDistribution())
                .thenThrow(new RuntimeException("Failed to fetch status distribution"));

        // Act & Assert
        mockMvc.perform(get("/api/dashboard/status-distribution"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Failed to fetch status distribution"));

        verify(dashboardService).getStatusDistribution();
    }
}