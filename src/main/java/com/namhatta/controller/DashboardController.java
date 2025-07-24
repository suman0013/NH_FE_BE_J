package com.namhatta.controller;

import com.namhatta.dto.ApiResponse;
import com.namhatta.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Dashboard Controller
 * 
 * Handles dashboard and statistics API endpoints to match Node.js backend functionality.
 * Provides summary data, analytics, and map visualization data.
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * Get dashboard summary statistics
     * GET /api/dashboard
     * Requires authentication
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICE') or hasRole('DISTRICT_SUPERVISOR')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardSummary() {
        log.info("GET /api/dashboard - Fetching dashboard summary");
        try {
            Map<String, Object> summary = dashboardService.getDashboardSummary();
            log.debug("Dashboard summary retrieved successfully");
            return ResponseEntity.ok(ApiResponse.success(summary));
        } catch (Exception e) {
            log.error("Error fetching dashboard summary", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch dashboard summary: " + e.getMessage()));
        }
    }

    /**
     * Get devotional status distribution
     * GET /api/status-distribution
     * Requires authentication
     */
    @GetMapping("/status-distribution")
    @PreAuthorize("hasRole('ADMIN') or hasRole('OFFICE') or hasRole('DISTRICT_SUPERVISOR')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStatusDistribution() {
        log.info("GET /api/status-distribution - Fetching status distribution");
        try {
            Map<String, Object> distribution = dashboardService.getStatusDistribution();
            log.debug("Status distribution retrieved successfully");
            return ResponseEntity.ok(ApiResponse.success(distribution));
        } catch (Exception e) {
            log.error("Error fetching status distribution", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch status distribution: " + e.getMessage()));
        }
    }

    /**
     * Get namhatta counts by country for map visualization
     * GET /api/map/countries
     */
    @GetMapping("/map/countries")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getNamhattaCountsByCountry() {
        log.info("GET /api/map/countries - Fetching namhatta counts by country");
        try {
            List<Map<String, Object>> countryCounts = dashboardService.getNamhattaCountsByCountry();
            log.debug("Found {} countries with namhattas", countryCounts.size());
            return ResponseEntity.ok(ApiResponse.success(countryCounts));
        } catch (Exception e) {
            log.error("Error fetching namhatta counts by country", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch country data: " + e.getMessage()));
        }
    }

    /**
     * Get namhatta counts by state for map visualization
     * GET /api/map/states
     */
    @GetMapping("/map/states")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getNamhattaCountsByState() {
        log.info("GET /api/map/states - Fetching namhatta counts by state");
        try {
            List<Map<String, Object>> stateCounts = dashboardService.getNamhattaCountsByState();
            log.debug("Found {} states with namhattas", stateCounts.size());
            return ResponseEntity.ok(ApiResponse.success(stateCounts));
        } catch (Exception e) {
            log.error("Error fetching namhatta counts by state", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch state data: " + e.getMessage()));
        }
    }

    /**
     * Get namhatta counts by district for map visualization
     * GET /api/map/districts
     */
    @GetMapping("/map/districts")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getNamhattaCountsByDistrict() {
        log.info("GET /api/map/districts - Fetching namhatta counts by district");
        try {
            List<Map<String, Object>> districtCounts = dashboardService.getNamhattaCountsByDistrict();
            log.debug("Found {} districts with namhattas", districtCounts.size());
            return ResponseEntity.ok(ApiResponse.success(districtCounts));
        } catch (Exception e) {
            log.error("Error fetching namhatta counts by district", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch district data: " + e.getMessage()));
        }
    }

    /**
     * Get namhatta counts by sub-district for map visualization
     * GET /api/map/sub-districts
     */
    @GetMapping("/map/sub-districts")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getNamhattaCountsBySubDistrict() {
        log.info("GET /api/map/sub-districts - Fetching namhatta counts by sub-district");
        try {
            List<Map<String, Object>> subDistrictCounts = dashboardService.getNamhattaCountsBySubDistrict();
            log.debug("Found {} sub-districts with namhattas", subDistrictCounts.size());
            return ResponseEntity.ok(ApiResponse.success(subDistrictCounts));
        } catch (Exception e) {
            log.error("Error fetching namhatta counts by sub-district", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch sub-district data: " + e.getMessage()));
        }
    }

    /**
     * Get namhatta counts by village for map visualization
     * GET /api/map/villages
     */
    @GetMapping("/map/villages")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getNamhattaCountsByVillage() {
        log.info("GET /api/map/villages - Fetching namhatta counts by village");
        try {
            List<Map<String, Object>> villageCounts = dashboardService.getNamhattaCountsByVillage();
            log.debug("Found {} villages with namhattas", villageCounts.size());
            return ResponseEntity.ok(ApiResponse.success(villageCounts));
        } catch (Exception e) {
            log.error("Error fetching namhatta counts by village", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch village data: " + e.getMessage()));
        }
    }
}