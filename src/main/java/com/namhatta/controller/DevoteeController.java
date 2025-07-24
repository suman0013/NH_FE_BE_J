package com.namhatta.controller;

import com.namhatta.dto.ApiResponse;
import com.namhatta.dto.DevoteeDTO;
import com.namhatta.service.DevoteeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/devotees")
@CrossOrigin(origins = "*")
public class DevoteeController {
    
    @Autowired
    private DevoteeService devoteeService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<Page<DevoteeDTO>>> getDevotees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "legalName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search) {
        
        try {
            Page<DevoteeDTO> devotees = devoteeService.getDevotees(page, size, sortBy, sortDir, search);
            return ResponseEntity.ok(ApiResponse.success(devotees));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to fetch devotees", e.getMessage()));
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<DevoteeDTO>>> getAllDevotees() {
        try {
            List<DevoteeDTO> devotees = devoteeService.getAllDevotees();
            return ResponseEntity.ok(ApiResponse.success(devotees));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to fetch all devotees", e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DevoteeDTO>> getDevoteeById(@PathVariable Long id) {
        try {
            Optional<DevoteeDTO> devotee = devoteeService.getDevoteeById(id);
            if (devotee.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success(devotee.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to fetch devotee", e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<DevoteeDTO>> createDevotee(@Valid @RequestBody DevoteeDTO devoteeDTO) {
        try {
            DevoteeDTO created = devoteeService.createDevotee(devoteeDTO);
            return ResponseEntity.ok(ApiResponse.success("Devotee created successfully", created));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to create devotee", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DevoteeDTO>> updateDevotee(@PathVariable Long id, @Valid @RequestBody DevoteeDTO devoteeDTO) {
        try {
            DevoteeDTO updated = devoteeService.updateDevotee(id, devoteeDTO);
            return ResponseEntity.ok(ApiResponse.success("Devotee updated successfully", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to update devotee", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDevotee(@PathVariable Long id) {
        try {
            devoteeService.deleteDevotee(id);
            return ResponseEntity.ok(ApiResponse.success("Devotee deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to delete devotee", e.getMessage()));
        }
    }
    
    @GetMapping("/namhatta/{namhattaId}")
    public ResponseEntity<ApiResponse<List<DevoteeDTO>>> getDevoteesByNamhatta(@PathVariable Long namhattaId) {
        try {
            List<DevoteeDTO> devotees = devoteeService.getDevoteesByNamhatta(namhattaId);
            return ResponseEntity.ok(ApiResponse.success(devotees));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to fetch devotees by namhatta", e.getMessage()));
        }
    }
    
    @GetMapping("/status/{statusId}")
    public ResponseEntity<ApiResponse<List<DevoteeDTO>>> getDevoteesByStatus(@PathVariable Long statusId) {
        try {
            List<DevoteeDTO> devotees = devoteeService.getDevoteesByStatus(statusId);
            return ResponseEntity.ok(ApiResponse.success(devotees));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to fetch devotees by status", e.getMessage()));
        }
    }
    
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getTotalDevotees() {
        try {
            long count = devoteeService.getTotalDevotees();
            return ResponseEntity.ok(ApiResponse.success(count));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to get devotee count", e.getMessage()));
        }
    }
}