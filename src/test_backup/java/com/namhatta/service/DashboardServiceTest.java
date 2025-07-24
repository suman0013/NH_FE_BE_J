package com.namhatta.service;

import com.namhatta.repository.DevoteeRepository;
import com.namhatta.repository.NamhattaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for DashboardService
 */
@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private DevoteeRepository devoteeRepository;

    @Mock
    private NamhattaRepository namhattaRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @BeforeEach
    void setUp() {
        // Setup common mock data if needed
    }

    @Test
    void getDashboardSummary_ShouldReturnCorrectCounts() {
        // Arrange
        when(devoteeRepository.count()).thenReturn(150L);
        when(namhattaRepository.count()).thenReturn(25L);

        // Act
        Map<String, Object> result = dashboardService.getDashboardSummary();

        // Assert
        assertNotNull(result);
        assertEquals(150L, result.get("totalDevotees"));
        assertEquals(25L, result.get("totalNamhattas"));
        assertNotNull(result.get("recentUpdates"));
        assertTrue(result.get("recentUpdates") instanceof List);

        verify(devoteeRepository).count();
        verify(namhattaRepository).count();
    }

    @Test
    void getStatusDistribution_ShouldReturnStatusCounts() {
        // Arrange
        List<Object[]> mockStatusCounts = Arrays.asList(
            new Object[]{"Bhakta", 50L},
            new Object[]{"Initiated", 30L},
            new Object[]{"Brahmachari", 20L},
            new Object[]{null, 10L} // Test null handling
        );
        when(devoteeRepository.findDevoteeCountByStatus()).thenReturn(mockStatusCounts);

        // Act
        Map<String, Object> result = dashboardService.getStatusDistribution();

        // Assert
        assertNotNull(result);
        assertNotNull(result.get("statusDistribution"));
        
        @SuppressWarnings("unchecked")
        Map<String, Long> distribution = (Map<String, Long>) result.get("statusDistribution");
        
        assertEquals(50L, distribution.get("Bhakta"));
        assertEquals(30L, distribution.get("Initiated"));
        assertEquals(20L, distribution.get("Brahmachari"));
        assertEquals(10L, distribution.get("Unknown")); // null should be converted to "Unknown"

        verify(devoteeRepository).findDevoteeCountByStatus();
    }

    @Test
    void getNamhattaCountsByCountry_ShouldReturnCountryData() {
        // Arrange
        List<Object[]> mockCountryCounts = Arrays.asList(
            new Object[]{"India", 20L},
            new Object[]{"USA", 3L},
            new Object[]{null, 2L} // Test null handling
        );
        when(namhattaRepository.findNamhattaCountsByCountry()).thenReturn(mockCountryCounts);

        // Act
        List<Map<String, Object>> result = dashboardService.getNamhattaCountsByCountry();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        
        Map<String, Object> firstCountry = result.get(0);
        assertEquals("India", firstCountry.get("country"));
        assertEquals(20L, firstCountry.get("count"));
        
        Map<String, Object> lastCountry = result.get(2);
        assertEquals("Unknown", lastCountry.get("country")); // null should be converted to "Unknown"
        assertEquals(2L, lastCountry.get("count"));

        verify(namhattaRepository).findNamhattaCountsByCountry();
    }

    @Test
    void getNamhattaCountsByState_ShouldReturnStateData() {
        // Arrange
        List<Object[]> mockStateCounts = Arrays.asList(
            new Object[]{"West Bengal", 15L},
            new Object[]{"Maharashtra", 8L}
        );
        when(namhattaRepository.findNamhattaCountsByState()).thenReturn(mockStateCounts);

        // Act
        List<Map<String, Object>> result = dashboardService.getNamhattaCountsByState();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        
        Map<String, Object> firstState = result.get(0);
        assertEquals("West Bengal", firstState.get("state"));
        assertEquals(15L, firstState.get("count"));

        verify(namhattaRepository).findNamhattaCountsByState();
    }

    @Test
    void getNamhattaCountsByDistrict_ShouldReturnDistrictData() {
        // Arrange
        List<Object[]> mockDistrictCounts = Arrays.asList(
            new Object[]{"Kolkata", 10L},
            new Object[]{"Mumbai", 5L}
        );
        when(namhattaRepository.findNamhattaCountsByDistrict()).thenReturn(mockDistrictCounts);

        // Act
        List<Map<String, Object>> result = dashboardService.getNamhattaCountsByDistrict();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        
        Map<String, Object> firstDistrict = result.get(0);
        assertEquals("Kolkata", firstDistrict.get("district"));
        assertEquals(10L, firstDistrict.get("count"));

        verify(namhattaRepository).findNamhattaCountsByDistrict();
    }

    @Test
    void getDashboardSummary_WhenRepositoryThrowsException_ShouldPropagateException() {
        // Arrange
        when(devoteeRepository.count()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> dashboardService.getDashboardSummary());
        
        assertEquals("Failed to fetch dashboard summary", exception.getMessage());
        assertNotNull(exception.getCause());
        assertEquals("Database error", exception.getCause().getMessage());

        verify(devoteeRepository).count();
    }

    @Test
    void getStatusDistribution_WhenRepositoryThrowsException_ShouldPropagateException() {
        // Arrange
        when(devoteeRepository.findDevoteeCountByStatus()).thenThrow(new RuntimeException("Query failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> dashboardService.getStatusDistribution());
        
        assertEquals("Failed to fetch status distribution", exception.getMessage());
        assertNotNull(exception.getCause());
        assertEquals("Query failed", exception.getCause().getMessage());

        verify(devoteeRepository).findDevoteeCountByStatus();
    }
}