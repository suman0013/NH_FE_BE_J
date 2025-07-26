package com.namhatta.repository;

import com.namhatta.entity.NamhattaUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NamhattaUpdateRepository extends JpaRepository<NamhattaUpdate, Long> {
    
    List<NamhattaUpdate> findByNamhattaIdOrderByCreatedAtDesc(Long namhattaId);
    
    Page<NamhattaUpdate> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    @Query("SELECT nu FROM NamhattaUpdate nu WHERE " +
           "(:programType IS NULL OR nu.programType = :programType) AND " +
           "(:namhattaId IS NULL OR nu.namhattaId = :namhattaId) " +
           "ORDER BY nu.createdAt DESC")
    Page<NamhattaUpdate> findFilteredUpdates(
            @Param("programType") String programType,
            @Param("namhattaId") Long namhattaId,
            Pageable pageable);
    
    @Query(value = "SELECT COUNT(*) FROM namhatta_updates WHERE created_at >= CURRENT_DATE - INTERVAL '30 days'", nativeQuery = true)
    Long countRecentUpdates();
}