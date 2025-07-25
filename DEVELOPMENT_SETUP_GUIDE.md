# Development Setup Guide

## Complete Development Environment Configuration

This guide provides multiple options for running the Namhatta Management System in development mode.

## Quick Start Options

### Option 1: Full Stack Development (Recommended)
Start both React frontend and Spring Boot backend simultaneously:
```bash
./start-fullstack.sh
```
- **Frontend**: http://localhost:3000 (React + Vite)
- **Backend**: http://localhost:8080 (Spring Boot)
- **Proxy**: Frontend automatically proxies API calls to backend
- **Auto-restart**: Both services restart on file changes

### Option 2: Separate Development
Run frontend and backend in separate terminals:

**Terminal 1 (Frontend)**:
```bash
./start-frontend.sh
```

**Terminal 2 (Backend)**:
```bash
./run-spring-boot.sh
```

### Option 3: Frontend Only
For UI development without backend:
```bash
./start-frontend.sh
```

### Option 4: Backend Only  
For API development and testing:
```bash
./run-spring-boot.sh
```

## Architecture Overview

### Frontend (React + Vite)
- **Framework**: React 18 with TypeScript
- **Build Tool**: Vite for fast development
- **UI Library**: Tailwind CSS + Radix UI components  
- **State Management**: TanStack Query for server state
- **Routing**: Wouter for client-side routing

### Backend (Spring Boot)
- **Framework**: Spring Boot 3.2.1 with Java 17
- **Database**: PostgreSQL with JPA/Hibernate
- **Security**: Spring Security with JWT authentication
- **API**: RESTful endpoints with OpenAPI documentation
- **Build**: Maven for dependency management

## Development URLs

| Service | URL | Description |
|---------|-----|-------------|
| Frontend | http://localhost:3000 | React development server |
| Backend API | http://localhost:8080/api | Spring Boot REST API |
| Health Check | http://localhost:8080/actuator/health | Backend health status |
| Swagger UI | http://localhost:8080/swagger-ui.html | API documentation |

## Environment Configuration

### Backend Environment (.env.local)
```env
# Database
DATABASE_URL=jdbc:postgresql://localhost:5432/namhatta

# JWT Security
JWT_SECRET=your-super-secret-jwt-key-at-least-32-characters-long

# Server Configuration
PORT=8080
SPRING_PROFILES_ACTIVE=dev
```

### Frontend Environment (client/.env.local)
```env
# API Configuration
VITE_API_BASE_URL=http://localhost:8080/api
VITE_APP_TITLE=Namhatta Management System
VITE_ENVIRONMENT=development
VITE_AUTHENTICATION_ENABLED=true
```

## Development Workflow

### 1. Initial Setup
```bash
# Install dependencies
npm install
cd client && npm install && cd ..

# Build Spring Boot
mvn clean compile

# Start development
./start-fullstack.sh
```

### 2. Daily Development
```bash
# Quick start (if dependencies already installed)
./start-fullstack.sh
```

### 3. Database Setup
1. Configure PostgreSQL connection in `.env.local`
2. Run database migrations (if any)
3. Seed initial data

## Script Details

### start-fullstack.sh
- Automatically installs missing dependencies
- Builds Spring Boot application if needed
- Starts both services with proper process management
- Configures service restarts on file changes
- Shows colored output for easy debugging

### start-frontend.sh
- Starts Vite development server on port 3000
- Enables hot module replacement
- Proxies API calls to backend (if running)
- Provides development tools and error overlays

### run-spring-boot.sh  
- Compiles Java sources
- Starts Spring Boot application on port 8080
- Enables auto-restart on Java file changes
- Configures development profile

## Development Features

### Hot Reloading
- **Frontend**: Instant updates on save
- **Backend**: Automatic restart on Java changes
- **Database**: Schema updates with JPA

### Debugging
- **Frontend**: React DevTools, Vite error overlay
- **Backend**: Spring Boot DevTools, detailed logging
- **API**: Swagger UI for endpoint testing

### Testing
- **Frontend**: Vitest for unit/component tests
- **Backend**: JUnit 5 for unit/integration tests
- **API**: Postman/curl for endpoint testing

## Port Configuration

### Default Ports
- **3000**: React development server
- **8080**: Spring Boot application
- **5432**: PostgreSQL database (if local)

### Port Conflicts
If ports are busy, scripts will:
1. Kill existing processes on those ports
2. Start services on available ports
3. Update proxy configuration automatically

## Performance Optimization

### Development Mode
- Fast rebuilds with Vite
- Incremental compilation with Maven
- Hot reloading for instant feedback
- Source maps for debugging

### Production Build
```bash
# Frontend build
cd client && npm run build

# Backend build
mvn clean package

# Run production
java -jar target/namhatta-management-system-1.0.0.jar
```

## Troubleshooting

### Common Issues

#### Java Version
```bash
# Check Java version (needs 17+)
java -version

# Install Java 17 if needed
# Replit provides this automatically
```

#### Node.js Dependencies
```bash
# Clear cache and reinstall
rm -rf node_modules client/node_modules
npm install
cd client && npm install
```

#### Database Connection
```bash
# Check database status
curl http://localhost:8080/actuator/health

# Verify connection string in .env.local
DATABASE_URL=jdbc:postgresql://...
```

#### Port Conflicts
```bash
# Kill processes on specific ports
sudo lsof -ti:3000 | xargs kill -9
sudo lsof -ti:8080 | xargs kill -9
```

### Debug Logs

#### Frontend Logs
- Vite development server logs
- Browser console errors
- Network tab for API calls

#### Backend Logs  
- Spring Boot startup logs
- Application logs in console
- JPA SQL queries (if enabled)

## IDE Configuration

### VS Code
Recommended extensions:
- Java Extension Pack
- React snippets
- Tailwind CSS IntelliSense
- Spring Boot Tools

### IntelliJ IDEA
- Import as Maven project
- Enable Spring Boot run configuration
- Configure hot reload settings

## Team Development

### Version Control
- Use feature branches for development
- Run tests before pushing
- Keep database migrations in version control

### Environment Consistency
- Share .env.example templates
- Document setup requirements
- Use same Node.js/Java versions

### Code Standards
- ESLint for frontend code
- Checkstyle for backend code
- Prettier for formatting

## Production Deployment

### Build Process
```bash
# Prepare for production
./prepare-for-import.sh

# Build both services
npm run build
mvn clean package
```

### Deployment Options
- Replit autoscale deployment
- Docker containers
- Traditional server deployment

This setup ensures consistent development experience across different environments and team members.