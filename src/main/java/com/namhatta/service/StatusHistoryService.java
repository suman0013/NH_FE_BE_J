package com.namhatta.service;

import com.namhatta.entity.StatusHistory;
import com.namhatta.repository.StatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatusHistoryService {
    
    private final StatusHistoryRepository statusHistoryRepository;
    
    public List<StatusHistory> getAllStatusHistory() {
        log.debug("Fetching all status history records");
        return statusHistoryRepository.findAll();
    }
    
    public List<StatusHistory> getDevoteeStatusHistory(Long devoteeId) {
        log.debug("Fetching status history for devotee ID: {}", devoteeId);
        return statusHistoryRepository.findDevoteeStatusHistory(devoteeId);
    }
    
    public Optional<StatusHistory> getStatusHistoryById(Long id) {
        log.debug("Fetching status history by ID: {}", id);
        return statusHistoryRepository.findById(id);
    }
    
    public StatusHistory createStatusHistory(StatusHistory statusHistory) {
        log.info("Creating status history record for devotee ID: {}", statusHistory.getDevoteeId());
        return statusHistoryRepository.save(statusHistory);
    }
    
    public StatusHistory recordStatusChange(Long devoteeId, String previousStatus, String newStatus, String comment) {
        log.info("Recording status change for devotee {}: {} -> {}", devoteeId, previousStatus, newStatus);
        
        StatusHistory history = new StatusHistory();
        history.setDevoteeId(devoteeId);
        history.setPreviousStatus(previousStatus);
        history.setNewStatus(newStatus);
        history.setComment(comment);
        
        return statusHistoryRepository.save(history);
    }
    
    public Long getRecentStatusChangesCount() {
        log.debug("Getting recent status changes count");
        try {
            return statusHistoryRepository.countRecentStatusChanges();
        } catch (Exception e) {
            log.warn("Error counting recent status changes: {}", e.getMessage());
            return 0L;
        }
    }
    
    public List<Object[]> getRecentStatusDistribution() {
        log.debug("Getting recent status distribution");
        try {
            return statusHistoryRepository.getRecentStatusDistribution();
        } catch (Exception e) {
            log.warn("Error getting recent status distribution: {}", e.getMessage());
            return List.of();
        }
    }
}