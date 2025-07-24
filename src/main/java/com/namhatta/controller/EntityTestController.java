package com.namhatta.controller;

import com.namhatta.entity.*;
import com.namhatta.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/entity-test")
public class EntityTestController {

    @Autowired private DevoteeRepository devoteeRepository;
    @Autowired private NamhattaRepository namhattaRepository;  
    @Autowired private UserRepository userRepository;
    @Autowired private DevotionalStatusRepository devotionalStatusRepository;
    @Autowired private AddressRepository addressRepository;

    @GetMapping("/entities")
    public Map<String, Object> testEntities() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Test devotee entity
            List<Devotee> devotees = devoteeRepository.findAll();
            result.put("devotees_count", devotees.size());
            if (!devotees.isEmpty()) {
                Devotee first = devotees.get(0);
                Map<String, Object> devoteeInfo = new HashMap<>();
                devoteeInfo.put("id", first.getId());
                devoteeInfo.put("legalName", first.getLegalName());
                devoteeInfo.put("spiritualName", first.getSpiritualName());
                devoteeInfo.put("namhatta", first.getNamhatta() != null ? first.getNamhatta().getName() : null);
                devoteeInfo.put("status", first.getDevotionalStatus() != null ? first.getDevotionalStatus().getName() : null);
                devoteeInfo.put("addresses_count", first.getAddresses() != null ? first.getAddresses().size() : 0);
                result.put("devotee_sample", devoteeInfo);
            }
            
            // Test namhatta entity
            List<Namhatta> namhattas = namhattaRepository.findAll();
            result.put("namhattas_count", namhattas.size());
            if (!namhattas.isEmpty()) {
                Namhatta first = namhattas.get(0);
                Map<String, Object> namhattaInfo = new HashMap<>();
                namhattaInfo.put("id", first.getId());
                namhattaInfo.put("name", first.getName());
                namhattaInfo.put("code", first.getCode());
                namhattaInfo.put("secretary", first.getSecretary());
                result.put("namhatta_sample", namhattaInfo);
            }
            
            // Test user entity  
            List<User> users = userRepository.findAll();
            result.put("users_count", users.size());
            if (!users.isEmpty()) {
                User first = users.get(0);
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("id", first.getId());
                userInfo.put("username", first.getUsername());
                userInfo.put("role", first.getRole());
                userInfo.put("active", first.getActive());
                result.put("user_sample", userInfo);
            }
            
            // Test devotional status entity
            List<DevotionalStatus> statuses = devotionalStatusRepository.findAll();
            result.put("devotional_statuses_count", statuses.size());
            
            // Test address entity
            List<Address> addresses = addressRepository.findAll();
            result.put("addresses_count", addresses.size());
            
            result.put("status", "SUCCESS");
            
        } catch (Exception e) {
            result.put("status", "ERROR");
            result.put("error", e.getMessage());
            result.put("error_class", e.getClass().getSimpleName());
        }
        
        return result;
    }
    
    @GetMapping("/counts")
    public Map<String, Object> getCounts() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            result.put("devotees", devoteeRepository.count());
            result.put("namhattas", namhattaRepository.count());
            result.put("users", userRepository.count());
            result.put("devotional_statuses", devotionalStatusRepository.count());
            result.put("addresses", addressRepository.count());
            result.put("status", "SUCCESS");
        } catch (Exception e) {
            result.put("status", "ERROR");
            result.put("error", e.getMessage());
        }
        
        return result;
    }
}