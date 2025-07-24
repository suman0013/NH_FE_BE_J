package com.namhatta.controller;

import com.namhatta.dto.DevotionalStatusDTO;
import com.namhatta.service.DevotionalStatusService;
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
 * Unit tests for DevotionalStatusController
 */
@WebMvcTest(DevotionalStatusController.class)
class DevotionalStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DevotionalStatusService devotionalStatusService;

    @Autowired
    private ObjectMapper objectMapper;

    private DevotionalStatusDTO.Response responseDTO;
    private DevotionalStatusDTO.CreateRequest createRequest;

    @BeforeEach
    void setUp() {
        responseDTO = new DevotionalStatusDTO.Response();
        responseDTO.setId(1L);
        responseDTO.setName("Bhakta");

        createRequest = new DevotionalStatusDTO.CreateRequest();
        createRequest.setName("Initiated");
    }

    @Test
    void getAllStatuses_WithPageable_ShouldReturnPagedResults() throws Exception {
        // Arrange
        List<DevotionalStatusDTO.Response> statuses = Arrays.asList(responseDTO);
        Page<DevotionalStatusDTO.Response> page = new PageImpl<>(statuses, PageRequest.of(0, 20), 1);
        when(devotionalStatusService.getAllStatuses(any())).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/devotional-statuses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].name").value("Bhakta"))
                .andExpect(jsonPath("$.data.totalElements").value(1));

        verify(devotionalStatusService).getAllStatuses(any());
    }

    @Test
    void getAllStatuses_WithoutPagination_ShouldReturnAllResults() throws Exception {
        // Arrange
        List<DevotionalStatusDTO.Response> statuses = Arrays.asList(responseDTO);
        when(devotionalStatusService.getAllStatuses()).thenReturn(statuses);

        // Act & Assert
        mockMvc.perform(get("/api/devotional-statuses/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("Bhakta"));

        verify(devotionalStatusService).getAllStatuses();
    }

    @Test
    void getStatusById_WithExistingId_ShouldReturnStatus() throws Exception {
        // Arrange
        when(devotionalStatusService.getStatusById(1L)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/devotional-statuses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Bhakta"));

        verify(devotionalStatusService).getStatusById(1L);
    }

    @Test
    void createStatus_WithValidRequest_ShouldReturnCreatedStatus() throws Exception {
        // Arrange
        when(devotionalStatusService.createStatus(any(DevotionalStatusDTO.CreateRequest.class)))
                .thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/devotional-statuses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Bhakta"));

        verify(devotionalStatusService).createStatus(any(DevotionalStatusDTO.CreateRequest.class));
    }

    @Test
    void updateStatusName_WithValidRequest_ShouldReturnUpdatedStatus() throws Exception {
        // Arrange
        when(devotionalStatusService.updateStatusName(1L, "Updated Status")).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/devotional-statuses/1")
                .param("name", "Updated Status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Bhakta"));

        verify(devotionalStatusService).updateStatusName(1L, "Updated Status");
    }

    @Test
    void deleteStatus_WithExistingId_ShouldReturnSuccess() throws Exception {
        // Arrange
        doNothing().when(devotionalStatusService).deleteStatus(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/devotional-statuses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("DevotionalStatus deleted successfully"));

        verify(devotionalStatusService).deleteStatus(1L);
    }

    @Test
    void checkNameUniqueness_WithUniqueName_ShouldReturnTrue() throws Exception {
        // Arrange
        when(devotionalStatusService.isNameUnique("Unique Status")).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/devotional-statuses/check-name")
                .param("name", "Unique Status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.isUnique").value(true));

        verify(devotionalStatusService).isNameUnique("Unique Status");
    }
}