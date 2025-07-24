package com.namhatta.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "devotees")
public class Devotee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Personal information
    @Column(name = "legal_name", nullable = false)
    private String legalName;
    
    @Column(name = "spiritual_name")
    private String spiritualName;
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    @Column(name = "gender")
    private String gender;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "whatsapp_number")
    private String whatsappNumber;
    
    // Spiritual information
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "devotional_status_id")
    private DevotionalStatus devotionalStatus;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "namhatta_id")
    private Namhatta namhatta;
    
    @Column(name = "harinama_date")
    private LocalDate harinameDate;
    
    @Column(name = "diksha_date")
    private LocalDate dikshaDate;
    
    @Column(name = "pancharatrik_date")
    private LocalDate pancharatrikDate;
    
    @Column(name = "guru_maharaja")
    private String guruMaharaja;
    
    // Professional information
    @Column(name = "education")
    private String education;
    
    @Column(name = "occupation")
    private String occupation;
    
    // JSON field for devotional courses - simplified as text for now
    @Column(name = "devotional_courses", columnDefinition = "jsonb")
    private String devotionalCourses;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Relationships
    @OneToMany(mappedBy = "devotee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DevoteeAddress> addresses = new ArrayList<>();
    
    // Constructors
    public Devotee() {}
    
    public Devotee(String legalName) {
        this.legalName = legalName;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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
    
    public DevotionalStatus getDevotionalStatus() { return devotionalStatus; }
    public void setDevotionalStatus(DevotionalStatus devotionalStatus) { this.devotionalStatus = devotionalStatus; }
    
    public Namhatta getNamhatta() { return namhatta; }
    public void setNamhatta(Namhatta namhatta) { this.namhatta = namhatta; }
    
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
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public List<DevoteeAddress> getAddresses() { return addresses; }
    public void setAddresses(List<DevoteeAddress> addresses) { this.addresses = addresses; }
}