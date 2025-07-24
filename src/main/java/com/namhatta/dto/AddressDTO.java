package com.namhatta.dto;

import jakarta.validation.constraints.*;

public class AddressDTO {
    private Long id;
    
    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;
    
    @NotBlank(message = "State is required")
    @Size(max = 100, message = "State must not exceed 100 characters")
    private String stateNameEnglish;
    
    @NotBlank(message = "District is required")
    @Size(max = 100, message = "District must not exceed 100 characters")
    private String districtNameEnglish;
    
    @NotBlank(message = "Sub-district is required")
    @Size(max = 100, message = "Sub-district must not exceed 100 characters")
    private String subdistrictNameEnglish;
    
    @NotBlank(message = "Village is required")
    @Size(max = 100, message = "Village must not exceed 100 characters")
    private String villageNameEnglish;
    
    @NotBlank(message = "Pincode is required")
    @Pattern(regexp = "^[0-9]{6}$", message = "Pincode must be exactly 6 digits")
    private String pincode;
    
    @Size(max = 500, message = "Landmark must not exceed 500 characters")
    private String landmark;
    
    @Size(max = 50, message = "Address type must not exceed 50 characters")
    private String addressType;
    
    // Constructors
    public AddressDTO() {}
    
    public AddressDTO(String country, String stateNameEnglish, String districtNameEnglish) {
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
    
    public String getLandmark() { return landmark; }
    public void setLandmark(String landmark) { this.landmark = landmark; }
    
    public String getAddressType() { return addressType; }
    public void setAddressType(String addressType) { this.addressType = addressType; }
}