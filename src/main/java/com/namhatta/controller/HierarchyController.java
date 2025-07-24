package com.namhatta.controller;

import com.namhatta.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Hierarchy Controller
 * 
 * Handles leadership hierarchy API endpoints to match Node.js backend functionality.
 * Provides organizational leadership structure.
 */
@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HierarchyController {

    /**
     * Get leadership hierarchy
     * GET /api/hierarchy
     */
    @GetMapping("/hierarchy")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getHierarchy() {
        log.info("GET /api/hierarchy - Fetching leadership hierarchy");
        try {
            Map<String, Object> hierarchy = new HashMap<>();
            
            // Founder Acharya
            List<Map<String, Object>> founder = new ArrayList<>();
            Map<String, Object> founderData = new HashMap<>();
            founderData.put("id", 1L);
            founderData.put("name", "His Divine Grace A.C. Bhaktivedanta Swami Prabhupada");
            founderData.put("role", "Founder-Acharya");
            founderData.put("location", "ISKCON");
            founder.add(founderData);
            hierarchy.put("founder", founder);
            
            // Current Acharya/GBC
            List<Map<String, Object>> currentAcharya = new ArrayList<>();
            Map<String, Object> gbcData = new HashMap<>();
            gbcData.put("id", 2L);
            gbcData.put("name", "His Holiness Jayapataka Swami");
            gbcData.put("role", "Current Acharya");
            gbcData.put("location", "West Bengal, India");
            currentAcharya.add(gbcData);
            hierarchy.put("currentAcharya", currentAcharya);
            
            // Regional Directors
            List<Map<String, Object>> regionalDirectors = new ArrayList<>();
            Map<String, Object> rdData = new HashMap<>();
            rdData.put("id", 3L);
            rdData.put("name", "Regional Director");
            rdData.put("role", "Regional Director");
            rdData.put("location", "West Bengal");
            regionalDirectors.add(rdData);
            hierarchy.put("regionalDirectors", regionalDirectors);
            
            // Co-Regional Directors
            List<Map<String, Object>> coRegionalDirectors = new ArrayList<>();
            Map<String, Object> crdData = new HashMap<>();
            crdData.put("id", 4L);
            crdData.put("name", "Co-Regional Director");
            crdData.put("role", "Co-Regional Director");
            crdData.put("location", "West Bengal");
            coRegionalDirectors.add(crdData);
            hierarchy.put("coRegionalDirectors", coRegionalDirectors);
            
            // District Supervisors
            List<Map<String, Object>> districtSupervisors = new ArrayList<>();
            Map<String, Object> dsData = new HashMap<>();
            dsData.put("id", 5L);
            dsData.put("name", "District Supervisor");
            dsData.put("role", "District Supervisor");
            dsData.put("location", "Various Districts");
            districtSupervisors.add(dsData);
            hierarchy.put("districtSupervisors", districtSupervisors);
            
            // Mala Senapotis  
            List<Map<String, Object>> malaSenapotis = new ArrayList<>();
            Map<String, Object> msData = new HashMap<>();
            msData.put("id", 6L);
            msData.put("name", "Mala Senapoti");
            msData.put("role", "Mala Senapoti");
            msData.put("location", "Local Areas");
            malaSenapotis.add(msData);
            hierarchy.put("malaSenapotis", malaSenapotis);
            
            log.debug("Successfully fetched hierarchy with {} levels", hierarchy.size());
            return ResponseEntity.ok(ApiResponse.success(hierarchy));
        } catch (Exception e) {
            log.error("Error fetching hierarchy", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch hierarchy: " + e.getMessage()));
        }
    }
}