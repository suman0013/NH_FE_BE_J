package com.namhatta.controller;

import com.namhatta.service.GeographyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for GeographyController
 */
@WebMvcTest(GeographyController.class)
class GeographyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeographyService geographyService;

    private List<String> mockCountries;
    private List<String> mockStates;
    private Map<String, Object> mockPincodeSearch;
    private Map<String, Object> mockAddressInfo;

    @BeforeEach
    void setUp() {
        mockCountries = Arrays.asList("India", "USA", "UK");
        mockStates = Arrays.asList("West Bengal", "Maharashtra", "Karnataka");
        
        mockPincodeSearch = new HashMap<>();
        mockPincodeSearch.put("pincodes", Arrays.asList("700019", "700020"));
        mockPincodeSearch.put("total", 2L);
        mockPincodeSearch.put("hasMore", false);

        mockAddressInfo = new HashMap<>();
        mockAddressInfo.put("country", "India");
        mockAddressInfo.put("state", "West Bengal");
        mockAddressInfo.put("district", "Kolkata");
        mockAddressInfo.put("subDistricts", Arrays.asList("Ballygunge"));
        mockAddressInfo.put("villages", Arrays.asList("Park Street"));
    }

    @Test
    void getAllCountries_ShouldReturnCountryList() throws Exception {
        // Arrange
        when(geographyService.getAllCountries()).thenReturn(mockCountries);

        // Act & Assert
        mockMvc.perform(get("/api/countries"))
                .andExpect(status().isOk())
                .andExpected(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0]").value("India"))
                .andExpect(jsonPath("$.data[1]").value("USA"))
                .andExpect(jsonPath("$.data[2]").value("UK"));

        verify(geographyService).getAllCountries();
    }

    @Test
    void getStatesByCountry_WithCountryParam_ShouldReturnStateList() throws Exception {
        // Arrange
        when(geographyService.getStatesByCountry("India")).thenReturn(mockStates);

        // Act & Assert
        mockMvc.perform(get("/api/states")
                .param("country", "India"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0]").value("West Bengal"));

        verify(geographyService).getStatesByCountry("India");
    }

    @Test
    void getStatesByCountry_WithoutCountryParam_ShouldReturnAllStates() throws Exception {
        // Arrange
        when(geographyService.getStatesByCountry(null)).thenReturn(mockStates);

        // Act & Assert
        mockMvc.perform(get("/api/states"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());

        verify(geographyService).getStatesByCountry(null);
    }

    @Test
    void getDistrictsByState_WithStateParam_ShouldReturnDistrictList() throws Exception {
        // Arrange
        List<String> mockDistricts = Arrays.asList("Kolkata", "Howrah");
        when(geographyService.getDistrictsByState("West Bengal")).thenReturn(mockDistricts);

        // Act & Assert
        mockMvc.perform(get("/api/districts")
                .param("state", "West Bengal"))
                .andExpect(status().isOk())
                .andExpected(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0]").value("Kolkata"));

        verify(geographyService).getDistrictsByState("West Bengal");
    }

    @Test
    void getSubDistrictsByDistrict_WithDistrictAndPincode_ShouldReturnSubDistrictList() throws Exception {
        // Arrange
        List<String> mockSubDistricts = Arrays.asList("Ballygunge", "Park Street");
        when(geographyService.getSubDistrictsByDistrict("Kolkata", "700019"))
                .thenReturn(mockSubDistricts);

        // Act & Assert
        mockMvc.perform(get("/api/subdistricts")
                .param("district", "Kolkata")
                .param("pincode", "700019"))
                .andExpect(status().isOk())
                .andExpected(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0]").value("Ballygunge"));

        verify(geographyService).getSubDistrictsByDistrict("Kolkata", "700019");
    }

    @Test
    void getVillagesBySubDistrict_ShouldReturnVillageList() throws Exception {
        // Arrange
        List<String> mockVillages = Arrays.asList("Park Street Area", "Camac Street");
        when(geographyService.getVillagesBySubDistrict("Ballygunge", "700019"))
                .thenReturn(mockVillages);

        // Act & Assert
        mockMvc.perform(get("/api/villages")
                .param("subdistrict", "Ballygunge")
                .param("pincode", "700019"))
                .andExpect(status().isOk())
                .andExpected(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpected(jsonPath("$.data[0]").value("Park Street Area"));

        verify(geographyService).getVillagesBySubDistrict("Ballygunge", "700019");
    }

    @Test
    void getPincodes_WithVillageParam_ShouldReturnPincodeList() throws Exception {
        // Arrange
        List<String> mockPincodes = Arrays.asList("700019", "700020");
        when(geographyService.getPincodes("Park Street", null, null)).thenReturn(mockPincodes);

        // Act & Assert
        mockMvc.perform(get("/api/pincodes")
                .param("village", "Park Street"))
                .andExpect(status().isOk())
                .andExpected(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0]").value("700019"));

        verify(geographyService).getPincodes("Park Street", null, null);
    }

    @Test
    void searchPincodes_WithSearchTerm_ShouldReturnSearchResults() throws Exception {
        // Arrange
        when(geographyService.searchPincodes("India", "7000", 0, 10)).thenReturn(mockPincodeSearch);

        // Act & Assert
        mockMvc.perform(get("/api/pincodes/search")
                .param("country", "India")
                .param("search", "7000")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpected(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.pincodes").isArray())
                .andExpect(jsonPath("$.data.total").value(2))
                .andExpect(jsonPath("$.data.hasMore").value(false));

        verify(geographyService).searchPincodes("India", "7000", 0, 10);
    }

    @Test
    void getAddressByPincode_WithValidPincode_ShouldReturnAddressInfo() throws Exception {
        // Arrange
        when(geographyService.getAddressByPincode("700019")).thenReturn(mockAddressInfo);

        // Act & Assert
        mockMvc.perform(get("/api/address/700019"))
                .andExpect(status().isOk())
                .andExpected(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.country").value("India"))
                .andExpect(jsonPath("$.data.state").value("West Bengal"))
                .andExpect(jsonPath("$.data.district").value("Kolkata"));

        verify(geographyService).getAddressByPincode("700019");
    }

    @Test
    void getAddressByPincode_WithInvalidPincode_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(geographyService.getAddressByPincode("999999")).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/address/999999"))
                .andExpect(status().isNotFound())
                .andExpected(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Address not found for pincode: 999999"));

        verify(geographyService).getAddressByPincode("999999");
    }

    @Test
    void searchPincodes_WithMissingCountry_ShouldReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/pincodes/search")
                .param("search", "7000"))
                .andExpect(status().isBadRequest())
                .andExpected(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Country parameter is required"));

        verify(geographyService, never()).searchPincodes(anyString(), anyString(), anyInt(), anyInt());
    }

    @Test
    void getDistrictsByState_WithMissingState_ShouldReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/districts"))
                .andExpect(status().isBadRequest())
                .andExpected(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("State parameter is required"));

        verify(geographyService, never()).getDistrictsByState(anyString());
    }
}