package com.namhatta.dto;

import jakarta.validation.constraints.*;

public class AuthDTO {
    
    public static class LoginRequest {
        @NotBlank(message = "Username is required")
        private String username;
        
        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;
        
        // Constructors
        public LoginRequest() {}
        
        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
        
        // Getters and Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    public static class LoginResponse {
        private String token;
        private UserDTO user;
        private String message;
        
        // Constructors
        public LoginResponse() {}
        
        public LoginResponse(String token, UserDTO user) {
            this.token = token;
            this.user = user;
            this.message = "Login successful";
        }
        
        // Getters and Setters
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        
        public UserDTO getUser() { return user; }
        public void setUser(UserDTO user) { this.user = user; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
    
    public static class VerifyResponse {
        private UserDTO user;
        private Boolean valid;
        private String message;
        
        // Constructors
        public VerifyResponse() {}
        
        public VerifyResponse(UserDTO user, Boolean valid) {
            this.user = user;
            this.valid = valid;
            this.message = valid ? "Token is valid" : "Token is invalid";
        }
        
        // Getters and Setters
        public UserDTO getUser() { return user; }
        public void setUser(UserDTO user) { this.user = user; }
        
        public Boolean getValid() { return valid; }
        public void setValid(Boolean valid) { this.valid = valid; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}