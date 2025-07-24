package com.namhatta.service;

import com.namhatta.entity.Address;
import com.namhatta.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for GeographyService
 */
@ExtendWith(MockitoExtension.class)
class GeographyServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private GeographyService geographyService;

    private Address testAddress;

    @BeforeEach
    void setUp() {
        testAddress = new Address();
        testAddress.setId(1L);
        testAddress.setCountry("India");
        testAddress.setStateNameEnglish("West Bengal");
        testAddress.setDistrictNameEnglish("Kolkata");
        testAddress.setSubdistrictNameEnglish("Ballygunge");
        testAddress.setVillageNameEnglish("Park Street");
        testAddress.setPincode("700019");
    }

    @Test
    void getAllCountries_ShouldReturnDistinctCountries() {
        // Arrange
        List<String> mockCountries = Arrays.asList("India", "USA", "UK");
        when(addressRepository.findDistinctCountries()).thenReturn(mockCountries);

        // Act
        List<String> result = geographyService.getAllCountries();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("India"));
        assertTrue(result.contains("USA"));
        assertTrue(result.contains("UK"));

        verify(addressRepository).findDistinctCountries();
    }

    @Test
    void getStatesByCountry_WithCountry_ShouldReturnStatesByCountry() {
        // Arrange
        List<String> mockStates = Arrays.asList("West Bengal", "Maharashtra", "Karnataka");
        when(addressRepository.findDistinctStatesByCountry("India")).thenReturn(mockStates);

        // Act
        List<String> result = geographyService.getStatesByCountry("India");

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("West Bengal"));

        verify(addressRepository).findDistinctStatesByCountry("India");
        verify(addressRepository, never()).findDistinctStates();
    }

    @Test
    void getStatesByCountry_WithNullCountry_ShouldReturnAllStates() {
        // Arrange
        List<String> mockStates = Arrays.asList("West Bengal", "Maharashtra", "Karnataka");
        when(addressRepository.findDistinctStates()).thenReturn(mockStates);

        // Act
        List<String> result = geographyService.getStatesByCountry(null);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());

        verify(addressRepository, never()).findDistinctStatesByCountry(any());
        verify(addressRepository).findDistinctStates();
    }

    @Test
    void getDistrictsByState_WithState_ShouldReturnDistrictsByState() {
        // Arrange
        List<String> mockDistricts = Arrays.asList("Kolkata", "Howrah", "North 24 Parganas");
        when(addressRepository.findDistinctDistrictsByState("West Bengal")).thenReturn(mockDistricts);

        // Act
        List<String> result = geographyService.getDistrictsByState("West Bengal");

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("Kolkata"));

        verify(addressRepository).findDistinctDistrictsByState("West Bengal");
    }

    @Test
    void getSubDistrictsByDistrict_WithDistrictAndPincode_ShouldReturnFilteredSubDistricts() {
        // Arrange
        List<String> mockSubDistricts = Arrays.asList("Ballygunge", "Park Street");
        when(addressRepository.findDistinctSubDistrictsByDistrictAndPincode("Kolkata", "700019"))
            .thenReturn(mockSubDistricts);

        // Act
        List<String> result = geographyService.getSubDistrictsByDistrict("Kolkata", "700019");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("Ballygunge"));

        verify(addressRepository).findDistinctSubDistrictsByDistrictAndPincode("Kolkata", "700019");
    }

    @Test
    void getSubDistrictsByDistrict_WithOnlyDistrict_ShouldReturnAllSubDistricts() {
        // Arrange
        List<String> mockSubDistricts = Arrays.asList("Ballygunge", "Park Street", "Gariahat");
        when(addressRepository.findDistinctSubDistrictsByDistrict("Kolkata")).thenReturn(mockSubDistricts);

        // Act
        List<String> result = geographyService.getSubDistrictsByDistrict("Kolkata", null);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());

        verify(addressRepository).findDistinctSubDistrictsByDistrict("Kolkata");
    }

    @Test
    void getVillagesBySubDistrict_WithSubDistrictAndPincode_ShouldReturnFilteredVillages() {
        // Arrange
        List<String> mockVillages = Arrays.asList("Park Street Area", "Camac Street");
        when(addressRepository.findDistinctVillagesBySubDistrictAndPincode("Ballygunge", "700019"))
            .thenReturn(mockVillages);

        // Act
        List<String> result = geographyService.getVillagesBySubDistrict("Ballygunge", "700019");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("Park Street Area"));

        verify(addressRepository).findDistinctVillagesBySubDistrictAndPincode("Ballygunge", "700019");
    }

    @Test
    void getPincodes_WithVillage_ShouldReturnPincodesByVillage() {
        // Arrange
        List<String> mockPincodes = Arrays.asList("700019", "700020");
        when(addressRepository.findDistinctPincodesByVillage("Park Street")).thenReturn(mockPincodes);

        // Act
        List<String> result = geographyService.getPincodes("Park Street", null, null);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("700019"));

        verify(addressRepository).findDistinctPincodesByVillage("Park Street");
    }

    @Test
    void searchPincodes_WithCountryAndSearchTerm_ShouldReturnPagedResults() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<String> mockPincodes = Arrays.asList("700019", "700020", "700021");
        Page<String> mockPage = new PageImpl<>(mockPincodes, pageable, 3);
        
        when(addressRepository.searchPincodesByCountryAndTerm("India", "7000", pageable))
            .thenReturn(mockPage);

        // Act
        Map<String, Object> result = geographyService.searchPincodes("India", "7000", 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(mockPincodes, result.get("pincodes"));
        assertEquals(3L, result.get("total"));
        assertEquals(false, result.get("hasMore"));

        verify(addressRepository).searchPincodesByCountryAndTerm("India", "7000", pageable);
    }

    @Test
    void searchPincodes_WithEmptySearchTerm_ShouldReturnAllPincodes() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<String> mockPincodes = Arrays.asList("700019", "700020");
        Page<String> mockPage = new PageImpl<>(mockPincodes, pageable, 2);
        
        when(addressRepository.findPincodesByCountry("India", pageable)).thenReturn(mockPage);

        // Act
        Map<String, Object> result = geographyService.searchPincodes("India", "", 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(mockPincodes, result.get("pincodes"));
        assertEquals(2L, result.get("total"));

        verify(addressRepository).findPincodesByCountry("India", pageable);
    }

    @Test
    void getAddressByPincode_WithValidPincode_ShouldReturnAddressInfo() {
        // Arrange
        List<Address> mockAddresses = Arrays.asList(testAddress);
        when(addressRepository.findByPincode("700019")).thenReturn(mockAddresses);

        // Act
        Map<String, Object> result = geographyService.getAddressByPincode("700019");

        // Assert
        assertNotNull(result);
        assertEquals("India", result.get("country"));
        assertEquals("West Bengal", result.get("state"));
        assertEquals("Kolkata", result.get("district"));
        assertNotNull(result.get("subDistricts"));
        assertNotNull(result.get("villages"));

        verify(addressRepository).findByPincode("700019");
    }

    @Test
    void getAddressByPincode_WithInvalidPincode_ShouldReturnNull() {
        // Arrange
        when(addressRepository.findByPincode("999999")).thenReturn(Arrays.asList());

        // Act
        Map<String, Object> result = geographyService.getAddressByPincode("999999");

        // Assert
        assertNull(result);
        verify(addressRepository).findByPincode("999999");
    }

    @Test
    void getAddressByPincode_WithNullPincode_ShouldReturnNull() {
        // Act
        Map<String, Object> result = geographyService.getAddressByPincode(null);

        // Assert
        assertNull(result);
        verify(addressRepository, never()).findByPincode(any());
    }

    @Test
    void getAddressByPincode_WithEmptyPincode_ShouldReturnNull() {
        // Act
        Map<String, Object> result = geographyService.getAddressByPincode("   ");

        // Assert
        assertNull(result);
        verify(addressRepository, never()).findByPincode(any());
    }
}