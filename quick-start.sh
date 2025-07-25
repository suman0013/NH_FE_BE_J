#!/bin/bash

echo "🚀 Namhatta Management System - Quick Start Guide"
echo "================================================="
echo ""

show_option() {
    echo "Option $1: $2"
    echo "   Command: $3"
    echo "   Description: $4"
    echo ""
}

echo "Choose your development setup:"
echo ""

show_option "1" "Full-Stack (Concurrent)" \
    "./start-fullstack.sh" \
    "Runs both React frontend (port 3000) and Spring Boot backend (port 8080)"

show_option "2" "Frontend Only" \
    "./start-frontend.sh" \
    "Runs only React development server"

show_option "3" "Backend Only" \
    "./run-spring-boot.sh" \
    "Runs only Spring Boot backend server"

show_option "4" "Manual Setup" \
    "See DEVELOPMENT_SETUP_GUIDE.md" \
    "Step-by-step manual configuration"

echo "Recommended for first time: Option 1 (Full-Stack)"
echo ""
echo "Available endpoints after startup:"
echo "  Frontend:     http://localhost:3000"
echo "  Backend API:  http://localhost:8080/api"
echo "  Health Check: http://localhost:8080/api/health"
echo ""
echo "Need help? Check these files:"
echo "  📖 DEVELOPMENT_SETUP_GUIDE.md"
echo "  🚀 REPLIT_IMPORT_OPTIMIZATION_GUIDE.md"