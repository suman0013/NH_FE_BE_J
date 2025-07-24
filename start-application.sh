#!/bin/bash

# Combined startup script for Namhatta Management System
# Starts Spring Boot backend and React frontend together

echo "🚀 Starting Namhatta Management System (Full Stack)"

# Set proper environment variables
export DATABASE_URL="jdbc:postgresql://ep-calm-silence-a15zko7l-pooler.ap-southeast-1.aws.neon.tech:5432/neondb?user=neondb_owner&password=npg_5MIwCD4YhSdP&sslmode=require"
export JWT_SECRET="42d236149a7fe69b8f2f5ec7093f4805873e6569098cacbdc076eae0f80eef53"
export PORT=5000

# Start Spring Boot backend
echo "📦 Starting Spring Boot backend on port $PORT..."
java -jar target/namhatta-management-system-1.0.0.jar \
    --spring.profiles.active=prod \
    --server.port=$PORT \
    --server.address=0.0.0.0 &

BACKEND_PID=$!
echo "✅ Backend started with PID: $BACKEND_PID"

# Wait for backend to be ready
echo "⏳ Waiting for backend to start..."
for i in {1..30}; do
    if curl -s http://localhost:$PORT/api/health > /dev/null 2>&1; then
        echo "✅ Backend is ready!"
        break
    fi
    if [ $i -eq 30 ]; then
        echo "❌ Backend failed to start"
        kill $BACKEND_PID 2>/dev/null || true
        exit 1
    fi
    sleep 1
done

# Start React frontend (development mode)
echo "🌐 Starting React frontend..."
cd client && npm run build

echo "🎯 Application started successfully!"
echo "   Backend: http://localhost:$PORT"
echo "   Health Check: http://localhost:$PORT/api/health"
echo "   Frontend: Served by Spring Boot at http://localhost:$PORT"

# Keep the process alive
wait $BACKEND_PID