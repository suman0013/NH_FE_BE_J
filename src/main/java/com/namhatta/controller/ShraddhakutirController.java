package com.namhatta.controller;

import com.namhatta.dto.ApiResponse;
import com.namhatta.entity.Shraddhakutir;
import com.namhatta.service.ShraddhakutirService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shraddhakutirs")
@RequiredArgsConstructor
public class ShraddhakutirController {
    
    private final ShraddhakutirService shraddhakutirService;
    
    /**
     * Get all shraddhakutirs
     * GET /shraddhakutirs
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Shraddhakutir>>> getAllShraddhakutirs(
            @RequestParam(required = false) String districtCode) {
        log.info("GET /shraddhakutirs - Fetching shraddhakutirs with districtCode: {}", districtCode);
        try {
            List<Shraddhakutir> shraddhakutirs;
            if (districtCode != null && !districtCode.trim().isEmpty()) {
                shraddhakutirs = shraddhakutirService.getShraddhakutirsByDistrictCode(districtCode);
            } else {
                shraddhakutirs = shraddhakutirService.getAllShraddhakutirs();
            }
            return ResponseEntity.ok(ApiResponse.success(shraddhakutirs));
        } catch (Exception e) {
            log.error("Error fetching shraddhakutirs", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch shraddhakutirs: " + e.getMessage()));
        }
    }
    
    /**
     * Get shraddhakutir by ID
     * GET /shraddhakutirs/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Shraddhakutir>> getShraddhakutirById(@PathVariable Long id) {
        log.info("GET /shraddhakutirs/{} - Fetching shraddhakutir by ID", id);
        try {
            Shraddhakutir shraddhakutir = shraddhakutirService.getShraddhakutirById(id)
                    .orElseThrow(() -> new RuntimeException("Shraddhakutir not found"));
            return ResponseEntity.ok(ApiResponse.success(shraddhakutir));
        } catch (Exception e) {
            log.error("Error fetching shraddhakutir", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch shraddhakutir: " + e.getMessage()));
        }
    }
    
    /**
     * Create new shraddhakutir
     * POST /shraddhakutirs
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Shraddhakutir>> createShraddhakutir(@RequestBody Shraddhakutir shraddhakutir) {
        log.info("POST /shraddhakutirs - Creating new shraddhakutir: {}", shraddhakutir.getName());
        try {
            Shraddhakutir created = shraddhakutirService.createShraddhakutir(shraddhakutir);
            return ResponseEntity.ok(ApiResponse.success(created));
        } catch (Exception e) {
            log.error("Error creating shraddhakutir", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to create shraddhakutir: " + e.getMessage()));
        }
    }
    
    /**
     * Update shraddhakutir
     * PUT /shraddhakutirs/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Shraddhakutir>> updateShraddhakutir(
            @PathVariable Long id, 
            @RequestBody Shraddhakutir shraddhakutir) {
        log.info("PUT /shraddhakutirs/{} - Updating shraddhakutir", id);
        try {
            Shraddhakutir updated = shraddhakutirService.updateShraddhakutir(id, shraddhakutir);
            return ResponseEntity.ok(ApiResponse.success(updated));
        } catch (Exception e) {
            log.error("Error updating shraddhakutir", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to update shraddhakutir: " + e.getMessage()));
        }
    }
    
    /**
     * Delete shraddhakutir
     * DELETE /shraddhakutirs/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteShraddhakutir(@PathVariable Long id) {
        log.info("DELETE /shraddhakutirs/{} - Deleting shraddhakutir", id);
        try {
            shraddhakutirService.deleteShraddhakutir(id);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (Exception e) {
            log.error("Error deleting shraddhakutir", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to delete shraddhakutir: " + e.getMessage()));
        }
    }
    
    /**
     * Get all district codes
     * GET /shraddhakutirs/district-codes
     */
    @GetMapping("/district-codes")
    public ResponseEntity<ApiResponse<List<String>>> getAllDistrictCodes() {
        log.info("GET /shraddhakutirs/district-codes - Fetching all district codes");
        try {
            List<String> districtCodes = shraddhakutirService.getAllDistrictCodes();
            return ResponseEntity.ok(ApiResponse.success(districtCodes));
        } catch (Exception e) {
            log.error("Error fetching district codes", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch district codes: " + e.getMessage()));
        }
    }
}