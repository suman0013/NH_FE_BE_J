package com.namhatta.repository;

import com.namhatta.entity.Namhatta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NamhattaRepository extends JpaRepository<Namhatta, Long> {
    
    Optional<Namhatta> findByCode(String code);
    
    Page<Namhatta> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    List<Namhatta> findByStatus(String status);
    
    @Query("SELECT n FROM Namhatta n LEFT JOIN FETCH n.devotees WHERE n.id = :id")
    Optional<Namhatta> findByIdWithDevotees(@Param("id") Long id);
    
    boolean existsByCode(String code);
    
    long countByStatus(String status);
    
    /**
     * Get namhatta counts by country for map visualization
     */
    @Query("SELECT COALESCE(a.country, 'Unknown'), COUNT(n) FROM Namhatta n " +
           "LEFT JOIN NamhattaAddress na ON n.id = na.namhatta.id " +
           "LEFT JOIN na.address a " +
           "GROUP BY a.country")
    List<Object[]> findNamhattaCountsByCountry();
    
    /**
     * Get namhatta counts by state for map visualization
     */
    @Query("SELECT COALESCE(a.stateNameEnglish, 'Unknown'), COUNT(n) FROM Namhatta n " +
           "LEFT JOIN NamhattaAddress na ON n.id = na.namhatta.id " +
           "LEFT JOIN na.address a " +
           "GROUP BY a.stateNameEnglish")
    List<Object[]> findNamhattaCountsByState();
    
    /**
     * Get namhatta counts by district for map visualization
     */
    @Query("SELECT COALESCE(a.districtNameEnglish, 'Unknown'), COUNT(n) FROM Namhatta n " +
           "LEFT JOIN NamhattaAddress na ON n.id = na.namhatta.id " +
           "LEFT JOIN na.address a " +
           "GROUP BY a.districtNameEnglish")
    List<Object[]> findNamhattaCountsByDistrict();
    
    /**
     * Get namhatta counts by sub-district for map visualization
     */
    @Query("SELECT COALESCE(a.subdistrictNameEnglish, 'Unknown'), COUNT(n) FROM Namhatta n " +
           "LEFT JOIN NamhattaAddress na ON n.id = na.namhatta.id " +
           "LEFT JOIN na.address a " +
           "GROUP BY a.subdistrictNameEnglish")
    List<Object[]> findNamhattaCountsBySubDistrict();
    
    /**
     * Get namhatta counts by village for map visualization
     */
    @Query("SELECT COALESCE(a.villageNameEnglish, 'Unknown'), COUNT(n) FROM Namhatta n " +
           "LEFT JOIN NamhattaAddress na ON n.id = na.namhatta.id " +
           "LEFT JOIN na.address a " +
           "GROUP BY a.villageNameEnglish")
    List<Object[]> findNamhattaCountsByVillage();
}