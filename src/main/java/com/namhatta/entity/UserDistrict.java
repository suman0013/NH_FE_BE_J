package com.namhatta.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_districts")
public class UserDistrict {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "district_name", nullable = false)
    private String districtName;
    
    // Constructors
    public UserDistrict() {}
    
    public UserDistrict(User user, String districtName) {
        this.user = user;
        this.districtName = districtName;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public String getDistrictName() { return districtName; }
    public void setDistrictName(String districtName) { this.districtName = districtName; }
}