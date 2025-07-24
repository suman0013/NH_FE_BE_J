package com.namhatta.controller;

import com.namhatta.dto.ApiResponse;
import com.namhatta.service.GeographyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Geography Controller
 * 
 * Handles geography-related API endpoints to match Node.js backend functionality.
 * Provides address data and location-based operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/geography")
@RequiredArgsConstructor
public class GeographyController {

    private final GeographyService geographyService;

    /**
     * Get all countries
     * GET /countries
     */
    @GetMapping("/countries")
    public ResponseEntity<ApiResponse<List<String>>> getCountries() {
        log.info("GET /countries - Fetching all countries");
        try {
            List<String> countries = geographyService.getAllCountries();
            log.debug("Found {} countries", countries.size());
            return ResponseEntity.ok(ApiResponse.success(countries));
        } catch (Exception e) {
            log.error("Error fetching countries", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch countries: " + e.getMessage()));
        }
    }

    /**
     * Get states by country
     * GET /states?country={country}
     */
    @GetMapping("/states")
    public ResponseEntity<ApiResponse<List<String>>> getStates(
            @RequestParam(required = false) String country) {
        log.info("GET /states - Fetching states for country: {}", country);
        try {
            List<String> states = geographyService.getStatesByCountry(country);
            log.debug("Found {} states", states.size());
            return ResponseEntity.ok(ApiResponse.success(states));
        } catch (Exception e) {
            log.error("Error fetching states for country: {}", country, e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch states: " + e.getMessage()));
        }
    }

    /**
     * Get districts by state
     * GET /districts?state={state}
     */
    @GetMapping("/districts")
    public ResponseEntity<ApiResponse<List<String>>> getDistricts(
            @RequestParam(required = false) String state) {
        log.info("GET /districts - Fetching districts for state: {}", state);
        try {
            List<String> districts = geographyService.getDistrictsByState(state);
            log.debug("Found {} districts", districts.size());
            return ResponseEntity.ok(ApiResponse.success(districts));
        } catch (Exception e) {
            log.error("Error fetching districts for state: {}", state, e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch districts: " + e.getMessage()));
        }
    }

    /**
     * Get sub-districts by district and optionally by pincode
     * GET /sub-districts?district={district}&pincode={pincode}
     */
    @GetMapping("/sub-districts")
    public ResponseEntity<ApiResponse<List<String>>> getSubDistricts(
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String pincode) {
        log.info("GET /sub-districts - Fetching sub-districts for district: {}, pincode: {}", 
                district, pincode);
        try {
            List<String> subDistricts = geographyService.getSubDistrictsByDistrict(district, pincode);
            log.debug("Found {} sub-districts", subDistricts.size());
            return ResponseEntity.ok(ApiResponse.success(subDistricts));
        } catch (Exception e) {
            log.error("Error fetching sub-districts for district: {}, pincode: {}", district, pincode, e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch sub-districts: " + e.getMessage()));
        }
    }

    /**
     * Get villages by sub-district and optionally by pincode
     * GET /villages?subDistrict={subDistrict}&pincode={pincode}
     */
    @GetMapping("/villages")
    public ResponseEntity<ApiResponse<List<String>>> getVillages(
            @RequestParam(required = false) String subDistrict,
            @RequestParam(required = false) String pincode) {
        log.info("GET /villages - Fetching villages for sub-district: {}, pincode: {}", 
                subDistrict, pincode);
        try {
            List<String> villages = geographyService.getVillagesBySubDistrict(subDistrict, pincode);
            log.debug("Found {} villages", villages.size());
            return ResponseEntity.ok(ApiResponse.success(villages));
        } catch (Exception e) {
            log.error("Error fetching villages for sub-district: {}, pincode: {}", subDistrict, pincode, e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch villages: " + e.getMessage()));
        }
    }

    /**
     * Search pincodes with pagination
     * GET /pincodes/search?country={country}&searchTerm={term}&page={page}&limit={limit}
     */
    @GetMapping("/pincodes/search")
    public ResponseEntity<ApiResponse<Map<String, Object>>> searchPincodes(
            @RequestParam(defaultValue = "India") String country,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int limit) {
        log.info("GET /pincodes/search - Searching pincodes for country: {}, term: {}, page: {}, limit: {}", 
                country, searchTerm, page, limit);
        try {
            Map<String, Object> result = geographyService.searchPincodes(country, searchTerm, page, limit);
            log.debug("Found {} pincodes in search results", 
                    ((List<?>) result.get("pincodes")).size());
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            log.error("Error searching pincodes", e);
            return ResponseEntity.ok(ApiResponse.error("Failed to search pincodes: " + e.getMessage()));
        }
    }

    /**
     * Get address information by pincode
     * GET /address-by-pincode/{pincode}
     */
    @GetMapping("/address-by-pincode/{pincode}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAddressByPincode(
            @PathVariable String pincode) {
        log.info("GET /address-by-pincode/{} - Fetching address info", pincode);
        try {
            Map<String, Object> address = geographyService.getAddressByPincode(pincode);
            if (address != null) {
                log.debug("Found address info for pincode: {}", pincode);
                return ResponseEntity.ok(ApiResponse.success(address));
            } else {
                log.debug("No address info found for pincode: {}", pincode);
                return ResponseEntity.ok(ApiResponse.error("No address found for pincode: " + pincode));
            }
        } catch (Exception e) {
            log.error("Error fetching address for pincode: {}", pincode, e);
            return ResponseEntity.ok(ApiResponse.error("Failed to fetch address: " + e.getMessage()));
        }
    }
}