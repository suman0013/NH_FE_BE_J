package com.namhatta.service;

import com.namhatta.entity.Address;
import com.namhatta.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Geography Service Implementation
 * 
 * Provides geography-related operations to match Node.js backend functionality.
 * Handles countries, states, districts, sub-districts, villages, and pincode operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeographyService {

    private final AddressRepository addressRepository;

    /**
     * Get all countries from address data
     */
    public List<String> getAllCountries() {
        log.debug("Fetching all countries");
        return addressRepository.findDistinctCountries();
    }

    /**
     * Get states by country
     */
    public List<String> getStatesByCountry(String country) {
        log.debug("Fetching states for country: {}", country);
        if (country != null && !country.trim().isEmpty()) {
            return addressRepository.findDistinctStatesByCountry(country);
        }
        return addressRepository.findDistinctStates();
    }

    /**
     * Get districts by state
     */
    public List<String> getDistrictsByState(String state) {
        log.debug("Fetching districts for state: {}", state);
        if (state != null && !state.trim().isEmpty()) {
            return addressRepository.findDistinctDistrictsByState(state);
        }
        return addressRepository.findDistinctDistricts();
    }

    /**
     * Get sub-districts by district and optionally by pincode
     */
    public List<String> getSubDistrictsByDistrict(String district, String pincode) {
        log.debug("Fetching sub-districts for district: {}, pincode: {}", district, pincode);
        
        if (pincode != null && !pincode.trim().isEmpty()) {
            if (district != null && !district.trim().isEmpty()) {
                return addressRepository.findDistinctSubDistrictsByDistrictAndPincode(district, pincode);
            } else {
                return addressRepository.findDistinctSubDistrictsByPincode(pincode);
            }
        } else if (district != null && !district.trim().isEmpty()) {
            return addressRepository.findDistinctSubDistrictsByDistrict(district);
        }
        
        return addressRepository.findDistinctSubDistricts();
    }

    /**
     * Get villages by sub-district and optionally by pincode
     */
    public List<String> getVillagesBySubDistrict(String subDistrict, String pincode) {
        log.debug("Fetching villages for sub-district: {}, pincode: {}", subDistrict, pincode);
        
        if (pincode != null && !pincode.trim().isEmpty()) {
            if (subDistrict != null && !subDistrict.trim().isEmpty()) {
                return addressRepository.findDistinctVillagesBySubDistrictAndPincode(subDistrict, pincode);
            } else {
                return addressRepository.findDistinctVillagesByPincode(pincode);
            }
        } else if (subDistrict != null && !subDistrict.trim().isEmpty()) {
            return addressRepository.findDistinctVillagesBySubDistrict(subDistrict);
        }
        
        return addressRepository.findDistinctVillages();
    }

    /**
     * Get pincodes with optional filtering
     */
    public List<String> getPincodes(String village, String district, String subDistrict) {
        log.debug("Fetching pincodes for village: {}, district: {}, sub-district: {}", 
                 village, district, subDistrict);
        
        if (village != null && !village.trim().isEmpty()) {
            return addressRepository.findDistinctPincodesByVillage(village);
        } else if (subDistrict != null && !subDistrict.trim().isEmpty()) {
            return addressRepository.findDistinctPincodesBySubDistrict(subDistrict);
        } else if (district != null && !district.trim().isEmpty()) {
            return addressRepository.findDistinctPincodesByDistrict(district);
        }
        
        return addressRepository.findDistinctPincodes();
    }

    /**
     * Search pincodes with pagination
     */
    public Map<String, Object> searchPincodes(String country, String searchTerm, int page, int limit) {
        log.debug("Searching pincodes for country: {}, term: {}, page: {}, limit: {}", 
                 country, searchTerm, page, limit);
        
        Pageable pageable = PageRequest.of(page, limit);
        Page<String> pincodeePage;
        
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            if (country != null && !country.trim().isEmpty()) {
                pincodeePage = addressRepository.searchPincodesByCountryAndTerm(country, searchTerm, pageable);
            } else {
                pincodeePage = addressRepository.searchPincodesByTerm(searchTerm, pageable);
            }
        } else {
            if (country != null && !country.trim().isEmpty()) {
                pincodeePage = addressRepository.findPincodesByCountry(country, pageable);
            } else {
                pincodeePage = addressRepository.findAllPincodes(pageable);
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("pincodes", pincodeePage.getContent());
        result.put("total", pincodeePage.getTotalElements());
        result.put("hasMore", pincodeePage.hasNext());
        
        return result;
    }

    /**
     * Get address information by pincode
     */
    public Map<String, Object> getAddressByPincode(String pincode) {
        log.debug("Fetching address info for pincode: {}", pincode);
        
        if (pincode == null || pincode.trim().isEmpty()) {
            return null;
        }
        
        List<Address> addresses = addressRepository.findByPincode(pincode);
        if (addresses.isEmpty()) {
            return null;
        }
        
        // Get the first address for basic info
        Address firstAddress = addresses.get(0);
        
        // Collect all unique sub-districts and villages for this pincode
        Set<String> subDistricts = addresses.stream()
            .map(Address::getSubdistrictNameEnglish)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
            
        Set<String> villages = addresses.stream()
            .map(Address::getVillageNameEnglish)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        
        Map<String, Object> result = new HashMap<>();
        result.put("country", firstAddress.getCountry());
        result.put("state", firstAddress.getStateNameEnglish());
        result.put("district", firstAddress.getDistrictNameEnglish());
        result.put("subDistricts", new ArrayList<>(subDistricts));
        result.put("villages", new ArrayList<>(villages));
        
        return result;
    }

    /**
     * Get country-wise namhatta counts for map visualization
     */
    public List<Map<String, Object>> getCountryMapData() {
        log.debug("Fetching country map data");
        try {
            List<Object[]> results = addressRepository.getCountryWiseNamhattaCounts();
            return results.stream()
                    .map(row -> Map.of(
                            "name", row[0] != null ? row[0].toString() : "Unknown",
                            "count", row[1] != null ? ((Number) row[1]).intValue() : 0,
                            "coordinates", Arrays.asList(0.0, 0.0) // Default coordinates
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching country map data", e);
            return List.of();
        }
    }
    
    /**
     * Get state-wise namhatta counts for map visualization
     */
    public List<Map<String, Object>> getStateMapData(String country) {
        log.debug("Fetching state map data for country: {}", country);
        try {
            List<Object[]> results = addressRepository.getStateWiseNamhattaCounts(country);
            return results.stream()
                    .map(row -> Map.of(
                            "name", row[0] != null ? row[0].toString() : "Unknown",
                            "count", row[1] != null ? ((Number) row[1]).intValue() : 0,
                            "coordinates", Arrays.asList(0.0, 0.0) // Default coordinates
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching state map data", e);
            return List.of();
        }
    }
    
    /**
     * Get district-wise namhatta counts for map visualization
     */
    public List<Map<String, Object>> getDistrictMapData(String state) {
        log.debug("Fetching district map data for state: {}", state);
        try {
            List<Object[]> results = addressRepository.getDistrictWiseNamhattaCounts(state);
            return results.stream()
                    .map(row -> Map.of(
                            "name", row[0] != null ? row[0].toString() : "Unknown",
                            "count", row[1] != null ? ((Number) row[1]).intValue() : 0,
                            "coordinates", Arrays.asList(0.0, 0.0) // Default coordinates
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching district map data", e);
            return List.of();
        }
    }
    
    /**
     * Get sub-district-wise namhatta counts for map visualization
     */
    public List<Map<String, Object>> getSubDistrictMapData(String district) {
        log.debug("Fetching sub-district map data for district: {}", district);
        try {
            List<Object[]> results = addressRepository.getSubDistrictWiseNamhattaCounts(district);
            return results.stream()
                    .map(row -> Map.of(
                            "name", row[0] != null ? row[0].toString() : "Unknown",
                            "count", row[1] != null ? ((Number) row[1]).intValue() : 0,
                            "coordinates", Arrays.asList(0.0, 0.0) // Default coordinates
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching sub-district map data", e);
            return List.of();
        }
    }
    
    /**
     * Get village-wise namhatta counts for map visualization
     */
    public List<Map<String, Object>> getVillageMapData(String subDistrict) {
        log.debug("Fetching village map data for sub-district: {}", subDistrict);
        try {
            List<Object[]> results = addressRepository.getVillageWiseNamhattaCounts(subDistrict);
            return results.stream()
                    .map(row -> Map.of(
                            "name", row[0] != null ? row[0].toString() : "Unknown",
                            "count", row[1] != null ? ((Number) row[1]).intValue() : 0,
                            "coordinates", Arrays.asList(0.0, 0.0) // Default coordinates
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching village map data", e);
            return List.of();
        }
    }
}