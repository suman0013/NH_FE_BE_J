package com.namhatta.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "country")
    private String country;
    
    @Column(name = "state_name_english")
    private String stateNameEnglish;
    
    @Column(name = "district_name_english") 
    private String districtNameEnglish;
    
    @Column(name = "subdistrict_name_english")
    private String subdistrictNameEnglish;
    
    @Column(name = "village_name_english")
    private String villageNameEnglish;
    
    @Column(name = "pincode")
    private String pincode;
    
    // Relationships
    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DevoteeAddress> devoteeAddresses = new ArrayList<>();
    
    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NamhattaAddress> namhattaAddresses = new ArrayList<>();
    
    // Constructors
    public Address() {}
    
    public Address(String country, String stateNameEnglish, String districtNameEnglish) {
        this.country = country;
        this.stateNameEnglish = stateNameEnglish;
        this.districtNameEnglish = districtNameEnglish;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    
    public String getStateNameEnglish() { return stateNameEnglish; }
    public void setStateNameEnglish(String stateNameEnglish) { this.stateNameEnglish = stateNameEnglish; }
    
    public String getDistrictNameEnglish() { return districtNameEnglish; }
    public void setDistrictNameEnglish(String districtNameEnglish) { this.districtNameEnglish = districtNameEnglish; }
    
    public String getSubdistrictNameEnglish() { return subdistrictNameEnglish; }
    public void setSubdistrictNameEnglish(String subdistrictNameEnglish) { this.subdistrictNameEnglish = subdistrictNameEnglish; }
    
    public String getVillageNameEnglish() { return villageNameEnglish; }
    public void setVillageNameEnglish(String villageNameEnglish) { this.villageNameEnglish = villageNameEnglish; }
    
    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }
    
    public List<DevoteeAddress> getDevoteeAddresses() { return devoteeAddresses; }
    public void setDevoteeAddresses(List<DevoteeAddress> devoteeAddresses) { this.devoteeAddresses = devoteeAddresses; }
    
    public List<NamhattaAddress> getNamhattaAddresses() { return namhattaAddresses; }
    public void setNamhattaAddresses(List<NamhattaAddress> namhattaAddresses) { this.namhattaAddresses = namhattaAddresses; }
}