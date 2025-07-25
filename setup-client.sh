#!/bin/bash

echo "🎨 Setting up React Frontend for Namhatta Management System"
echo "=========================================================="

# Check if we're in the right directory
if [ ! -d "client" ]; then
    echo "❌ No client directory found. Are you in the root directory?"
    exit 1
fi

cd client

# Check if package.json exists
if [ ! -f "package.json" ]; then
    echo "📦 Creating React frontend with Vite..."
    
    # Create package.json for React + TypeScript + Vite
    cat > package.json << 'EOF'
{
  "name": "namhatta-client",
  "private": true,
  "version": "0.0.0",
  "type": "module",
  "scripts": {
    "dev": "vite",
    "build": "tsc && vite build",
    "lint": "eslint . --ext ts,tsx --report-unused-disable-directives --max-warnings 0",
    "preview": "vite preview"
  },
  "dependencies": {
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "@tanstack/react-query": "^5.0.0",
    "wouter": "^3.0.0",
    "axios": "^1.0.0"
  },
  "devDependencies": {
    "@types/react": "^18.2.0",
    "@types/react-dom": "^18.2.0",
    "@typescript-eslint/eslint-plugin": "^6.0.0",
    "@typescript-eslint/parser": "^6.0.0",
    "@vitejs/plugin-react": "^4.0.0",
    "eslint": "^8.45.0",
    "eslint-plugin-react-hooks": "^4.6.0",
    "eslint-plugin-react-refresh": "^0.4.3",
    "typescript": "^5.0.2",
    "vite": "^5.0.0"
  }
}
EOF

    echo "✅ Created package.json"
fi

# Install dependencies
echo "📦 Installing dependencies..."
npm install

# Create vite.config.ts if it doesn't exist
if [ ! -f "vite.config.ts" ]; then
    cat > vite.config.ts << 'EOF'
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000,
    host: '0.0.0.0',
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      }
    }
  },
  build: {
    outDir: 'dist'
  }
})
EOF
    echo "✅ Created vite.config.ts with proxy to Spring Boot backend"
fi

# Create environment file
if [ ! -f ".env" ]; then
    cat > .env << 'EOF'
VITE_API_BASE_URL=http://localhost:8080/api
VITE_APP_TITLE=Namhatta Management System
VITE_ENVIRONMENT=development
EOF
    echo "✅ Created .env file"
fi

echo ""
echo "✅ Frontend setup complete!"
echo "🚀 To start development server: npm run dev"
echo "🌐 Frontend will run on: http://localhost:3000"
echo "🔗 API proxy configured for: http://localhost:8080/api"