package com.namhatta.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Namhatta Management System API")
                        .description("Complete API documentation for the Namhatta Management System - a comprehensive platform for managing devotees, Namhattas (spiritual centers), hierarchical leadership structures, and devotional statuses.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Namhatta Management System")
                                .email("support@namhatta.org"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080/api")
                                .description("Development Server"),
                        new Server()
                                .url("https://namhatta-management.replit.app/api")
                                .description("Production Server")
                ));
    }
}