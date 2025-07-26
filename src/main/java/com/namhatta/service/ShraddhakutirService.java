package com.namhatta.service;

import com.namhatta.entity.Shraddhakutir;
import com.namhatta.repository.ShraddhakutirRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShraddhakutirService {
    
    private final ShraddhakutirRepository shraddhakutirRepository;
    
    public List<Shraddhakutir> getAllShraddhakutirs() {
        log.debug("Fetching all shraddhakutirs");
        return shraddhakutirRepository.findAll();
    }
    
    public List<Shraddhakutir> getShraddhakutirsByDistrictCode(String districtCode) {
        log.debug("Fetching shraddhakutirs by district code: {}", districtCode);
        return shraddhakutirRepository.findByDistrictCodeOptional(districtCode);
    }
    
    public Optional<Shraddhakutir> getShraddhakutirById(Long id) {
        log.debug("Fetching shraddhakutir by ID: {}", id);
        return shraddhakutirRepository.findById(id);
    }
    
    public Shraddhakutir createShraddhakutir(Shraddhakutir shraddhakutir) {
        log.info("Creating new shraddhakutir: {}", shraddhakutir.getName());
        return shraddhakutirRepository.save(shraddhakutir);
    }
    
    public Shraddhakutir updateShraddhakutir(Long id, Shraddhakutir shraddhakutirData) {
        log.info("Updating shraddhakutir ID: {}", id);
        return shraddhakutirRepository.findById(id)
                .map(existing -> {
                    existing.setName(shraddhakutirData.getName());
                    existing.setDistrictCode(shraddhakutirData.getDistrictCode());
                    return shraddhakutirRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Shraddhakutir not found with id: " + id));
    }
    
    public void deleteShraddhakutir(Long id) {
        log.info("Deleting shraddhakutir ID: {}", id);
        shraddhakutirRepository.deleteById(id);
    }
    
    public List<String> getAllDistrictCodes() {
        log.debug("Fetching all district codes");
        return shraddhakutirRepository.findAllDistrictCodes();
    }
}