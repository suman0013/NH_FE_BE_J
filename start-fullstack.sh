#!/bin/bash

echo "🚀 Starting Namhatta Management System Development Environment"
echo "=============================================="
echo "Frontend: http://localhost:3000 (React + Vite)"  
echo "Backend:  http://localhost:8080 (Spring Boot)"
echo "=============================================="
echo "Press Ctrl+C to stop all services"
echo ""

# Setup function
setup_environment() {
    echo "🔧 Setting up development environment..."
    
    # Install root dependencies if needed
    if [ ! -d "node_modules" ] || [ ! -d "node_modules/concurrently" ]; then
        echo "📦 Installing root dependencies..."
        npm install
    fi
    
    # Install client dependencies if needed
    if [ ! -d "client/node_modules" ]; then
        echo "📦 Installing client dependencies..."
        cd client && npm install && cd ..
    fi
    
    # Build Spring Boot if needed
    if [ ! -f "target/namhatta-management-system-1.0.0.jar" ]; then
        echo "☕ Building Spring Boot application..."
        mvn clean package -DskipTests -q
    fi
    
    echo "✅ Environment setup complete!"
}

# Run setup
setup_environment

# Start both services
echo "🚀 Starting services..."
npx concurrently \
  --names "REACT,SPRING" \
  --prefix-colors "cyan,yellow" \
  --kill-others \
  --restart-tries 3 \
  "cd client && npm run dev" \
  "./run-spring-boot.sh"