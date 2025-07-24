package com.namhatta.service;

import com.namhatta.dto.DevotionalStatusDTO;
import com.namhatta.entity.DevotionalStatus;
import com.namhatta.mapper.EntityMapper;
import com.namhatta.repository.DevotionalStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DevotionalStatusService {
    
    @Autowired
    private DevotionalStatusRepository devotionalStatusRepository;
    
    @Autowired
    private EntityMapper entityMapper;
    
    public List<DevotionalStatusDTO> getAllStatuses() {
        List<DevotionalStatus> statuses = devotionalStatusRepository.findByOrderByHierarchyLevelAsc();
        return statuses.stream()
                .map(entityMapper::toDevotionalStatusDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<DevotionalStatusDTO> getStatusById(Long id) {
        Optional<DevotionalStatus> status = devotionalStatusRepository.findById(id);
        return status.map(entityMapper::toDevotionalStatusDTO);
    }
    
    public Optional<DevotionalStatusDTO> getStatusByName(String name) {
        Optional<DevotionalStatus> status = devotionalStatusRepository.findByName(name);
        return status.map(entityMapper::toDevotionalStatusDTO);
    }
    
    public DevotionalStatusDTO createStatus(DevotionalStatusDTO statusDTO) {
        if (devotionalStatusRepository.existsByName(statusDTO.getName())) {
            throw new RuntimeException("Devotional status already exists: " + statusDTO.getName());
        }
        
        DevotionalStatus status = entityMapper.toDevotionalStatusEntity(statusDTO);
        status.setCreatedAt(LocalDateTime.now());
        status.setUpdatedAt(LocalDateTime.now());
        
        DevotionalStatus saved = devotionalStatusRepository.save(status);
        return entityMapper.toDevotionalStatusDTO(saved);
    }
    
    public DevotionalStatusDTO updateStatus(Long id, DevotionalStatusDTO statusDTO) {
        Optional<DevotionalStatus> existingOpt = devotionalStatusRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Devotional status not found with id: " + id);
        }
        
        DevotionalStatus existing = existingOpt.get();
        
        // Check if name is being changed and if new name already exists
        if (!statusDTO.getName().equals(existing.getName()) && 
            devotionalStatusRepository.existsByName(statusDTO.getName())) {
            throw new RuntimeException("Devotional status name already exists: " + statusDTO.getName());
        }
        
        existing.setName(statusDTO.getName());
        existing.setHierarchyLevel(statusDTO.getHierarchyLevel());
        existing.setUpdatedAt(LocalDateTime.now());
        
        DevotionalStatus updated = devotionalStatusRepository.save(existing);
        return entityMapper.toDevotionalStatusDTO(updated);
    }
    
    public void deleteStatus(Long id) {
        if (!devotionalStatusRepository.existsById(id)) {
            throw new RuntimeException("Devotional status not found with id: " + id);
        }
        devotionalStatusRepository.deleteById(id);
    }
}