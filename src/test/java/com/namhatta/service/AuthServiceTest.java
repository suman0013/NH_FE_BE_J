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
 * Unit tests for AuthService
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private AuthDTO.LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPasswordHash("hashedPassword");
        testUser.setRole("ADMIN");
        testUser.setActive(true);

        loginRequest = new AuthDTO.LoginRequest();
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
        AuthDTO.LoginResponse response = authService.login(loginRequest);

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
    void login_WithInvalidUsername_ShouldThrowException() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> authService.login(loginRequest));
        
        assertEquals("Invalid username or password", exception.getMessage());
        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtTokenProvider, never()).generateToken(anyString());
    }

    @Test
    void login_WithInvalidPassword_ShouldThrowException() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> authService.login(loginRequest));
        
        assertEquals("Invalid username or password", exception.getMessage());
        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder).matches("password123", "hashedPassword");
        verify(jwtTokenProvider, never()).generateToken(anyString());
    }

    @Test
    void login_WithInactiveUser_ShouldThrowException() {
        // Arrange
        testUser.setActive(false);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> authService.login(loginRequest));
        
        assertEquals("User account is inactive", exception.getMessage());
        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
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

    @Test
    void validateToken_WithInvalidToken_ShouldReturnFalse() {
        // Arrange
        when(jwtTokenProvider.validateToken("invalid-token")).thenReturn(false);

        // Act
        boolean result = authService.validateToken("invalid-token");

        // Assert
        assertFalse(result);
        verify(jwtTokenProvider).validateToken("invalid-token");
    }

    @Test
    void getUsernameFromToken_WithValidToken_ShouldReturnUsername() {
        // Arrange
        when(jwtTokenProvider.getUsernameFromToken("valid-token")).thenReturn("testuser");

        // Act
        String username = authService.getUsernameFromToken("valid-token");

        // Assert
        assertEquals("testuser", username);
        verify(jwtTokenProvider).getUsernameFromToken("valid-token");
    }
}