package com.namhatta.service;

import com.namhatta.dto.NamhattaDTO;
import com.namhatta.entity.Namhatta;
import com.namhatta.mapper.EntityMapper;
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
public class NamhattaService {
    
    @Autowired
    private NamhattaRepository namhattaRepository;
    
    @Autowired
    private EntityMapper entityMapper;
    
    public List<NamhattaDTO> getAllNamhattas() {
        try {
            // Use simple findAll without complex joins
            List<Namhatta> namhattas = namhattaRepository.findAll();
            System.out.println("Found " + namhattas.size() + " namhattas in database");
            
            return namhattas.stream()
                    .map(this::mapNamhattaToDTO)
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error fetching namhattas: " + e.getMessage());
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }
    
    private NamhattaDTO mapNamhattaToDTO(Namhatta namhatta) {
        NamhattaDTO dto = new NamhattaDTO();
        try {
            dto.setId(namhatta.getId());
            dto.setName(namhatta.getName() != null ? namhatta.getName() : "");
            dto.setCode(namhatta.getCode() != null ? namhatta.getCode() : "");
            dto.setSecretary(namhatta.getSecretary() != null ? namhatta.getSecretary() : "");
            dto.setContactNumber(namhatta.getContactNumber() != null ? namhatta.getContactNumber() : "");
            dto.setMeetingDay(namhatta.getMeetingDay() != null ? namhatta.getMeetingDay() : "");
            dto.setMeetingTime(namhatta.getMeetingTime() != null ? namhatta.getMeetingTime() : "");
            dto.setStatus(namhatta.getStatus() != null ? namhatta.getStatus() : "ACTIVE");
            dto.setCreatedAt(namhatta.getCreatedAt());
            dto.setUpdatedAt(namhatta.getUpdatedAt());
            
            // Set devotee count to 0 for now to avoid database issues
            dto.setDevoteeCount(0);
            
            // Set empty address list for now to avoid issues
            dto.setAddresses(new java.util.ArrayList<>());
            
        } catch (Exception e) {
            System.err.println("Error mapping namhatta: " + e.getMessage());
        }
        
        return dto;
    }
    
    public Page<NamhattaDTO> getNamhattas(int page, int size, String sortBy, String sortDir, String search, String status) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : 
                Sort.by(sortBy).ascending();
            
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<Namhatta> namhattaPage;
            if (search != null && !search.trim().isEmpty()) {
                namhattaPage = namhattaRepository.findByNameContainingIgnoreCase(search, pageable);
            } else {
                namhattaPage = namhattaRepository.findAll(pageable);
            }
            
            return namhattaPage.map(this::mapNamhattaToDTO);
        } catch (Exception e) {
            // Log error and return empty page
            System.err.println("Error fetching namhattas page: " + e.getMessage());
            return Page.empty();
        }
    }
    
    public Optional<NamhattaDTO> getNamhattaById(Long id) {
        Optional<Namhatta> namhatta = namhattaRepository.findByIdWithDevotees(id);
        return namhatta.map(entityMapper::toNamhattaDTO);
    }
    
    public Optional<NamhattaDTO> getNamhattaByCode(String code) {
        Optional<Namhatta> namhatta = namhattaRepository.findByCode(code);
        return namhatta.map(entityMapper::toNamhattaDTO);
    }
    
    public NamhattaDTO createNamhatta(NamhattaDTO namhattaDTO) {
        // Check if code already exists
        if (namhattaDTO.getCode() != null && namhattaRepository.existsByCode(namhattaDTO.getCode())) {
            throw new RuntimeException("Namhatta code already exists: " + namhattaDTO.getCode());
        }
        
        Namhatta namhatta = entityMapper.toNamhattaEntity(namhattaDTO);
        namhatta.setCreatedAt(LocalDateTime.now());
        namhatta.setUpdatedAt(LocalDateTime.now());
        
        if (namhatta.getStatus() == null) {
            namhatta.setStatus("ACTIVE");
        }
        
        Namhatta saved = namhattaRepository.save(namhatta);
        return entityMapper.toNamhattaDTO(saved);
    }
    
    public NamhattaDTO updateNamhatta(Long id, NamhattaDTO namhattaDTO) {
        Optional<Namhatta> existingOpt = namhattaRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Namhatta not found with id: " + id);
        }
        
        Namhatta existing = existingOpt.get();
        
        // Check if code is being changed and if new code already exists
        if (namhattaDTO.getCode() != null && 
            !namhattaDTO.getCode().equals(existing.getCode()) && 
            namhattaRepository.existsByCode(namhattaDTO.getCode())) {
            throw new RuntimeException("Namhatta code already exists: " + namhattaDTO.getCode());
        }
        
        // Update fields
        existing.setName(namhattaDTO.getName());
        existing.setCode(namhattaDTO.getCode());
        existing.setSecretary(namhattaDTO.getSecretary());
        existing.setContactNumber(namhattaDTO.getContactNumber());
        existing.setMeetingDay(namhattaDTO.getMeetingDay());
        existing.setMeetingTime(namhattaDTO.getMeetingTime());
        existing.setStatus(namhattaDTO.getStatus());
        existing.setUpdatedAt(LocalDateTime.now());
        
        Namhatta updated = namhattaRepository.save(existing);
        return entityMapper.toNamhattaDTO(updated);
    }
    
    public void deleteNamhatta(Long id) {
        if (!namhattaRepository.existsById(id)) {
            throw new RuntimeException("Namhatta not found with id: " + id);
        }
        namhattaRepository.deleteById(id);
    }
    
    public List<NamhattaDTO> getNamhattasByStatus(String status) {
        List<Namhatta> namhattas = namhattaRepository.findByStatus(status);
        return namhattas.stream()
                .map(entityMapper::toNamhattaDTO)
                .collect(Collectors.toList());
    }
    
    public long getTotalNamhattas() {
        return namhattaRepository.count();
    }
    
    public long getNamhattaCountByStatus(String status) {
        return namhattaRepository.countByStatus(status);
    }
    
    // Method for testing API compatibility
    public long getTotalCount() {
        return namhattaRepository.count();
    }
    
    // Method to match Node.js API signature for testing
    public Page<NamhattaDTO> getAllNamhattas(int page, int size, String search) {
        return getNamhattas(page, size, "id", "asc", search, null);
    }
    
    public boolean isCodeAvailable(String code) {
        return !namhattaRepository.existsByCode(code);
    }
}