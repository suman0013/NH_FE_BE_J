package com.namhatta.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DevoteeDTO {
    private Long id;
    
    @NotBlank(message = "Legal name is required")
    @Size(max = 255, message = "Legal name must not exceed 255 characters")
    private String legalName;
    
    @Size(max = 255, message = "Spiritual name must not exceed 255 characters")
    private String spiritualName;
    
    private LocalDate dateOfBirth;
    
    @Size(max = 20, message = "Gender must not exceed 20 characters")
    private String gender;
    
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number format")
    private String phoneNumber;
    
    @Email(message = "Invalid email format")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;
    
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid WhatsApp number format")
    private String whatsappNumber;
    
    private Long devotionalStatusId;
    private String devotionalStatusName;
    
    private Long namhattaId;
    private String namhattaName;
    
    private LocalDate harinameDate;
    private LocalDate dikshaDate;
    private LocalDate pancharatrikDate;
    
    @Size(max = 255, message = "Guru Maharaja name must not exceed 255 characters")
    private String guruMaharaja;
    
    @Size(max = 500, message = "Education must not exceed 500 characters")
    private String education;
    
    @Size(max = 500, message = "Occupation must not exceed 500 characters")
    private String occupation;
    
    private String devotionalCourses;
    
    private List<AddressDTO> addresses;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public DevoteeDTO() {}
    
    public DevoteeDTO(String legalName) {
        this.legalName = legalName;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getLegalName() { return legalName; }
    public void setLegalName(String legalName) { this.legalName = legalName; }
    
    public String getSpiritualName() { return spiritualName; }
    public void setSpiritualName(String spiritualName) { this.spiritualName = spiritualName; }
    
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getWhatsappNumber() { return whatsappNumber; }
    public void setWhatsappNumber(String whatsappNumber) { this.whatsappNumber = whatsappNumber; }
    
    public Long getDevotionalStatusId() { return devotionalStatusId; }
    public void setDevotionalStatusId(Long devotionalStatusId) { this.devotionalStatusId = devotionalStatusId; }
    
    public String getDevotionalStatusName() { return devotionalStatusName; }
    public void setDevotionalStatusName(String devotionalStatusName) { this.devotionalStatusName = devotionalStatusName; }
    
    public Long getNamhattaId() { return namhattaId; }
    public void setNamhattaId(Long namhattaId) { this.namhattaId = namhattaId; }
    
    public String getNamhattaName() { return namhattaName; }
    public void setNamhattaName(String namhattaName) { this.namhattaName = namhattaName; }
    
    public LocalDate getHarinameDate() { return harinameDate; }
    public void setHarinameDate(LocalDate harinameDate) { this.harinameDate = harinameDate; }
    
    public LocalDate getDikshaDate() { return dikshaDate; }
    public void setDikshaDate(LocalDate dikshaDate) { this.dikshaDate = dikshaDate; }
    
    public LocalDate getPancharatrikDate() { return pancharatrikDate; }
    public void setPancharatrikDate(LocalDate pancharatrikDate) { this.pancharatrikDate = pancharatrikDate; }
    
    public String getGuruMaharaja() { return guruMaharaja; }
    public void setGuruMaharaja(String guruMaharaja) { this.guruMaharaja = guruMaharaja; }
    
    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }
    
    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    
    public String getDevotionalCourses() { return devotionalCourses; }
    public void setDevotionalCourses(String devotionalCourses) { this.devotionalCourses = devotionalCourses; }
    
    public List<AddressDTO> getAddresses() { return addresses; }
    public void setAddresses(List<AddressDTO> addresses) { this.addresses = addresses; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}