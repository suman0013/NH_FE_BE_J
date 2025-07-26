package com.namhatta.controller;

import com.namhatta.dto.ApiResponse;
import com.namhatta.service.GeographyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapDataController {
    
    private final GeographyService geographyService;
    
    /**
     * Get namhatta counts by country for map visualization
     * GET /map/countries
     */
    @GetMapping("/countries")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getCountryMapData() {
        log.info("GET /map/countries - Fetching country-wise namhatta counts");
        try {
            List<Map<String, Object>> mapData = geographyService.getCountryMapData();
            return ResponseEntity.ok(ApiResponse.success(mapData));
        } catch (Exception e) {
            log.error("Error fetching country map data", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch country map data: " + e.getMessage()));
        }
    }
    
    /**
     * Get namhatta counts by state for map visualization
     * GET /map/states
     */
    @GetMapping("/states")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getStateMapData(
            @RequestParam(required = false) String country) {
        log.info("GET /map/states - Fetching state-wise namhatta counts for country: {}", country);
        try {
            List<Map<String, Object>> mapData = geographyService.getStateMapData(country);
            return ResponseEntity.ok(ApiResponse.success(mapData));
        } catch (Exception e) {
            log.error("Error fetching state map data", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch state map data: " + e.getMessage()));
        }
    }
    
    /**
     * Get namhatta counts by district for map visualization
     * GET /map/districts
     */
    @GetMapping("/districts")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getDistrictMapData(
            @RequestParam(required = false) String state) {
        log.info("GET /map/districts - Fetching district-wise namhatta counts for state: {}", state);
        try {
            List<Map<String, Object>> mapData = geographyService.getDistrictMapData(state);
            return ResponseEntity.ok(ApiResponse.success(mapData));
        } catch (Exception e) {
            log.error("Error fetching district map data", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch district map data: " + e.getMessage()));
        }
    }
    
    /**
     * Get namhatta counts by sub-district for map visualization
     * GET /map/sub-districts
     */
    @GetMapping("/sub-districts")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getSubDistrictMapData(
            @RequestParam(required = false) String district) {
        log.info("GET /map/sub-districts - Fetching sub-district-wise namhatta counts for district: {}", district);
        try {
            List<Map<String, Object>> mapData = geographyService.getSubDistrictMapData(district);
            return ResponseEntity.ok(ApiResponse.success(mapData));
        } catch (Exception e) {
            log.error("Error fetching sub-district map data", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch sub-district map data: " + e.getMessage()));
        }
    }
    
    /**
     * Get namhatta counts by village for map visualization
     * GET /map/villages
     */
    @GetMapping("/villages")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getVillageMapData(
            @RequestParam(required = false) String subDistrict) {
        log.info("GET /map/villages - Fetching village-wise namhatta counts for sub-district: {}", subDistrict);
        try {
            List<Map<String, Object>> mapData = geographyService.getVillageMapData(subDistrict);
            return ResponseEntity.ok(ApiResponse.success(mapData));
        } catch (Exception e) {
            log.error("Error fetching village map data", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch village map data: " + e.getMessage()));
        }
    }
}