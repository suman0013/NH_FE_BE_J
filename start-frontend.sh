#!/bin/bash

echo "🎨 Starting React Frontend Development Server"
echo "============================================="
echo "URL: http://localhost:3000"
echo "Environment: Development"
echo "============================================="

cd client

# Check if dependencies are installed
if [ ! -d "node_modules" ]; then
    echo "Installing frontend dependencies..."
    npm install
fi

# Start development server
npm run dev -- --port 3000 --host 0.0.0.0