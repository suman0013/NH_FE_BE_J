package com.namhatta.repository;

import com.namhatta.entity.Devotee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DevoteeRepository extends JpaRepository<Devotee, Long> {
    
    @Query("SELECT d FROM Devotee d LEFT JOIN FETCH d.devotionalStatus LEFT JOIN FETCH d.namhatta WHERE d.id = :id")
    Optional<Devotee> findByIdWithDetails(@Param("id") Long id);
    
    Page<Devotee> findByLegalNameContainingIgnoreCase(String legalName, Pageable pageable);
    
    Page<Devotee> findBySpiritualNameContainingIgnoreCase(String spiritualName, Pageable pageable);
    
    List<Devotee> findByNamhattaId(Long namhattaId);
    
    List<Devotee> findByDevotionalStatusId(Long statusId);
    
    @Query("SELECT d FROM Devotee d WHERE d.phoneNumber = :phoneNumber OR d.whatsappNumber = :phoneNumber")
    List<Devotee> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);
    
    long countByNamhattaId(Long namhattaId);
    
    long countByDevotionalStatusId(Long statusId);
    
    /**
     * Get devotee count by devotional status for dashboard analytics
     */
    @Query("SELECT ds.name, COUNT(d) FROM Devotee d " +
           "LEFT JOIN d.devotionalStatus ds " +
           "GROUP BY ds.name")
    List<Object[]> findDevoteeCountByStatus();
}