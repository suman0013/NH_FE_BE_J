# Namhatta Management System - Development Setup Guide

## Development Setup Options

### Option 1: Concurrent Development (Recommended)
Run both React and Spring Boot simultaneously with one command.

#### Setup:
```bash
# Install concurrently if not present
npm install concurrently --save-dev

# Add to package.json scripts
"dev:fullstack": "concurrently \"npm run dev:frontend\" \"npm run dev:backend\"",
"dev:frontend": "cd client && npm run dev",
"dev:backend": "./run-spring-boot.sh"
```

#### Usage:
```bash
npm run dev:fullstack
```

**Ports:**
- Frontend: http://localhost:3000 (Vite dev server)
- Backend: http://localhost:8080 (Spring Boot)

**Pros:**
- Single command startup
- Auto-restart on file changes
- Color-coded logs
- Easy to manage

---

### Option 2: Separate Terminals (Manual Control)
Run frontend and backend in separate terminal sessions.

#### Terminal 1 (Frontend):
```bash
cd client
npm run dev
```

#### Terminal 2 (Backend):
```bash
# Using Maven directly
mvn spring-boot:run

# OR using the shell script
./run-spring-boot.sh

# OR using Java directly
java -jar target/namhatta-management-system-1.0.0.jar
```

**Pros:**
- Full control over each service
- Easy debugging
- Independent restart capability

---

### Option 3: Replit Workflows (Platform Native)
Use Replit's built-in workflow system.

#### Frontend Workflow:
```yaml
# .replit-frontend
run = "cd client && npm run dev"
```

#### Backend Workflow:
```yaml
# .replit-backend  
run = "mvn spring-boot:run"
```

**Usage:**
- Click workflow buttons in Replit
- Monitor logs separately
- Automatic port detection

---

### Option 4: Docker Compose (Production-like)
Containerized development environment.

#### docker-compose.yml:
```yaml
version: '3.8'
services:
  frontend:
    build: ./client
    ports:
      - "3000:3000"
    volumes:
      - ./client:/app
    command: npm run dev
    
  backend:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=dev
    depends_on:
      - postgres
      
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: namhatta
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
```

---

## Quick Start Scripts

### Package.json Scripts Addition:
```json
{
  "scripts": {
    "dev": "NODE_ENV=development tsx server/index.ts",
    "dev:fullstack": "concurrently \"npm run dev:frontend\" \"npm run dev:backend\" --names \"REACT,SPRING\" --prefix-colors \"cyan,yellow\"",
    "dev:frontend": "cd client && npm run dev -- --port 3000",
    "dev:backend": "./run-spring-boot.sh",
    "build:frontend": "cd client && npm run build",
    "build:backend": "mvn clean package -DskipTests",
    "build:all": "npm run build:frontend && npm run build:backend"
  }
}
```

### Shell Scripts:

#### start-dev.sh:
```bash
#!/bin/bash
echo "🚀 Starting Namhatta Management System Development Environment"
echo "Frontend: http://localhost:3000"
echo "Backend: http://localhost:8080"
echo "Press Ctrl+C to stop all services"

concurrently \
  --names "REACT,SPRING" \
  --prefix-colors "cyan,yellow" \
  "cd client && npm run dev -- --port 3000" \
  "./run-spring-boot.sh"
```

#### start-frontend.sh:
```bash
#!/bin/bash
echo "🎨 Starting React Frontend Development Server"
cd client && npm run dev -- --port 3000
```

#### start-backend.sh (enhanced):
```bash
#!/bin/bash
echo "☕ Starting Spring Boot Backend Server"
echo "Building application..."
mvn clean compile -q
echo "Starting server on port 8080..."
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## Environment Configuration

### Frontend (.env):
```env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_APP_TITLE=Namhatta Management System
VITE_ENVIRONMENT=development
```

### Backend (application-dev.yml):
```yaml
server:
  port: 8080
  
spring:
  profiles:
    active: dev
  datasource:
    url: ${DATABASE_URL:jdbc:postgresql://localhost:5432/namhatta}
    username: ${DB_USERNAME:admin}
    password: ${DB_PASSWORD:password}
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    
logging:
  level:
    com.namhatta: DEBUG
    org.springframework.web: DEBUG
```

---

## Recommended Development Flow

1. **Start Backend First:**
   ```bash
   ./run-spring-boot.sh
   ```
   Wait for "Started Application" message

2. **Start Frontend:**
   ```bash
   cd client && npm run dev
   ```

3. **Verify Connection:**
   - Frontend: http://localhost:3000
   - Backend Health: http://localhost:8080/actuator/health
   - API Test: http://localhost:8080/api/auth/login

---

## Which Option Should You Choose?

**Choose Option 1 (Concurrent)** if:
- You want simplicity and automation
- Working on full-stack features
- Don't need fine-grained control

**Choose Option 2 (Separate Terminals)** if:
- You need to debug specific services
- Working on frontend or backend only
- Want maximum control and visibility

**Choose Option 3 (Replit Workflows)** if:
- You prefer Replit's native tools
- Working in teams
- Want integrated monitoring

**Choose Option 4 (Docker)** if:
- You need production-like environment
- Working with multiple developers
- Want consistent environment across machines