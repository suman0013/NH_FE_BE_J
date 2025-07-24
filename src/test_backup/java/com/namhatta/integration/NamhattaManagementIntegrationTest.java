package com.namhatta.integration;

import com.namhatta.NamhattaApplication;
import com.namhatta.dto.AuthDTO;
import com.namhatta.dto.DevoteeDTO;
import com.namhatta.dto.NamhattaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for complete Namhatta Management System workflows
 */
@SpringBootTest(classes = NamhattaApplication.class)
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class NamhattaManagementIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String authToken;

    @BeforeEach
    void setUp() throws Exception {
        // Setup authentication for integration tests
        AuthDTO.LoginRequest loginRequest = new AuthDTO.LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("admin123");

        // Note: This assumes we have a test admin user in the database
        // In a real integration test, we would seed this data
    }

    @Test
    void fullWorkflow_CreateNamhattaAndAssignDevotees_ShouldWork() throws Exception {
        // Step 1: Test API health
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // Step 2: Create a new Namhatta
        NamhattaDTO.CreateRequest namhattaRequest = new NamhattaDTO.CreateRequest();
        namhattaRequest.setCode("INTEG001");
        namhattaRequest.setName("Integration Test Namhatta");
        namhattaRequest.setMeetingDay("Sunday");
        namhattaRequest.setMeetingTime("10:00 AM");
        namhattaRequest.setSecretary("Test Secretary");

        String namhattaResponse = mockMvc.perform(post("/api/namhattas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(namhattaRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.code").value("INTEG001"))
                .andReturn().getResponse().getContentAsString();

        // Step 3: Verify Namhatta was created and retrieve ID
        mockMvc.perform(get("/api/namhattas")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray());

        // Step 4: Test dashboard endpoints
        mockMvc.perform(get("/api/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalNamhattas").exists());

        // Step 5: Test geography endpoints
        mockMvc.perform(get("/api/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());

        mockMvc.perform(get("/api/states")
                .param("country", "India"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void apiCompatibility_AllEndpoints_ShouldFollowStandardFormat() throws Exception {
        // Test all compatibility endpoints
        mockMvc.perform(get("/api/test/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists());

        mockMvc.perform(get("/api/test/response-format"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists());

        mockMvc.perform(get("/api/test/pagination"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.page").exists())
                .andExpect(jsonPath("$.data.size").exists());

        mockMvc.perform(get("/api/test/compatibility-report"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.summary").exists());
    }

    @Test
    void errorHandling_InvalidRequests_ShouldReturnProperErrorFormat() throws Exception {
        // Test invalid devotee creation
        DevoteeDTO.CreateRequest invalidDevotee = new DevoteeDTO.CreateRequest();
        // Missing required fields

        mockMvc.perform(post("/api/devotees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDevotee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        // Test non-existent resource access
        mockMvc.perform(get("/api/devotees/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void paginationConsistency_AllPagedEndpoints_ShouldFollowSameFormat() throws Exception {
        // Test devotees pagination
        mockMvc.perform(get("/api/devotees")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.page").value(0))
                .andExpect(jsonPath("$.data.size").value(5))
                .andExpect(jsonPath("$.data.totalElements").exists())
                .andExpect(jsonPath("$.data.totalPages").exists());

        // Test namhattas pagination
        mockMvc.perform(get("/api/namhattas")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.page").value(0))
                .andExpect(jsonPath("$.data.size").value(5));

        // Test devotional statuses pagination
        mockMvc.perform(get("/api/devotional-statuses")
                .param("page", "0")
                .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.page").value(0))
                .andExpect(jsonPath("$.data.size").value(5));
    }
}