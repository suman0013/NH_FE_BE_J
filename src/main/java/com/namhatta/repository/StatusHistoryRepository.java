package com.namhatta.repository;

import com.namhatta.entity.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Long> {
    
    List<StatusHistory> findByDevoteeIdOrderByUpdatedAtDesc(Long devoteeId);
    
    @Query("SELECT sh FROM StatusHistory sh WHERE sh.devoteeId = :devoteeId ORDER BY sh.updatedAt DESC")
    List<StatusHistory> findDevoteeStatusHistory(@Param("devoteeId") Long devoteeId);
    
    @Query(value = "SELECT COUNT(*) FROM status_history WHERE updated_at >= CURRENT_DATE - INTERVAL '30 days'", nativeQuery = true)
    Long countRecentStatusChanges();
    
    @Query(value = "SELECT new_status, COUNT(*) FROM status_history " +
           "WHERE updated_at >= CURRENT_DATE - INTERVAL '30 days' " +
           "GROUP BY new_status", nativeQuery = true)
    List<Object[]> getRecentStatusDistribution();
}