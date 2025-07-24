package com.namhatta.controller;

import com.namhatta.dto.ApiResponse;
import com.namhatta.dto.DevotionalStatusDTO;
import com.namhatta.service.DevotionalStatusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/statuses")
@CrossOrigin(origins = "*")
public class DevotionalStatusController {
    
    @Autowired
    private DevotionalStatusService devotionalStatusService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<DevotionalStatusDTO>>> getAllStatuses() {
        try {
            List<DevotionalStatusDTO> statuses = devotionalStatusService.getAllStatuses();
            return ResponseEntity.ok(ApiResponse.success(statuses));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to fetch devotional statuses", e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DevotionalStatusDTO>> getStatusById(@PathVariable Long id) {
        try {
            Optional<DevotionalStatusDTO> status = devotionalStatusService.getStatusById(id);
            if (status.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success(status.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to fetch devotional status", e.getMessage()));
        }
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<DevotionalStatusDTO>> getStatusByName(@PathVariable String name) {
        try {
            Optional<DevotionalStatusDTO> status = devotionalStatusService.getStatusByName(name);
            if (status.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success(status.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to fetch devotional status by name", e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<DevotionalStatusDTO>> createStatus(@Valid @RequestBody DevotionalStatusDTO statusDTO) {
        try {
            DevotionalStatusDTO created = devotionalStatusService.createStatus(statusDTO);
            return ResponseEntity.ok(ApiResponse.success("Devotional status created successfully", created));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to create devotional status", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DevotionalStatusDTO>> updateStatus(@PathVariable Long id, @Valid @RequestBody DevotionalStatusDTO statusDTO) {
        try {
            DevotionalStatusDTO updated = devotionalStatusService.updateStatus(id, statusDTO);
            return ResponseEntity.ok(ApiResponse.success("Devotional status updated successfully", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to update devotional status", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStatus(@PathVariable Long id) {
        try {
            devotionalStatusService.deleteStatus(id);
            return ResponseEntity.ok(ApiResponse.success("Devotional status deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to delete devotional status", e.getMessage()));
        }
    }
}