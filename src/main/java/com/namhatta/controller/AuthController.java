package com.namhatta.controller;

import com.namhatta.dto.ApiResponse;
import com.namhatta.dto.AuthDTO;
import com.namhatta.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthDTO.LoginResponse>> login(@Valid @RequestBody AuthDTO.LoginRequest request) {
        try {
            AuthDTO.LoginResponse response = authService.login(request);
            return ResponseEntity.ok(ApiResponse.success("Login successful", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Login failed", e.getMessage()));
        }
    }
    
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<AuthDTO.VerifyResponse>> verify(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String token = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
            
            AuthDTO.VerifyResponse response = authService.verifyToken(token);
            
            if (response.getValid()) {
                return ResponseEntity.ok(ApiResponse.success("Token is valid", response));
            } else {
                return ResponseEntity.status(401)
                    .body(ApiResponse.error("Token verification failed", "Invalid or expired token"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401)
                .body(ApiResponse.error("Token verification failed", e.getMessage()));
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String token = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
            
            authService.logout(token);
            return ResponseEntity.ok(ApiResponse.success("Logout successful"));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.success("Logout completed"));
        }
    }
}