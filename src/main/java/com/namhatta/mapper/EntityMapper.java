package com.namhatta.mapper;

import com.namhatta.dto.*;
import com.namhatta.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityMapper {
    
    // User mappings
    public UserDTO toUserDTO(User user) {
        if (user == null) return null;
        
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        dto.setActive(user.getActive());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        
        // Map districts
        if (user.getUserDistricts() != null) {
            List<String> districts = user.getUserDistricts().stream()
                .map(UserDistrict::getDistrictName)
                .collect(Collectors.toList());
            dto.setDistricts(districts);
        }
        
        return dto;
    }
    
    public User toUserEntity(UserDTO dto) {
        if (dto == null) return null;
        
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setRole(dto.getRole());
        user.setActive(dto.getActive());
        user.setCreatedAt(dto.getCreatedAt());
        user.setUpdatedAt(dto.getUpdatedAt());
        
        return user;
    }
    
    // Devotee mappings
    public DevoteeDTO toDevoteeDTO(Devotee devotee) {
        if (devotee == null) return null;
        
        DevoteeDTO dto = new DevoteeDTO();
        dto.setId(devotee.getId());
        dto.setLegalName(devotee.getLegalName());
        dto.setSpiritualName(devotee.getSpiritualName());
        dto.setDateOfBirth(devotee.getDateOfBirth());
        dto.setGender(devotee.getGender());
        dto.setPhoneNumber(devotee.getPhoneNumber());
        dto.setEmail(devotee.getEmail());
        dto.setWhatsappNumber(devotee.getWhatsappNumber());
        dto.setHarinameDate(devotee.getHarinameDate());
        dto.setDikshaDate(devotee.getDikshaDate());
        dto.setPancharatrikDate(devotee.getPancharatrikDate());
        dto.setGuruMaharaja(devotee.getGuruMaharaja());
        dto.setEducation(devotee.getEducation());
        dto.setOccupation(devotee.getOccupation());
        dto.setDevotionalCourses(devotee.getDevotionalCourses());
        dto.setCreatedAt(devotee.getCreatedAt());
        dto.setUpdatedAt(devotee.getUpdatedAt());
        
        // Map devotional status
        if (devotee.getDevotionalStatus() != null) {
            dto.setDevotionalStatusId(devotee.getDevotionalStatus().getId());
            dto.setDevotionalStatusName(devotee.getDevotionalStatus().getName());
        }
        
        // Map namhatta
        if (devotee.getNamhatta() != null) {
            dto.setNamhattaId(devotee.getNamhatta().getId());
            dto.setNamhattaName(devotee.getNamhatta().getName());
        }
        
        // Map addresses
        if (devotee.getAddresses() != null) {
            List<AddressDTO> addresses = devotee.getAddresses().stream()
                .map(this::toAddressDTO)
                .collect(Collectors.toList());
            dto.setAddresses(addresses);
        }
        
        return dto;
    }
    
    public Devotee toDevoteeEntity(DevoteeDTO dto) {
        if (dto == null) return null;
        
        Devotee devotee = new Devotee();
        devotee.setId(dto.getId());
        devotee.setLegalName(dto.getLegalName());
        devotee.setSpiritualName(dto.getSpiritualName());
        devotee.setDateOfBirth(dto.getDateOfBirth());
        devotee.setGender(dto.getGender());
        devotee.setPhoneNumber(dto.getPhoneNumber());
        devotee.setEmail(dto.getEmail());
        devotee.setWhatsappNumber(dto.getWhatsappNumber());
        devotee.setHarinameDate(dto.getHarinameDate());
        devotee.setDikshaDate(dto.getDikshaDate());
        devotee.setPancharatrikDate(dto.getPancharatrikDate());
        devotee.setGuruMaharaja(dto.getGuruMaharaja());
        devotee.setEducation(dto.getEducation());
        devotee.setOccupation(dto.getOccupation());
        devotee.setDevotionalCourses(dto.getDevotionalCourses());
        devotee.setCreatedAt(dto.getCreatedAt());
        devotee.setUpdatedAt(dto.getUpdatedAt());
        
        return devotee;
    }
    
    // Namhatta mappings
    public NamhattaDTO toNamhattaDTO(Namhatta namhatta) {
        if (namhatta == null) return null;
        
        NamhattaDTO dto = new NamhattaDTO();
        dto.setId(namhatta.getId());
        dto.setName(namhatta.getName());
        dto.setCode(namhatta.getCode());
        dto.setSecretary(namhatta.getSecretary());
        dto.setContactNumber(namhatta.getContactNumber());
        dto.setMeetingDay(namhatta.getMeetingDay());
        dto.setMeetingTime(namhatta.getMeetingTime());
        dto.setStatus(namhatta.getStatus());
        dto.setCreatedAt(namhatta.getCreatedAt());
        dto.setUpdatedAt(namhatta.getUpdatedAt());
        
        // Map devotee count
        if (namhatta.getDevotees() != null) {
            dto.setDevoteeCount(namhatta.getDevotees().size());
        }
        
        // Map addresses
        if (namhatta.getAddresses() != null) {
            List<AddressDTO> addresses = namhatta.getAddresses().stream()
                .map(this::toAddressDTO)
                .collect(Collectors.toList());
            dto.setAddresses(addresses);
        }
        
        return dto;
    }
    
    public Namhatta toNamhattaEntity(NamhattaDTO dto) {
        if (dto == null) return null;
        
        Namhatta namhatta = new Namhatta();
        namhatta.setId(dto.getId());
        namhatta.setName(dto.getName());
        namhatta.setCode(dto.getCode());
        namhatta.setSecretary(dto.getSecretary());
        namhatta.setContactNumber(dto.getContactNumber());
        namhatta.setMeetingDay(dto.getMeetingDay());
        namhatta.setMeetingTime(dto.getMeetingTime());
        namhatta.setStatus(dto.getStatus());
        namhatta.setCreatedAt(dto.getCreatedAt());
        namhatta.setUpdatedAt(dto.getUpdatedAt());
        
        return namhatta;
    }
    
    // Address mappings
    public AddressDTO toAddressDTO(DevoteeAddress devoteeAddress) {
        if (devoteeAddress == null || devoteeAddress.getAddress() == null) return null;
        
        Address address = devoteeAddress.getAddress();
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setCountry(address.getCountry());
        dto.setStateNameEnglish(address.getStateNameEnglish());
        dto.setDistrictNameEnglish(address.getDistrictNameEnglish());
        dto.setSubdistrictNameEnglish(address.getSubdistrictNameEnglish());
        dto.setVillageNameEnglish(address.getVillageNameEnglish());
        dto.setPincode(address.getPincode());
        dto.setLandmark(devoteeAddress.getLandmark());
        dto.setAddressType(devoteeAddress.getAddressType());
        
        return dto;
    }
    
    public AddressDTO toAddressDTO(NamhattaAddress namhattaAddress) {
        if (namhattaAddress == null || namhattaAddress.getAddress() == null) return null;
        
        Address address = namhattaAddress.getAddress();
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setCountry(address.getCountry());
        dto.setStateNameEnglish(address.getStateNameEnglish());
        dto.setDistrictNameEnglish(address.getDistrictNameEnglish());
        dto.setSubdistrictNameEnglish(address.getSubdistrictNameEnglish());
        dto.setVillageNameEnglish(address.getVillageNameEnglish());
        dto.setPincode(address.getPincode());
        dto.setLandmark(namhattaAddress.getLandmark());
        dto.setAddressType(namhattaAddress.getAddressType());
        
        return dto;
    }
    
    // DevotionalStatus mappings
    public DevotionalStatusDTO toDevotionalStatusDTO(DevotionalStatus status) {
        if (status == null) return null;
        
        DevotionalStatusDTO dto = new DevotionalStatusDTO();
        dto.setId(status.getId());
        dto.setName(status.getName());
        dto.setHierarchyLevel(status.getHierarchyLevel());
        dto.setCreatedAt(status.getCreatedAt());
        dto.setUpdatedAt(status.getUpdatedAt());
        
        // Set devotee count if available
        if (status.getDevotees() != null) {
            dto.setDevoteeCount(status.getDevotees().size());
        }
        
        return dto;
    }
    
    public DevotionalStatus toDevotionalStatusEntity(DevotionalStatusDTO dto) {
        if (dto == null) return null;
        
        DevotionalStatus status = new DevotionalStatus();
        status.setId(dto.getId());
        status.setName(dto.getName());
        status.setHierarchyLevel(dto.getHierarchyLevel());
        status.setCreatedAt(dto.getCreatedAt());
        status.setUpdatedAt(dto.getUpdatedAt());
        
        return status;
    }
}