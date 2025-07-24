package com.namhatta.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class DevotionalStatusDTO {
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;
    
    @Min(value = 1, message = "Hierarchy level must be at least 1")
    @Max(value = 10, message = "Hierarchy level must not exceed 10")
    private Integer hierarchyLevel;
    
    private Integer devoteeCount;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public DevotionalStatusDTO() {}
    
    public DevotionalStatusDTO(String name, Integer hierarchyLevel) {
        this.name = name;
        this.hierarchyLevel = hierarchyLevel;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Integer getHierarchyLevel() { return hierarchyLevel; }
    public void setHierarchyLevel(Integer hierarchyLevel) { this.hierarchyLevel = hierarchyLevel; }
    
    public Integer getDevoteeCount() { return devoteeCount; }
    public void setDevoteeCount(Integer devoteeCount) { this.devoteeCount = devoteeCount; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}