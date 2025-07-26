package com.namhatta.controller;

import com.namhatta.dto.ApiResponse;
import com.namhatta.entity.StatusHistory;
import com.namhatta.service.StatusHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/devotees")
@RequiredArgsConstructor
public class StatusHistoryController {
    
    private final StatusHistoryService statusHistoryService;
    
    /**
     * Get status history for a devotee
     * GET /devotees/{devoteeId}/status-history
     */
    @GetMapping("/{devoteeId}/status-history")
    public ResponseEntity<ApiResponse<List<StatusHistory>>> getDevoteeStatusHistory(@PathVariable Long devoteeId) {
        log.info("GET /devotees/{}/status-history - Fetching status history", devoteeId);
        try {
            List<StatusHistory> history = statusHistoryService.getDevoteeStatusHistory(devoteeId);
            return ResponseEntity.ok(ApiResponse.success(history));
        } catch (Exception e) {
            log.error("Error fetching devotee status history", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch status history: " + e.getMessage()));
        }
    }
    
    /**
     * Record status change for a devotee
     * POST /devotees/{devoteeId}/status-change
     */
    @PostMapping("/{devoteeId}/status-change")
    public ResponseEntity<ApiResponse<StatusHistory>> recordStatusChange(
            @PathVariable Long devoteeId,
            @RequestBody StatusChangeRequest request) {
        log.info("POST /devotees/{}/status-change - Recording status change", devoteeId);
        try {
            StatusHistory history = statusHistoryService.recordStatusChange(
                    devoteeId, 
                    request.getPreviousStatus(), 
                    request.getNewStatus(), 
                    request.getComment()
            );
            return ResponseEntity.ok(ApiResponse.success(history));
        } catch (Exception e) {
            log.error("Error recording status change", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to record status change: " + e.getMessage()));
        }
    }
    
    /**
     * Inner class for status change request
     */
    public static class StatusChangeRequest {
        private String previousStatus;
        private String newStatus;
        private String comment;
        
        // Getters and setters
        public String getPreviousStatus() { return previousStatus; }
        public void setPreviousStatus(String previousStatus) { this.previousStatus = previousStatus; }
        
        public String getNewStatus() { return newStatus; }
        public void setNewStatus(String newStatus) { this.newStatus = newStatus; }
        
        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
    }
}

@Slf4j
@RestController
@RequestMapping("/status-history")
@RequiredArgsConstructor
class StatusHistoryGlobalController {
    
    private final StatusHistoryService statusHistoryService;
    
    /**
     * Get all status history records
     * GET /status-history
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<StatusHistory>>> getAllStatusHistory() {
        log.info("GET /status-history - Fetching all status history");
        try {
            List<StatusHistory> history = statusHistoryService.getAllStatusHistory();
            return ResponseEntity.ok(ApiResponse.success(history));
        } catch (Exception e) {
            log.error("Error fetching all status history", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch status history: " + e.getMessage()));
        }
    }
}