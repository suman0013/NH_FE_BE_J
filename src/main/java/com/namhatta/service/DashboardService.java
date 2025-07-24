package com.namhatta.service;

import com.namhatta.entity.Devotee;
import com.namhatta.entity.Namhatta;
import com.namhatta.repository.DevoteeRepository;
import com.namhatta.repository.NamhattaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dashboard Service Implementation
 * 
 * Provides dashboard statistics and analytics matching Node.js backend functionality.
 * Includes summary data, status distribution, and recent activity metrics.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final DevoteeRepository devoteeRepository;
    private final NamhattaRepository namhattaRepository;

    /**
     * Get dashboard summary statistics
     * Returns total counts and recent activity data
     */
    public Map<String, Object> getDashboardSummary() {
        log.debug("Fetching dashboard summary statistics");
        
        try {
            // Get total counts
            long totalDevotees = devoteeRepository.count();
            long totalNamhattas = namhattaRepository.count();
            
            // Get recent updates - simplified version for now
            // In the full implementation, this would fetch from namhatta_updates table
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalDevotees", totalDevotees);
            summary.put("totalNamhattas", totalNamhattas);
            summary.put("recentUpdates", List.of()); // Empty for now, can be enhanced later
            
            log.debug("Dashboard summary: {} devotees, {} namhattas", totalDevotees, totalNamhattas);
            return summary;
            
        } catch (Exception e) {
            log.error("Error fetching dashboard summary", e);
            throw new RuntimeException("Failed to fetch dashboard summary", e);
        }
    }

    /**
     * Get devotional status distribution
     * Returns count of devotees by each devotional status
     */
    public Map<String, Object> getStatusDistribution() {
        log.debug("Fetching status distribution");
        
        try {
            // Query devotees grouped by devotional status
            List<Object[]> statusCounts = devoteeRepository.findDevoteeCountByStatus();
            
            Map<String, Long> distribution = new HashMap<>();
            for (Object[] row : statusCounts) {
                String statusName = (String) row[0];
                Long count = (Long) row[1];
                distribution.put(statusName != null ? statusName : "Unknown", count);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("statusDistribution", distribution);
            
            log.debug("Status distribution: {} status types", distribution.size());
            return result;
            
        } catch (Exception e) {
            log.error("Error fetching status distribution", e);
            throw new RuntimeException("Failed to fetch status distribution", e);
        }
    }

    /**
     * Get namhatta counts by country for map visualization
     */
    public List<Map<String, Object>> getNamhattaCountsByCountry() {
        log.debug("Fetching namhatta counts by country");
        
        try {
            List<Object[]> countryCounts = namhattaRepository.findNamhattaCountsByCountry();
            
            return countryCounts.stream()
                .map(row -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("country", row[0] != null ? row[0] : "Unknown");
                    item.put("count", row[1]);
                    return item;
                })
                .toList();
                
        } catch (Exception e) {
            log.error("Error fetching namhatta counts by country", e);
            throw new RuntimeException("Failed to fetch namhatta counts by country", e);
        }
    }

    /**
     * Get namhatta counts by state for map visualization
     */
    public List<Map<String, Object>> getNamhattaCountsByState() {
        log.debug("Fetching namhatta counts by state");
        
        try {
            List<Object[]> stateCounts = namhattaRepository.findNamhattaCountsByState();
            
            return stateCounts.stream()
                .map(row -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("state", row[0] != null ? row[0] : "Unknown");
                    item.put("count", row[1]);
                    return item;
                })
                .toList();
                
        } catch (Exception e) {
            log.error("Error fetching namhatta counts by state", e);
            throw new RuntimeException("Failed to fetch namhatta counts by state", e);
        }
    }

    /**
     * Get namhatta counts by district for map visualization
     */
    public List<Map<String, Object>> getNamhattaCountsByDistrict() {
        log.debug("Fetching namhatta counts by district");
        
        try {
            List<Object[]> districtCounts = namhattaRepository.findNamhattaCountsByDistrict();
            
            return districtCounts.stream()
                .map(row -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("district", row[0] != null ? row[0] : "Unknown");
                    item.put("count", row[1]);
                    return item;
                })
                .toList();
                
        } catch (Exception e) {
            log.error("Error fetching namhatta counts by district", e);
            throw new RuntimeException("Failed to fetch namhatta counts by district", e);
        }
    }

    /**
     * Get namhatta counts by sub-district for map visualization
     */
    public List<Map<String, Object>> getNamhattaCountsBySubDistrict() {
        log.debug("Fetching namhatta counts by sub-district");
        
        try {
            List<Object[]> subDistrictCounts = namhattaRepository.findNamhattaCountsBySubDistrict();
            
            return subDistrictCounts.stream()
                .map(row -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("subDistrict", row[0] != null ? row[0] : "Unknown");
                    item.put("count", row[1]);
                    return item;
                })
                .toList();
                
        } catch (Exception e) {
            log.error("Error fetching namhatta counts by sub-district", e);
            throw new RuntimeException("Failed to fetch namhatta counts by sub-district", e);
        }
    }

    /**
     * Get namhatta counts by village for map visualization
     */
    public List<Map<String, Object>> getNamhattaCountsByVillage() {
        log.debug("Fetching namhatta counts by village");
        
        try {
            List<Object[]> villageCounts = namhattaRepository.findNamhattaCountsByVillage();
            
            return villageCounts.stream()
                .map(row -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("village", row[0] != null ? row[0] : "Unknown");
                    item.put("count", row[1]);
                    return item;
                })
                .toList();
                
        } catch (Exception e) {
            log.error("Error fetching namhatta counts by village", e);
            throw new RuntimeException("Failed to fetch namhatta counts by village", e);
        }
    }
}