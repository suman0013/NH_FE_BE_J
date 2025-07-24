package com.namhatta.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "namhattas")
public class Namhatta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "code", unique = true)
    private String code;
    
    @Column(name = "secretary")
    private String secretary;
    
    @Column(name = "contact_number")
    private String contactNumber;
    
    @Column(name = "meeting_day")
    private String meetingDay;
    
    @Column(name = "meeting_time")
    private String meetingTime;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relationships
    @OneToMany(mappedBy = "namhatta", fetch = FetchType.LAZY)
    private List<Devotee> devotees = new ArrayList<>();
    
    @OneToMany(mappedBy = "namhatta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NamhattaAddress> addresses = new ArrayList<>();
    
    // Constructors
    public Namhatta() {}
    
    public Namhatta(String name, String code) {
        this.name = name;
        this.code = code;
        this.status = "ACTIVE";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<Devotee> getDevotees() { return devotees; }
    public void setDevotees(List<Devotee> devotees) { this.devotees = devotees; }
    
    public List<NamhattaAddress> getAddresses() { return addresses; }
    public void setAddresses(List<NamhattaAddress> addresses) { this.addresses = addresses; }
}