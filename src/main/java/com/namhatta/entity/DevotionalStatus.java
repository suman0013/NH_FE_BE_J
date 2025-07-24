package com.namhatta.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "devotional_statuses")
public class DevotionalStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "hierarchy_level")
    private Integer hierarchyLevel;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relationships
    @OneToMany(mappedBy = "devotionalStatus", fetch = FetchType.LAZY)
    private List<Devotee> devotees;
    
    // Constructors
    public DevotionalStatus() {}
    
    public DevotionalStatus(String name, Integer hierarchyLevel) {
        this.name = name;
        this.hierarchyLevel = hierarchyLevel;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Integer getHierarchyLevel() { return hierarchyLevel; }
    public void setHierarchyLevel(Integer hierarchyLevel) { this.hierarchyLevel = hierarchyLevel; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<Devotee> getDevotees() { return devotees; }
    public void setDevotees(List<Devotee> devotees) { this.devotees = devotees; }
}