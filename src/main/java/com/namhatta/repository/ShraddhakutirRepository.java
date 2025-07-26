package com.namhatta.repository;

import com.namhatta.entity.Shraddhakutir;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShraddhakutirRepository extends JpaRepository<Shraddhakutir, Long> {
    
    List<Shraddhakutir> findByDistrictCode(String districtCode);
    
    @Query("SELECT s FROM Shraddhakutir s WHERE " +
           "(:districtCode IS NULL OR s.districtCode = :districtCode) " +
           "ORDER BY s.name")
    List<Shraddhakutir> findByDistrictCodeOptional(@Param("districtCode") String districtCode);
    
    @Query("SELECT DISTINCT s.districtCode FROM Shraddhakutir s WHERE s.districtCode IS NOT NULL ORDER BY s.districtCode")
    List<String> findAllDistrictCodes();
}