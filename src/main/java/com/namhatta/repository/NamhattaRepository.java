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
}