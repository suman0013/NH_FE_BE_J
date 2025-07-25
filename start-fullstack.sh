#!/bin/bash

echo "🚀 Starting Namhatta Management System Development Environment"
echo "=============================================="
echo "Frontend: http://localhost:3000 (React + Vite)"  
echo "Backend:  http://localhost:8080 (Spring Boot)"
echo "=============================================="
echo "Press Ctrl+C to stop all services"
echo ""

# Check if concurrently is installed
if ! command -v concurrently &> /dev/null; then
    echo "Installing concurrently..."
    npm install -g concurrently
fi

# Start both services
concurrently \
  --names "REACT,SPRING" \
  --prefix-colors "cyan,yellow" \
  --kill-others \
  "cd client && npm run dev -- --port 3000 --host 0.0.0.0" \
  "./run-spring-boot.sh"