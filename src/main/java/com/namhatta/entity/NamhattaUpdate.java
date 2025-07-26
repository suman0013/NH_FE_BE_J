package com.namhatta.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "namhatta_updates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NamhattaUpdate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "namhatta_id", nullable = false)
    private Long namhattaId;
    
    @Column(name = "program_type")
    private String programType;
    
    @Column(name = "date")
    private String date;
    
    @Column(name = "attendance")
    private Integer attendance;
    
    @Column(name = "prasad_distribution")
    private Integer prasadDistribution;
    
    @Column(name = "nagar_kirtan")
    private Integer nagarKirtan = 0; // 0 or 1
    
    @Column(name = "book_distribution")
    private Integer bookDistribution = 0; // 0 or 1
    
    @Column(name = "chanting")
    private Integer chanting = 0; // 0 or 1
    
    @Column(name = "arati")
    private Integer arati = 0; // 0 or 1
    
    @Column(name = "bhagwat_path")
    private Integer bhagwatPath = 0; // 0 or 1
    
    @ElementCollection
    @CollectionTable(name = "namhatta_update_images", joinColumns = @JoinColumn(name = "update_id"))
    @Column(name = "image_url")
    private List<String> imageUrls;
    
    @Column(name = "facebook_link")
    private String facebookLink;
    
    @Column(name = "youtube_link")
    private String youtubeLink;
    
    @Column(name = "special_attraction", columnDefinition = "TEXT")
    private String specialAttraction;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}