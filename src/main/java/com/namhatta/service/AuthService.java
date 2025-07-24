package com.namhatta.service;

import com.namhatta.dto.AuthDTO;
import com.namhatta.dto.UserDTO;
import com.namhatta.entity.User;
import com.namhatta.mapper.EntityMapper;
import com.namhatta.repository.UserRepository;
import com.namhatta.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Autowired
    private EntityMapper entityMapper;
    
    public AuthDTO.LoginResponse login(AuthDTO.LoginRequest request) {
        try {
            // Find user by username
            Optional<User> userOpt = userRepository.findByUsernameAndActive(request.getUsername(), true);
            if (userOpt.isEmpty()) {
                throw new RuntimeException("Invalid username or password");
            }
            
            User user = userOpt.get();
            
            // Verify password
            if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
                throw new RuntimeException("Invalid username or password");
            }
            
            // Generate JWT token
            String token = tokenProvider.generateToken(user.getUsername());
            
            // Convert to DTO
            UserDTO userDTO = entityMapper.toUserDTO(user);
            
            return new AuthDTO.LoginResponse(token, userDTO);
            
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }
    
    public AuthDTO.VerifyResponse verifyToken(String token) {
        try {
            if (token == null || !tokenProvider.validateToken(token)) {
                return new AuthDTO.VerifyResponse(null, false);
            }
            
            String username = tokenProvider.getUsernameFromToken(token);
            Optional<User> userOpt = userRepository.findByUsernameWithDistricts(username);
            
            if (userOpt.isEmpty() || !userOpt.get().getActive()) {
                return new AuthDTO.VerifyResponse(null, false);
            }
            
            UserDTO userDTO = entityMapper.toUserDTO(userOpt.get());
            return new AuthDTO.VerifyResponse(userDTO, true);
            
        } catch (Exception e) {
            return new AuthDTO.VerifyResponse(null, false);
        }
    }
    
    public void logout(String token) {
        // In a more complete implementation, we would add token to blacklist
        // For now, we rely on client-side token removal
    }
    
    public User createUser(String username, String password, String role) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(role);
        user.setActive(true);
        
        return userRepository.save(user);
    }
}