#!/bin/bash

# Stop any existing servers
pkill -f tsx 2>/dev/null || true
pkill -f java 2>/dev/null || true
pkill -f node 2>/dev/null || true

# Start the full stack development environment
echo "🚀 Starting React + Spring Boot Development Environment"
./start-fullstack.sh