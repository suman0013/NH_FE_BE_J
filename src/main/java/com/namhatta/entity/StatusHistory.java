package com.namhatta.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "status_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "devotee_id", nullable = false)
    private Long devoteeId;
    
    @Column(name = "previous_status")
    private String previousStatus;
    
    @Column(name = "new_status", nullable = false)
    private String newStatus;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;
    
    @PrePersist
    protected void onCreate() {
        updatedAt = LocalDateTime.now();
    }
}