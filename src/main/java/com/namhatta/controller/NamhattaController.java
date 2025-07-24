package com.namhatta.controller;

import com.namhatta.dto.ApiResponse;
import com.namhatta.dto.NamhattaDTO;
import com.namhatta.service.NamhattaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/namhattas")
@CrossOrigin(origins = "*")
public class NamhattaController {
    
    @Autowired
    private NamhattaService namhattaService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<Page<NamhattaDTO>>> getNamhattas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status) {
        
        try {
            Page<NamhattaDTO> namhattas = namhattaService.getNamhattas(page, size, sortBy, sortDir, search, status);
            return ResponseEntity.ok(ApiResponse.success(namhattas));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to fetch namhattas", e.getMessage()));
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<NamhattaDTO>>> getAllNamhattas() {
        try {
            List<NamhattaDTO> namhattas = namhattaService.getAllNamhattas();
            return ResponseEntity.ok(ApiResponse.success(namhattas));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to fetch all namhattas", e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NamhattaDTO>> getNamhattaById(@PathVariable Long id) {
        try {
            Optional<NamhattaDTO> namhatta = namhattaService.getNamhattaById(id);
            if (namhatta.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success(namhatta.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to fetch namhatta", e.getMessage()));
        }
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<NamhattaDTO>> getNamhattaByCode(@PathVariable String code) {
        try {
            Optional<NamhattaDTO> namhatta = namhattaService.getNamhattaByCode(code);
            if (namhatta.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success(namhatta.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to fetch namhatta by code", e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<NamhattaDTO>> createNamhatta(@Valid @RequestBody NamhattaDTO namhattaDTO) {
        try {
            NamhattaDTO created = namhattaService.createNamhatta(namhattaDTO);
            return ResponseEntity.ok(ApiResponse.success("Namhatta created successfully", created));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to create namhatta", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NamhattaDTO>> updateNamhatta(@PathVariable Long id, @Valid @RequestBody NamhattaDTO namhattaDTO) {
        try {
            NamhattaDTO updated = namhattaService.updateNamhatta(id, namhattaDTO);
            return ResponseEntity.ok(ApiResponse.success("Namhatta updated successfully", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to update namhatta", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNamhatta(@PathVariable Long id) {
        try {
            namhattaService.deleteNamhatta(id);
            return ResponseEntity.ok(ApiResponse.success("Namhatta deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to delete namhatta", e.getMessage()));
        }
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<NamhattaDTO>>> getNamhattasByStatus(@PathVariable String status) {
        try {
            List<NamhattaDTO> namhattas = namhattaService.getNamhattasByStatus(status);
            return ResponseEntity.ok(ApiResponse.success(namhattas));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to fetch namhattas by status", e.getMessage()));
        }
    }
    
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getTotalNamhattas() {
        try {
            long count = namhattaService.getTotalNamhattas();
            return ResponseEntity.ok(ApiResponse.success(count));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to get namhatta count", e.getMessage()));
        }
    }
}