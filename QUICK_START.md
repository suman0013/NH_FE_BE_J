# Namhatta Management System - Quick Start

## For Current Environment

### Start Full Development Environment (Recommended)
```bash
./start-fullstack.sh
```
- Frontend: http://localhost:3000 (React + Vite)
- Backend: http://localhost:8080 (Spring Boot)

### Start Individual Services
```bash
# Frontend only
./start-frontend.sh

# Backend only  
./run-spring-boot.sh
```

## For New Replit Import

### 1. Prepare Current Project for Import
```bash
./prepare-for-import.sh
```

### 2. After Import to New Account
```bash
./post-import-setup.sh
```

### 3. Start Development
```bash
./start-fullstack.sh
```

## Development URLs
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/api
- **Health Check**: http://localhost:8080/actuator/health

## Environment Files
- `.env.example` → Copy to `.env.local` for backend
- `client/.env` → Frontend environment variables

## Troubleshooting
- **Dependencies missing**: Run `npm install` and `cd client && npm install`
- **Java not found**: Replit should auto-install for Spring Boot projects
- **Build fails**: Check Java 17+ is available
- **Ports busy**: Scripts will kill existing processes

See DEVELOPMENT_SETUP_GUIDE.md for detailed options.