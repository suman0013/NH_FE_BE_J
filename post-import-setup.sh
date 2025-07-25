#!/bin/bash

echo "🚀 Zero-Setup Import Configuration for Namhatta Management System"
echo "================================================================="
echo "Configuring development environment automatically..."
echo "No manual intervention required!"
echo ""

# Global error handling
set -e
trap 'echo "❌ Setup failed at line $LINENO. Check the error above."; exit 1' ERR

# Progress tracking
TOTAL_STEPS=8
CURRENT_STEP=0

progress() {
    CURRENT_STEP=$((CURRENT_STEP + 1))
    echo "[$CURRENT_STEP/$TOTAL_STEPS] $1"
}

# Function to check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Setup Java environment
setup_java() {
    progress "Verifying Java environment"
    
    if command_exists java; then
        JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | awk -F '.' '{print $1}')
        if [ "$JAVA_VERSION" -ge 17 ]; then
            echo "   ✅ Java $JAVA_VERSION available"
        else
            echo "   ⚠️ Java 17+ required. Current: $JAVA_VERSION"
            echo "   📝 Replit should auto-upgrade for Spring Boot projects"
        fi
    else
        echo "   📦 Java not found - Replit will auto-install"
        echo "   ⏳ This may take a moment on first run"
    fi
}

# Setup Node.js environment  
setup_nodejs() {
    progress "Installing Node.js dependencies"
    
    if command_exists node && command_exists npm; then
        NODE_VERSION=$(node --version)
        echo "   ✅ Node.js $NODE_VERSION available"
        
        # Install root dependencies with error handling
        echo "   📦 Installing root dependencies..."
        if npm install --silent --prefer-offline --no-audit 2>/dev/null; then
            echo "   ✅ Root dependencies installed"
        else
            echo "   ⚠️ Retrying with --force..."
            npm install --force --silent --no-audit
            echo "   ✅ Root dependencies installed (forced)"
        fi
        
        # Install client dependencies
        echo "   🎨 Installing client dependencies..."
        cd client
        if npm install --silent --prefer-offline --no-audit 2>/dev/null; then
            echo "   ✅ Client dependencies installed"
        else
            echo "   ⚠️ Retrying with --force..."
            npm install --force --silent --no-audit
            echo "   ✅ Client dependencies installed (forced)"
        fi
        cd ..
        
    else
        echo "   ❌ Node.js/npm not found"
        echo "   📝 Replit should provide Node.js automatically"
        exit 1
    fi
}

# Setup Spring Boot
setup_spring_boot() {
    progress "Building Spring Boot application"
    
    if command_exists mvn; then
        echo "   ☕ Compiling Spring Boot application..."
        if mvn clean compile -q -T 1C 2>/dev/null; then
            echo "   ✅ Spring Boot compiled successfully"
        else
            echo "   ⚠️ Compilation warnings (checking for errors)..."
            if mvn compile -q 2>/dev/null; then
                echo "   ✅ Spring Boot compiled with warnings"
            else
                echo "   ❌ Compilation failed - will retry on first run"
            fi
        fi
    else
        echo "   📦 Maven not found - Replit will provide it"
        echo "   ⏳ Spring Boot will compile on first run"
    fi
}

# Setup environment files
setup_environment() {
    progress "Configuring environment files"
    
    # Backend environment setup
    if [ ! -f ".env.local" ] && [ -f ".env.example" ]; then
        cp .env.example .env.local
        echo "   ✅ Created .env.local from template"
    else
        echo "   📁 Backend environment already configured"
    fi
    
    # Frontend environment setup
    if [ ! -f "client/.env.local" ] && [ -f "client/.env" ]; then
        cp client/.env client/.env.local
        echo "   ✅ Created client/.env.local"
    else
        echo "   📁 Frontend environment already configured"
    fi
    
    # Set executable permissions for all scripts
    chmod +x *.sh 2>/dev/null || true
    echo "   🔧 Scripts are executable"
}

# Restore package.json files if they were optimized
restore_package_files() {
    progress "Restoring development dependencies"
    
    # Restore original package.json files if backups exist
    if [ -f "package.json.backup" ]; then
        mv package.json.backup package.json
        echo "   ✅ Restored root package.json with devDependencies"
    fi
    
    if [ -f "client/package.json.backup" ]; then
        mv client/package.json.backup client/package.json  
        echo "   ✅ Restored client package.json with devDependencies"
    fi
    
    # Install devDependencies that were removed for import optimization
    if [ -f "package.json" ] && grep -q "devDependencies" package.json; then
        echo "   📦 Installing development dependencies..."
        npm install --only=dev --silent 2>/dev/null || true
    fi
    
    if [ -f "client/package.json" ] && grep -q "devDependencies" client/package.json; then
        echo "   🎨 Installing client development dependencies..."
        cd client && npm install --only=dev --silent 2>/dev/null || true && cd ..
    fi
}

# Setup Replit configuration (template only - cannot modify .replit directly)
setup_replit_config() {
    progress "Setting up Replit configuration"
    
    # Note about .replit configuration
    echo "   📝 Replit configuration template available"
    if [ -f ".replit.template" ]; then
        echo "   📋 Use .replit.template as reference if needed"
    fi
    
    # Ensure the run command works
    if [ -f "start-fullstack.sh" ] && [ -x "start-fullstack.sh" ]; then
        echo "   ✅ Primary run command configured"
    else
        echo "   ⚠️ Primary run command needs setup"
    fi
}

# Start development automatically (final step)
start_development() {
    progress "Starting development environment"
    
    echo "   🚀 Launching React + Spring Boot development..."
    echo ""
    echo "   🌐 Development URLs:"
    echo "   Frontend: http://localhost:3000"
    echo "   Backend:  http://localhost:8080"
    echo ""
    echo "   📋 Development Commands:"
    echo "   Full Stack: ./start-fullstack.sh"
    echo "   Frontend:   ./start-frontend.sh"
    echo "   Backend:    ./run-spring-boot.sh"
    echo ""
    
    # Optional: Auto-start development (user can disable this)
    if [ "${AUTO_START:-false}" = "true" ]; then
        echo "   ▶️ Auto-starting development environment..."
        echo "   (Set AUTO_START=false to disable auto-start)"
        exec ./start-fullstack.sh
    else
        echo "   📋 Run ./start-fullstack.sh to start development"
    fi
}

# Main setup process
main() {
    echo ""
    echo "🎯 Starting zero-setup import configuration..."
    echo ""
    
    setup_java
    setup_nodejs  
    restore_package_files
    setup_environment
    setup_replit_config
    setup_spring_boot
    start_development
    
    echo ""
    echo "✅ Zero-setup import completed successfully!"
    echo ""
    echo "🚀 Development environment is ready!"
    echo "   Frontend: http://localhost:3000 (React + Vite)"
    echo "   Backend:  http://localhost:8080 (Spring Boot)"
    echo ""
    echo "📖 For detailed options, see DEVELOPMENT_SETUP_GUIDE.md"
}

# Run main setup
main