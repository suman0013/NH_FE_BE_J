package com.namhatta.service;

import com.namhatta.dto.DevoteeDTO;
import com.namhatta.entity.Devotee;
import com.namhatta.entity.DevotionalStatus;
import com.namhatta.entity.Namhatta;
import com.namhatta.mapper.EntityMapper;
import com.namhatta.repository.DevoteeRepository;
import com.namhatta.repository.DevotionalStatusRepository;
import com.namhatta.repository.NamhattaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DevoteeService {
    
    @Autowired
    private DevoteeRepository devoteeRepository;
    
    @Autowired
    private DevotionalStatusRepository devotionalStatusRepository;
    
    @Autowired
    private NamhattaRepository namhattaRepository;
    
    @Autowired
    private EntityMapper entityMapper;
    
    public List<DevoteeDTO> getAllDevotees() {
        List<Devotee> devotees = devoteeRepository.findAll();
        return devotees.stream()
                .map(entityMapper::toDevoteeDTO)
                .collect(Collectors.toList());
    }
    
    public Page<DevoteeDTO> getDevotees(int page, int size, String sortBy, String sortDir, String search) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : 
            Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Devotee> devoteePage;
        if (search != null && !search.trim().isEmpty()) {
            devoteePage = devoteeRepository.findByLegalNameContainingIgnoreCase(search, pageable);
        } else {
            devoteePage = devoteeRepository.findAll(pageable);
        }
        
        return devoteePage.map(entityMapper::toDevoteeDTO);
    }
    
    public Optional<DevoteeDTO> getDevoteeById(Long id) {
        Optional<Devotee> devotee = devoteeRepository.findByIdWithDetails(id);
        return devotee.map(entityMapper::toDevoteeDTO);
    }
    
    public DevoteeDTO createDevotee(DevoteeDTO devoteeDTO) {
        Devotee devotee = entityMapper.toDevoteeEntity(devoteeDTO);
        
        // Set relationships
        if (devoteeDTO.getDevotionalStatusId() != null) {
            Optional<DevotionalStatus> status = devotionalStatusRepository.findById(devoteeDTO.getDevotionalStatusId());
            status.ifPresent(devotee::setDevotionalStatus);
        }
        
        if (devoteeDTO.getNamhattaId() != null) {
            Optional<Namhatta> namhatta = namhattaRepository.findById(devoteeDTO.getNamhattaId());
            namhatta.ifPresent(devotee::setNamhatta);
        }
        
        devotee.setCreatedAt(LocalDateTime.now());
        devotee.setUpdatedAt(LocalDateTime.now());
        
        Devotee saved = devoteeRepository.save(devotee);
        return entityMapper.toDevoteeDTO(saved);
    }
    
    public DevoteeDTO updateDevotee(Long id, DevoteeDTO devoteeDTO) {
        Optional<Devotee> existingOpt = devoteeRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Devotee not found with id: " + id);
        }
        
        Devotee existing = existingOpt.get();
        
        // Update fields
        existing.setLegalName(devoteeDTO.getLegalName());
        existing.setSpiritualName(devoteeDTO.getSpiritualName());
        existing.setDateOfBirth(devoteeDTO.getDateOfBirth());
        existing.setGender(devoteeDTO.getGender());
        existing.setPhoneNumber(devoteeDTO.getPhoneNumber());
        existing.setEmail(devoteeDTO.getEmail());
        existing.setWhatsappNumber(devoteeDTO.getWhatsappNumber());
        existing.setHarinameDate(devoteeDTO.getHarinameDate());
        existing.setDikshaDate(devoteeDTO.getDikshaDate());
        existing.setPancharatrikDate(devoteeDTO.getPancharatrikDate());
        existing.setGuruMaharaja(devoteeDTO.getGuruMaharaja());
        existing.setEducation(devoteeDTO.getEducation());
        existing.setOccupation(devoteeDTO.getOccupation());
        existing.setDevotionalCourses(devoteeDTO.getDevotionalCourses());
        existing.setUpdatedAt(LocalDateTime.now());
        
        // Update relationships
        if (devoteeDTO.getDevotionalStatusId() != null) {
            Optional<DevotionalStatus> status = devotionalStatusRepository.findById(devoteeDTO.getDevotionalStatusId());
            status.ifPresent(existing::setDevotionalStatus);
        }
        
        if (devoteeDTO.getNamhattaId() != null) {
            Optional<Namhatta> namhatta = namhattaRepository.findById(devoteeDTO.getNamhattaId());
            namhatta.ifPresent(existing::setNamhatta);
        }
        
        Devotee updated = devoteeRepository.save(existing);
        return entityMapper.toDevoteeDTO(updated);
    }
    
    public void deleteDevotee(Long id) {
        if (!devoteeRepository.existsById(id)) {
            throw new RuntimeException("Devotee not found with id: " + id);
        }
        devoteeRepository.deleteById(id);
    }
    
    public List<DevoteeDTO> getDevoteesByNamhatta(Long namhattaId) {
        List<Devotee> devotees = devoteeRepository.findByNamhattaId(namhattaId);
        return devotees.stream()
                .map(entityMapper::toDevoteeDTO)
                .collect(Collectors.toList());
    }
    
    public List<DevoteeDTO> getDevoteesByStatus(Long statusId) {
        List<Devotee> devotees = devoteeRepository.findByDevotionalStatusId(statusId);
        return devotees.stream()
                .map(entityMapper::toDevoteeDTO)
                .collect(Collectors.toList());
    }
    
    public long getTotalDevotees() {
        return devoteeRepository.count();
    }
    
    public long getDevoteeCountByNamhatta(Long namhattaId) {
        return devoteeRepository.countByNamhattaId(namhattaId);
    }
    
    // Method for testing API compatibility
    public long getTotalCount() {
        return devoteeRepository.count();
    }
    
    // Method to match Node.js API signature for testing
    public Page<DevoteeDTO> getAllDevotees(int page, int size, String search) {
        return getDevotees(page, size, "id", "asc", search);
    }
}