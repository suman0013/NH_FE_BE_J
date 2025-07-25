# Namhatta Management System

A comprehensive web application for managing Namhatta religious/spiritual organizations, built with React frontend and Spring Boot backend.

## Quick Start

1. **Start Spring Boot Backend**
   ```bash
   mvn spring-boot:run
   ```
   Backend API will be available at `http://localhost:8080/api`

2. **Start React Frontend** (in another terminal)
   ```bash
   cd client
   npm install
   npm run dev
   ```
   Frontend will be available at `http://localhost:3000`

## Database Configuration

The application uses a Neon PostgreSQL database. Configure the database connection in `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: ${DATABASE_URL}
    driver-class-name: org.postgresql.Driver
```

Set the `DATABASE_URL` environment variable with your PostgreSQL connection string.

## Architecture

- **Frontend**: React 18 + TypeScript + Tailwind CSS + Vite
- **Backend**: Spring Boot 3.2.1 + Java 17 + JPA/Hibernate
- **Database**: PostgreSQL with Spring Data JPA
- **Security**: Spring Security with JWT authentication

## Key Features

- Devotee management with comprehensive profiles
- Namhatta (spiritual center) administration
- Leadership hierarchy tracking
- Geographic organization by regions
- Real-time updates and notifications
- Responsive mobile-friendly interface

For detailed documentation, see `replit.md`.