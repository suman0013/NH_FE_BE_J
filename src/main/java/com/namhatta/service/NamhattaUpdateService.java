package com.namhatta.service;

import com.namhatta.entity.NamhattaUpdate;
import com.namhatta.repository.NamhattaUpdateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NamhattaUpdateService {
    
    private final NamhattaUpdateRepository updateRepository;
    
    public List<NamhattaUpdate> getAllUpdates() {
        log.debug("Fetching all namhatta updates");
        return updateRepository.findAllByOrderByCreatedAtDesc(Pageable.unpaged()).getContent();
    }
    
    public Page<NamhattaUpdate> getUpdatesWithPagination(Pageable pageable) {
        log.debug("Fetching updates with pagination: {}", pageable);
        return updateRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
    
    public List<NamhattaUpdate> getUpdatesByNamhattaId(Long namhattaId) {
        log.debug("Fetching updates for namhatta ID: {}", namhattaId);
        return updateRepository.findByNamhattaIdOrderByCreatedAtDesc(namhattaId);
    }
    
    public Page<NamhattaUpdate> getFilteredUpdates(String programType, Long namhattaId, Pageable pageable) {
        log.debug("Fetching filtered updates - programType: {}, namhattaId: {}", programType, namhattaId);
        return updateRepository.findFilteredUpdates(programType, namhattaId, pageable);
    }
    
    public Optional<NamhattaUpdate> getUpdateById(Long id) {
        log.debug("Fetching update by ID: {}", id);
        return updateRepository.findById(id);
    }
    
    public NamhattaUpdate createUpdate(NamhattaUpdate update) {
        log.info("Creating new namhatta update for namhatta ID: {}", update.getNamhattaId());
        return updateRepository.save(update);
    }
    
    public NamhattaUpdate updateUpdate(Long id, NamhattaUpdate updateData) {
        log.info("Updating namhatta update ID: {}", id);
        return updateRepository.findById(id)
                .map(existing -> {
                    existing.setProgramType(updateData.getProgramType());
                    existing.setDate(updateData.getDate());
                    existing.setAttendance(updateData.getAttendance());
                    existing.setPrasadDistribution(updateData.getPrasadDistribution());
                    existing.setNagarKirtan(updateData.getNagarKirtan());
                    existing.setBookDistribution(updateData.getBookDistribution());
                    existing.setChanting(updateData.getChanting());
                    existing.setArati(updateData.getArati());
                    existing.setBhagwatPath(updateData.getBhagwatPath());
                    existing.setImageUrls(updateData.getImageUrls());
                    existing.setFacebookLink(updateData.getFacebookLink());
                    existing.setYoutubeLink(updateData.getYoutubeLink());
                    existing.setSpecialAttraction(updateData.getSpecialAttraction());
                    return updateRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Update not found with id: " + id));
    }
    
    public void deleteUpdate(Long id) {
        log.info("Deleting namhatta update ID: {}", id);
        updateRepository.deleteById(id);
    }
    
    public Long getRecentUpdatesCount() {
        log.debug("Getting recent updates count");
        try {
            return updateRepository.countRecentUpdates();
        } catch (Exception e) {
            log.warn("Error counting recent updates: {}", e.getMessage());
            return 0L;
        }
    }
}