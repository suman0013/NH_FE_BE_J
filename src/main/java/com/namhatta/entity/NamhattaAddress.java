package com.namhatta.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "namhatta_addresses")
public class NamhattaAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "namhatta_id", nullable = false)
    private Namhatta namhatta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
    
    @Column(name = "landmark")
    private String landmark;
    
    @Column(name = "address_type")
    private String addressType; // "MEETING", "OFFICE", etc.
    
    // Constructors
    public NamhattaAddress() {}
    
    public NamhattaAddress(Namhatta namhatta, Address address, String landmark) {
        this.namhatta = namhatta;
        this.address = address;
        this.landmark = landmark;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Namhatta getNamhatta() { return namhatta; }
    public void setNamhatta(Namhatta namhatta) { this.namhatta = namhatta; }
    
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    
    public String getLandmark() { return landmark; }
    public void setLandmark(String landmark) { this.landmark = landmark; }
    
    public String getAddressType() { return addressType; }
    public void setAddressType(String addressType) { this.addressType = addressType; }
}