#!/bin/bash

echo "🚀 Post-Import Setup for Namhatta Management System"
echo "===================================================="
echo "This script runs automatically after importing to a new Replit account"
echo ""

# Function to check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Setup Java environment
setup_java() {
    echo "☕ Checking Java environment..."
    if command_exists java; then
        JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | awk -F '.' '{print $1}')
        if [ "$JAVA_VERSION" -ge 17 ]; then
            echo "✅ Java $JAVA_VERSION is available"
        else
            echo "⚠️ Java 17+ required. Current: $JAVA_VERSION"
        fi
    else
        echo "❌ Java not found. Replit should install it automatically for Spring Boot projects."
    fi
}

# Setup Node.js environment  
setup_nodejs() {
    echo "📦 Checking Node.js environment..."
    if command_exists node && command_exists npm; then
        NODE_VERSION=$(node --version)
        echo "✅ Node.js $NODE_VERSION is available"
        
        # Install root dependencies
        echo "📦 Installing root dependencies..."
        npm install --silent
        
        # Install client dependencies
        echo "🎨 Installing client dependencies..."
        cd client && npm install --silent && cd ..
        
        echo "✅ Node.js dependencies installed"
    else
        echo "❌ Node.js/npm not found"
        exit 1
    fi
}

# Setup Spring Boot
setup_spring_boot() {
    echo "☕ Setting up Spring Boot..."
    if command_exists mvn; then
        echo "📦 Building Spring Boot application..."
        mvn clean compile -q
        echo "✅ Spring Boot compiled successfully"
    else
        echo "❌ Maven not found"
        exit 1
    fi
}

# Setup environment files
setup_environment() {
    echo "⚙️ Setting up environment variables..."
    
    # Copy environment files if they don't exist
    if [ ! -f ".env.local" ] && [ -f ".env.example" ]; then
        cp .env.example .env.local
        echo "✅ Created .env.local from .env.example"
    fi
    
    if [ ! -f "client/.env.local" ] && [ -f "client/.env" ]; then
        cp client/.env client/.env.local
        echo "✅ Created client/.env.local"
    fi
    
    # Set executable permissions
    chmod +x *.sh
    echo "✅ Set executable permissions on scripts"
}

# Create Replit configuration
setup_replit_config() {
    echo "🔧 Setting up Replit configuration..."
    
    # Update .replit file for proper development setup
    cat > .replit << 'EOF'
run = "./start-fullstack.sh"
entrypoint = "src/main/java/com/namhatta/Application.java"

[deployment]
run = "./start-fullstack.sh"
deploymentTarget = "autoscale"

[nix]
channel = "stable-22_11"

[languages.java]
pattern = "**/*.java"

[languages.java.languageServer]
start = "jdt-language-server"

[languages.javascript]
pattern = "**/{*.js,*.jsx,*.ts,*.tsx}"

[languages.javascript.languageServer]
start = "typescript-language-server --stdio"

[[ports]]
localPort = 3000
externalPort = 80

[[ports]]
localPort = 8080
externalPort = 8080
EOF
    
    echo "✅ Updated .replit configuration"
}

# Main setup process
main() {
    echo "🚀 Starting post-import setup..."
    
    setup_java
    setup_nodejs  
    setup_environment
    setup_replit_config
    setup_spring_boot
    
    echo ""
    echo "✅ Post-import setup completed successfully!"
    echo ""
    echo "🚀 Quick Start Commands:"
    echo "   Full Stack: ./start-fullstack.sh"
    echo "   Frontend:   ./start-frontend.sh" 
    echo "   Backend:    ./run-spring-boot.sh"
    echo ""
    echo "🌐 Access URLs:"
    echo "   Frontend: http://localhost:3000"
    echo "   Backend:  http://localhost:8080"
    echo ""
    echo "📖 For detailed setup options, see DEVELOPMENT_SETUP_GUIDE.md"
}

# Run main setup
main