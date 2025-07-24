package com.namhatta.controller;

import com.namhatta.dto.ApiResponse;
import com.namhatta.dto.AuthDTO;
import com.namhatta.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for AuthController
 */
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private AuthDTO.LoginRequest loginRequest;
    private AuthDTO.LoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        loginRequest = new AuthDTO.LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        loginResponse = new AuthDTO.LoginResponse();
        loginResponse.setToken("jwt-token");
        loginResponse.setUsername("testuser");
        loginResponse.setRole("ADMIN");
        loginResponse.setSuccess(true);
    }

    @Test
    void login_WithValidCredentials_ShouldReturnToken() throws Exception {
        // Arrange
        when(authService.login(any(AuthDTO.LoginRequest.class))).thenReturn(loginResponse);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").value("jwt-token"))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.role").value("ADMIN"));

        verify(authService).login(any(AuthDTO.LoginRequest.class));
    }

    @Test
    void login_WithInvalidCredentials_ShouldReturnError() throws Exception {
        // Arrange
        when(authService.login(any(AuthDTO.LoginRequest.class)))
                .thenThrow(new RuntimeException("Invalid username or password"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Invalid username or password"));

        verify(authService).login(any(AuthDTO.LoginRequest.class));
    }

    @Test
    void login_WithEmptyBody_ShouldReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());

        verify(authService, never()).login(any());
    }

    @Test
    void verifyToken_WithValidToken_ShouldReturnSuccess() throws Exception {
        // Arrange
        when(authService.validateToken("valid-token")).thenReturn(true);
        when(authService.getUsernameFromToken("valid-token")).thenReturn("testuser");

        // Act & Assert
        mockMvc.perform(get("/api/auth/verify")
                .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.valid").value(true))
                .andExpect(jsonPath("$.data.username").value("testuser"));

        verify(authService).validateToken("valid-token");
        verify(authService).getUsernameFromToken("valid-token");
    }

    @Test
    void verifyToken_WithInvalidToken_ShouldReturnUnauthorized() throws Exception {
        // Arrange
        when(authService.validateToken("invalid-token")).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/auth/verify")
                .header("Authorization", "Bearer invalid-token"))
                .andExpected(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Invalid token"));

        verify(authService).validateToken("invalid-token");
        verify(authService, never()).getUsernameFromToken(anyString());
    }

    @Test
    void verifyToken_WithoutToken_ShouldReturnUnauthorized() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/auth/verify"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("No token provided"));

        verify(authService, never()).validateToken(anyString());
    }

    @Test
    void logout_ShouldReturnSuccess() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Logged out successfully"));
    }

    @Test
    void healthCheck_ShouldReturnSuccess() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("OK"))
                .andExpect(jsonPath("$.data.service").value("Spring Boot Namhatta Management System"));
    }
}