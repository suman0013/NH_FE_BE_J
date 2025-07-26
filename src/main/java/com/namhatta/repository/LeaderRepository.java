package com.namhatta.repository;

import com.namhatta.entity.Leader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaderRepository extends JpaRepository<Leader, Long> {
    
    List<Leader> findByRole(String role);
    
    List<Leader> findByReportingTo(Long reportingTo);
    
    @Query("SELECT l FROM Leader l WHERE l.role = :role AND " +
           "(:country IS NULL OR l.country = :country) AND " +
           "(:state IS NULL OR l.state = :state) AND " +
           "(:district IS NULL OR l.district = :district)")
    List<Leader> findByRoleAndLocation(
            @Param("role") String role,
            @Param("country") String country,
            @Param("state") String state,
            @Param("district") String district);
    
    @Query("SELECT l FROM Leader l WHERE l.reportingTo IS NULL ORDER BY l.id")
    List<Leader> findTopLevelLeaders();
    
    @Query("SELECT DISTINCT l.role FROM Leader l ORDER BY l.role")
    List<String> findAllRoles();
}