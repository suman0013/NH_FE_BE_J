package com.namhatta.controller;

import com.namhatta.dto.DevoteeDTO;
import com.namhatta.service.DevoteeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for DevoteeController
 */
@WebMvcTest(DevoteeController.class)
class DevoteeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DevoteeService devoteeService;

    @Autowired
    private ObjectMapper objectMapper;

    private DevoteeDTO.Response responseDTO;
    private DevoteeDTO.CreateRequest createRequest;
    private DevoteeDTO.UpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        responseDTO = new DevoteeDTO.Response();
        responseDTO.setId(1L);
        responseDTO.setLegalName("John Doe");
        responseDTO.setSpiritualName("Bhakta John");
        responseDTO.setPhoneNumber("1234567890");
        responseDTO.setEmail("john@example.com");

        createRequest = new DevoteeDTO.CreateRequest();
        createRequest.setLegalName("John Doe");
        createRequest.setSpiritualName("Bhakta John");
        createRequest.setPhoneNumber("1234567890");
        createRequest.setEmail("john@example.com");
        createRequest.setNamhattaId(1L);
        createRequest.setDevotionalStatusId(1L);

        updateRequest = new DevoteeDTO.UpdateRequest();
        updateRequest.setLegalName("John Smith");
        updateRequest.setSpiritualName("Bhakta John");
        updateRequest.setPhoneNumber("0987654321");
        updateRequest.setEmail("john.smith@example.com");
    }

    @Test
    void getAllDevotees_WithDefaultParams_ShouldReturnPagedResults() throws Exception {
        // Arrange
        List<DevoteeDTO.Response> devotees = Arrays.asList(responseDTO);
        Page<DevoteeDTO.Response> page = new PageImpl<>(devotees, PageRequest.of(0, 20), 1);
        when(devoteeService.getAllDevotees(any())).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/devotees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].legalName").value("John Doe"))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.totalPages").value(1));

        verify(devoteeService).getAllDevotees(any());
    }

    @Test
    void getAllDevotees_WithCustomPageParams_ShouldUseProvidedParams() throws Exception {
        // Arrange
        List<DevoteeDTO.Response> devotees = Arrays.asList(responseDTO);
        Page<DevoteeDTO.Response> page = new PageImpl<>(devotees, PageRequest.of(1, 10), 21);
        when(devoteeService.getAllDevotees(any())).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/devotees")
                .param("page", "1")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.number").value(1))
                .andExpect(jsonPath("$.data.size").value(10));

        verify(devoteeService).getAllDevotees(any());
    }

    @Test
    void getDevoteeById_WithExistingId_ShouldReturnDevotee() throws Exception {
        // Arrange
        when(devoteeService.getDevoteeById(1L)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/devotees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.legalName").value("John Doe"))
                .andExpect(jsonPath("$.data.spiritualName").value("Bhakta John"));

        verify(devoteeService).getDevoteeById(1L);
    }

    @Test
    void getDevoteeById_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(devoteeService.getDevoteeById(999L))
                .thenThrow(new RuntimeException("Devotee not found with id: 999"));

        // Act & Assert
        mockMvc.perform(get("/api/devotees/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Devotee not found with id: 999"));

        verify(devoteeService).getDevoteeById(999L);
    }

    @Test
    void createDevotee_WithValidRequest_ShouldReturnCreatedDevotee() throws Exception {
        // Arrange
        when(devoteeService.createDevotee(any(DevoteeDTO.CreateRequest.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/devotees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.legalName").value("John Doe"))
                .andExpect(jsonPath("$.data.spiritualName").value("Bhakta John"));

        verify(devoteeService).createDevotee(any(DevoteeDTO.CreateRequest.class));
    }

    @Test
    void createDevotee_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        // Arrange
        createRequest.setLegalName(""); // Invalid empty name

        // Act & Assert
        mockMvc.perform(post("/api/devotees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());

        verify(devoteeService, never()).createDevotee(any());
    }

    @Test
    void updateDevotee_WithValidRequest_ShouldReturnUpdatedDevotee() throws Exception {
        // Arrange
        when(devoteeService.updateDevotee(eq(1L), any(DevoteeDTO.UpdateRequest.class)))
                .thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/devotees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));

        verify(devoteeService).updateDevotee(eq(1L), any(DevoteeDTO.UpdateRequest.class));
    }

    @Test
    void updateDevotee_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(devoteeService.updateDevotee(eq(999L), any(DevoteeDTO.UpdateRequest.class)))
                .thenThrow(new RuntimeException("Devotee not found with id: 999"));

        // Act & Assert
        mockMvc.perform(put("/api/devotees/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Devotee not found with id: 999"));

        verify(devoteeService).updateDevotee(eq(999L), any(DevoteeDTO.UpdateRequest.class));
    }

    @Test
    void deleteDevotee_WithExistingId_ShouldReturnSuccess() throws Exception {
        // Arrange
        doNothing().when(devoteeService).deleteDevotee(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/devotees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Devotee deleted successfully"));

        verify(devoteeService).deleteDevotee(1L);
    }

    @Test
    void deleteDevotee_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Devotee not found with id: 999"))
                .when(devoteeService).deleteDevotee(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/devotees/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Devotee not found with id: 999"));

        verify(devoteeService).deleteDevotee(999L);
    }

    @Test
    void searchDevoteesByName_WithSearchTerm_ShouldReturnFilteredResults() throws Exception {
        // Arrange
        List<DevoteeDTO.Response> devotees = Arrays.asList(responseDTO);
        Page<DevoteeDTO.Response> page = new PageImpl<>(devotees, PageRequest.of(0, 20), 1);
        when(devoteeService.searchDevoteesByName(eq("John"), any())).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/devotees/search")
                .param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].legalName").value("John Doe"));

        verify(devoteeService).searchDevoteesByName(eq("John"), any());
    }

    @Test
    void searchDevoteesByName_WithoutSearchTerm_ShouldReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/devotees/search"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Search name is required"));

        verify(devoteeService, never()).searchDevoteesByName(anyString(), any());
    }
}