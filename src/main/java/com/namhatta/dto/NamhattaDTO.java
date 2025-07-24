package com.namhatta.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

public class NamhattaDTO {
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;
    
    @Size(max = 50, message = "Code must not exceed 50 characters")
    private String code;
    
    @NotBlank(message = "Secretary is required")
    @Size(max = 255, message = "Secretary name must not exceed 255 characters")
    private String secretary;
    
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid contact number format")
    private String contactNumber;
    
    @Size(max = 50, message = "Meeting day must not exceed 50 characters")
    private String meetingDay;
    
    @Size(max = 50, message = "Meeting time must not exceed 50 characters")
    private String meetingTime;
    
    @Pattern(regexp = "^(ACTIVE|INACTIVE|PENDING|REJECTED)$", message = "Status must be ACTIVE, INACTIVE, PENDING, or REJECTED")
    private String status;
    
    private List<AddressDTO> addresses;
    private List<DevoteeDTO> devotees;
    private Integer devoteeCount;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public NamhattaDTO() {}
    
    public NamhattaDTO(String name, String code) {
        this.name = name;
        this.code = code;
        this.status = "ACTIVE";
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getSecretary() { return secretary; }
    public void setSecretary(String secretary) { this.secretary = secretary; }
    
    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
    
    public String getMeetingDay() { return meetingDay; }
    public void setMeetingDay(String meetingDay) { this.meetingDay = meetingDay; }
    
    public String getMeetingTime() { return meetingTime; }
    public void setMeetingTime(String meetingTime) { this.meetingTime = meetingTime; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public List<AddressDTO> getAddresses() { return addresses; }
    public void setAddresses(List<AddressDTO> addresses) { this.addresses = addresses; }
    
    public List<DevoteeDTO> getDevotees() { return devotees; }
    public void setDevotees(List<DevoteeDTO> devotees) { this.devotees = devotees; }
    
    public Integer getDevoteeCount() { return devoteeCount; }
    public void setDevoteeCount(Integer devoteeCount) { this.devoteeCount = devoteeCount; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}