#!/bin/bash

# Spring Boot Namhatta Management System Startup Script
# This script starts the Spring Boot application with proper environment configuration

echo "🚀 Starting Spring Boot Namhatta Management System..."

# Check if Java 17 is available
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | awk -F '.' '{print $1}')
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Java 17 or higher is required. Current version: $JAVA_VERSION"
    exit 1
fi

echo "✅ Java version: $(java -version 2>&1 | head -n 1)"

# Set environment variables if not already set
export DATABASE_URL=${DATABASE_URL:-"postgresql://neondb_owner:npg_5MIwCD4YhSdP@ep-calm-silence-a15zko7l-pooler.ap-southeast-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require"}
export JWT_SECRET=${JWT_SECRET:-"42d236149a7fe69b8f2f5ec7093f4805873e6569098cacbdc076eae0f80eef53"}
export PORT=${PORT:-5000}

echo "🔧 Environment configured:"
echo "   - Database: $(echo $DATABASE_URL | sed 's/:.*@/:***@/')"
echo "   - Port: $PORT"
echo "   - Profile: ${SPRING_PROFILES_ACTIVE:-production}"

# Build the application if needed
if [ ! -f "target/namhatta-management-system-1.0.0.jar" ]; then
    echo "📦 Building application..."
    mvn clean package -DskipTests -q
    if [ $? -ne 0 ]; then
        echo "❌ Build failed"
        exit 1
    fi
    echo "✅ Build completed"
fi

# Start the Spring Boot application
echo "🎯 Starting application on port $PORT..."
echo "   Access URL: http://localhost:$PORT"
echo "   Health Check: http://localhost:$PORT/api/health"
echo "   Test Endpoints: http://localhost:$PORT/api/test/health"
echo ""

# Run with production profile by default
java -jar target/namhatta-management-system-1.0.0.jar \
    --spring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod} \
    --server.port=$PORT