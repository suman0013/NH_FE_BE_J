package com.namhatta.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "devotee_addresses")
public class DevoteeAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "devotee_id", nullable = false)
    private Devotee devotee;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
    
    @Column(name = "landmark")
    private String landmark;
    
    @Column(name = "address_type")
    private String addressType; // "CURRENT", "PERMANENT", etc.
    
    // Constructors
    public DevoteeAddress() {}
    
    public DevoteeAddress(Devotee devotee, Address address, String landmark) {
        this.devotee = devotee;
        this.address = address;
        this.landmark = landmark;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Devotee getDevotee() { return devotee; }
    public void setDevotee(Devotee devotee) { this.devotee = devotee; }
    
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    
    public String getLandmark() { return landmark; }
    public void setLandmark(String landmark) { this.landmark = landmark; }
    
    public String getAddressType() { return addressType; }
    public void setAddressType(String addressType) { this.addressType = addressType; }
}