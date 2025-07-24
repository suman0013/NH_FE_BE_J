package com.namhatta.repository;

import com.namhatta.entity.DevotionalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DevotionalStatusRepository extends JpaRepository<DevotionalStatus, Long> {
    
    Optional<DevotionalStatus> findByName(String name);
    
    List<DevotionalStatus> findByOrderByHierarchyLevelAsc();
    
    boolean existsByName(String name);
}