package com.namhatta.controller;

import com.namhatta.dto.NamhattaDTO;
import com.namhatta.service.NamhattaService;
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
 * Unit tests for NamhattaController
 */
@WebMvcTest(NamhattaController.class)
class NamhattaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NamhattaService namhattaService;

    @Autowired
    private ObjectMapper objectMapper;

    private NamhattaDTO.Response responseDTO;
    private NamhattaDTO.CreateRequest createRequest;
    private NamhattaDTO.UpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        responseDTO = new NamhattaDTO.Response();
        responseDTO.setId(1L);
        responseDTO.setCode("TEST001");
        responseDTO.setName("Test Namhatta");
        responseDTO.setMeetingDay("Sunday");
        responseDTO.setMeetingTime("10:00 AM");
        responseDTO.setSecretary("Test Secretary");
        responseDTO.setStatus("APPROVED");

        createRequest = new NamhattaDTO.CreateRequest();
        createRequest.setCode("TEST001");
        createRequest.setName("Test Namhatta");
        createRequest.setMeetingDay("Sunday");
        createRequest.setMeetingTime("10:00 AM");
        createRequest.setSecretary("Test Secretary");

        updateRequest = new NamhattaDTO.UpdateRequest();
        updateRequest.setName("Updated Namhatta");
        updateRequest.setMeetingDay("Saturday");
        updateRequest.setMeetingTime("2:00 PM");
        updateRequest.setSecretary("Updated Secretary");
    }

    @Test
    void getAllNamhattas_WithDefaultParams_ShouldReturnPagedResults() throws Exception {
        // Arrange
        List<NamhattaDTO.Response> namhattas = Arrays.asList(responseDTO);
        Page<NamhattaDTO.Response> page = new PageImpl<>(namhattas, PageRequest.of(0, 20), 1);
        when(namhattaService.getAllNamhattas(any())).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/namhattas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].name").value("Test Namhatta"))
                .andExpect(jsonPath("$.data.content[0].code").value("TEST001"))
                .andExpect(jsonPath("$.data.totalElements").value(1));

        verify(namhattaService).getAllNamhattas(any());
    }

    @Test
    void getNamhattaById_WithExistingId_ShouldReturnNamhatta() throws Exception {
        // Arrange
        when(namhattaService.getNamhattaById(1L)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/namhattas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Test Namhatta"))
                .andExpect(jsonPath("$.data.code").value("TEST001"))
                .andExpect(jsonPath("$.data.status").value("APPROVED"));

        verify(namhattaService).getNamhattaById(1L);
    }

    @Test
    void createNamhatta_WithValidRequest_ShouldReturnCreatedNamhatta() throws Exception {
        // Arrange
        when(namhattaService.createNamhatta(any(NamhattaDTO.CreateRequest.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/namhattas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Test Namhatta"))
                .andExpect(jsonPath("$.data.code").value("TEST001"));

        verify(namhattaService).createNamhatta(any(NamhattaDTO.CreateRequest.class));
    }

    @Test
    void createNamhatta_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        // Arrange
        createRequest.setName(""); // Invalid empty name

        // Act & Assert
        mockMvc.perform(post("/api/namhattas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());

        verify(namhattaService, never()).createNamhatta(any());
    }

    @Test
    void updateNamhatta_WithValidRequest_ShouldReturnUpdatedNamhatta() throws Exception {
        // Arrange
        when(namhattaService.updateNamhatta(eq(1L), any(NamhattaDTO.UpdateRequest.class)))
                .thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/namhattas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));

        verify(namhattaService).updateNamhatta(eq(1L), any(NamhattaDTO.UpdateRequest.class));
    }

    @Test
    void approveNamhatta_WithExistingId_ShouldReturnSuccess() throws Exception {
        // Arrange
        responseDTO.setStatus("APPROVED");
        when(namhattaService.approveNamhatta(1L)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/namhattas/1/approve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("APPROVED"));

        verify(namhattaService).approveNamhatta(1L);
    }

    @Test
    void rejectNamhatta_WithExistingId_ShouldReturnSuccess() throws Exception {
        // Arrange
        responseDTO.setStatus("REJECTED");
        when(namhattaService.rejectNamhatta(1L)).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/namhattas/1/reject"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("REJECTED"));

        verify(namhattaService).rejectNamhatta(1L);
    }

    @Test
    void getPendingNamhattas_ShouldReturnPendingOnly() throws Exception {
        // Arrange
        responseDTO.setStatus("PENDING_APPROVAL");
        List<NamhattaDTO.Response> pendingNamhattas = Arrays.asList(responseDTO);
        Page<NamhattaDTO.Response> page = new PageImpl<>(pendingNamhattas, PageRequest.of(0, 20), 1);
        when(namhattaService.getPendingNamhattas(any())).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/namhattas/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].status").value("PENDING_APPROVAL"));

        verify(namhattaService).getPendingNamhattas(any());
    }

    @Test
    void checkCodeUniqueness_WithUniqueCode_ShouldReturnTrue() throws Exception {
        // Arrange
        when(namhattaService.isCodeUnique("UNIQUE001")).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/namhattas/check-code")
                .param("code", "UNIQUE001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.isUnique").value(true));

        verify(namhattaService).isCodeUnique("UNIQUE001");
    }

    @Test
    void checkCodeUniqueness_WithExistingCode_ShouldReturnFalse() throws Exception {
        // Arrange
        when(namhattaService.isCodeUnique("EXISTING001")).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/namhattas/check-code")
                .param("code", "EXISTING001"))
                .andExpected(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.isUnique").value(false));

        verify(namhattaService).isCodeUnique("EXISTING001");
    }

    @Test
    void checkCodeUniqueness_WithoutCode_ShouldReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/namhattas/check-code"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Code parameter is required"));

        verify(namhattaService, never()).isCodeUnique(anyString());
    }
}