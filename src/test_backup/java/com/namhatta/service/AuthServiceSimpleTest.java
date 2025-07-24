package com.namhatta.service;

import com.namhatta.dto.AuthDTO;
import com.namhatta.entity.User;
import com.namhatta.repository.UserRepository;
import com.namhatta.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AuthService - Simplified version
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceSimpleTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private AuthDTO loginRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPasswordHash("hashedPassword");
        testUser.setRole("ADMIN");
        testUser.setActive(true);

        loginRequest = new AuthDTO();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");
    }

    @Test
    void login_WithValidCredentials_ShouldReturnToken() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(jwtTokenProvider.generateToken(anyString())).thenReturn("jwt-token");

        // Act
        AuthDTO response = authService.login(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("testuser", response.getUsername());
        assertEquals("ADMIN", response.getRole());
        assertTrue(response.isSuccess());

        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder).matches("password123", "hashedPassword");
        verify(jwtTokenProvider).generateToken("testuser");
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnTrue() {
        // Arrange
        when(jwtTokenProvider.validateToken("valid-token")).thenReturn(true);

        // Act
        boolean result = authService.validateToken("valid-token");

        // Assert
        assertTrue(result);
        verify(jwtTokenProvider).validateToken("valid-token");
    }
}