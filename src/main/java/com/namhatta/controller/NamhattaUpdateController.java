package com.namhatta.controller;

import com.namhatta.dto.ApiResponse;
import com.namhatta.entity.NamhattaUpdate;
import com.namhatta.service.NamhattaUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/namhattas")
@RequiredArgsConstructor
public class NamhattaUpdateController {
    
    private final NamhattaUpdateService updateService;
    
    /**
     * Get updates for a specific namhatta
     * GET /namhattas/{id}/updates
     */
    @GetMapping("/{id}/updates")
    public ResponseEntity<ApiResponse<List<NamhattaUpdate>>> getNamhattaUpdates(@PathVariable Long id) {
        log.info("GET /namhattas/{}/updates - Fetching updates for namhatta", id);
        try {
            List<NamhattaUpdate> updates = updateService.getUpdatesByNamhattaId(id);
            return ResponseEntity.ok(ApiResponse.success(updates));
        } catch (Exception e) {
            log.error("Error fetching namhatta updates", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch updates: " + e.getMessage()));
        }
    }
    
    /**
     * Create update for a specific namhatta
     * POST /namhattas/{id}/updates
     */
    @PostMapping("/{id}/updates")
    public ResponseEntity<ApiResponse<NamhattaUpdate>> createNamhattaUpdate(
            @PathVariable Long id,
            @RequestBody NamhattaUpdate update) {
        log.info("POST /namhattas/{}/updates - Creating update for namhatta", id);
        try {
            update.setNamhattaId(id);
            NamhattaUpdate created = updateService.createUpdate(update);
            return ResponseEntity.ok(ApiResponse.success(created));
        } catch (Exception e) {
            log.error("Error creating namhatta update", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to create update: " + e.getMessage()));
        }
    }
}

@Slf4j
@RestController
@RequestMapping("/updates")
@RequiredArgsConstructor
class UpdateController {
    
    private final NamhattaUpdateService updateService;
    
    /**
     * Get all updates with filtering and pagination
     * GET /updates
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<NamhattaUpdate>>> getAllUpdates(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String programType,
            @RequestParam(required = false) Long namhattaId) {
        log.info("GET /updates - Fetching all updates with pagination");
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<NamhattaUpdate> updates = updateService.getFilteredUpdates(programType, namhattaId, pageable);
            return ResponseEntity.ok(ApiResponse.success(updates));
        } catch (Exception e) {
            log.error("Error fetching updates", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch updates: " + e.getMessage()));
        }
    }
    
    /**
     * Get update by ID
     * GET /updates/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NamhattaUpdate>> getUpdateById(@PathVariable Long id) {
        log.info("GET /updates/{} - Fetching update by ID", id);
        try {
            NamhattaUpdate update = updateService.getUpdateById(id)
                    .orElseThrow(() -> new RuntimeException("Update not found"));
            return ResponseEntity.ok(ApiResponse.success(update));
        } catch (Exception e) {
            log.error("Error fetching update", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch update: " + e.getMessage()));
        }
    }
    
    /**
     * Update existing update
     * PUT /updates/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NamhattaUpdate>> updateUpdate(
            @PathVariable Long id,
            @RequestBody NamhattaUpdate updateData) {
        log.info("PUT /updates/{} - Updating update", id);
        try {
            NamhattaUpdate updated = updateService.updateUpdate(id, updateData);
            return ResponseEntity.ok(ApiResponse.success(updated));
        } catch (Exception e) {
            log.error("Error updating update", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to update: " + e.getMessage()));
        }
    }
    
    /**
     * Delete update
     * DELETE /updates/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUpdate(@PathVariable Long id) {
        log.info("DELETE /updates/{} - Deleting update", id);
        try {
            updateService.deleteUpdate(id);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (Exception e) {
            log.error("Error deleting update", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to delete update: " + e.getMessage()));
        }
    }
}