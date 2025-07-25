#!/bin/bash

echo "🧹 Preparing Namhatta Management System for Replit Import"
echo "========================================================="

# Function to show size
show_size() {
    du -sh . 2>/dev/null | awk '{print $1}' || echo "Unknown"
}

echo "📏 Current project size: $(show_size)"

# Create clean version for import
create_clean_version() {
    echo "🧹 Creating clean version for import..."
    
    # Remove build artifacts and dependencies
    echo "🗑️ Removing build artifacts..."
    rm -rf node_modules
    rm -rf client/node_modules
    rm -rf target
    rm -rf client/dist
    rm -rf dist
    rm -rf .cache
    
    # Remove IDE and system files
    echo "🗑️ Removing IDE and system files..."
    rm -rf .vscode
    rm -rf .idea
    find . -name ".DS_Store" -delete 2>/dev/null || true
    find . -name "*.log" -delete 2>/dev/null || true
    find . -name "*.tmp" -delete 2>/dev/null || true
    
    # Archive large assets (optional)
    if [ -d "attached_assets" ] && [ "$(ls -A attached_assets)" ]; then
        ASSET_SIZE=$(du -sh attached_assets 2>/dev/null | awk '{print $1}')
        echo "📦 Found attached_assets ($ASSET_SIZE). Creating archive..."
        tar -czf attached_assets_backup.tar.gz attached_assets/ 2>/dev/null
        
        # Ask user if they want to remove originals
        read -p "Remove original attached_assets folder to reduce import size? (y/n): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            rm -rf attached_assets
            echo "✅ Archived assets to attached_assets_backup.tar.gz"
        else
            echo "📁 Keeping original attached_assets folder"
        fi
    fi
    
    echo "✅ Cleanup completed"
}

# Create import instructions
create_import_instructions() {
    cat > IMPORT_INSTRUCTIONS.md << 'EOF'
# Quick Import Instructions

## After importing to new Replit account:

### 1. Automatic Setup (Recommended)
```bash
./post-import-setup.sh
```

### 2. Manual Setup (If needed)
```bash
# Install dependencies
npm install
cd client && npm install && cd ..

# Build Spring Boot
mvn clean compile

# Start development
./start-fullstack.sh
```

### 3. Access Points
- Frontend: http://localhost:3000
- Backend: http://localhost:8080
- Full Stack: ./start-fullstack.sh

### 4. If you archived assets:
```bash
tar -xzf attached_assets_backup.tar.gz
```

### 5. Environment Setup
Copy .env.example to .env.local and configure:
- DATABASE_URL
- JWT_SECRET
- Other environment variables

## Troubleshooting
- If Java missing: Replit should auto-install for Spring Boot projects
- If dependencies fail: Run npm install --force
- If build fails: Check Java version (needs 17+)
EOF
    
    echo "✅ Created IMPORT_INSTRUCTIONS.md"
}

# Update .gitignore for cleaner imports
update_gitignore() {
    cat > .gitignore << 'EOF'
# Dependencies
node_modules/
client/node_modules/
/.pnp
.pnp.js

# Build outputs
/dist/
/client/dist/
/target/
*.jar
!.mvn/wrapper/maven-wrapper.jar
!**/src/main/**/target/
!**/src/test/**/target/

# IDE
.vscode/
.idea/
*.swp
*.swo
*~

# OS
.DS_Store
.DS_Store?
._*
.Spotlight-V100
.Trashes
ehthumbs.db
Thumbs.db

# Logs
logs
*.log
npm-debug.log*
yarn-debug.log*
yarn-error.log*
pnpm-debug.log*
lerna-debug.log*

# Environment variables
.env.local
.env.development.local
.env.test.local
.env.production.local

# Cache
.cache/
.parcel-cache/
.npm
.eslintcache

# Runtime data
pids
*.pid
*.seed
*.pid.lock

# Coverage directory used by tools like istanbul
coverage/
*.lcov

# Dependency directories
node_modules/
jspm_packages/

# Optional npm cache directory
.npm

# Optional REPL history
.node_repl_history

# Output of 'npm pack'
*.tgz

# Yarn Integrity file
.yarn-integrity

# dotenv environment variable files
.env
.env.test

# parcel-bundler cache (https://parceljs.org/)
.cache
.parcel-cache

# Next.js build output
.next

# Nuxt.js build / generate output
.nuxt
dist

# Gatsby files
.cache/
public

# Storybook build outputs
.out
.storybook-out

# Temporary folders
tmp/
temp/

# Editor directories and files
.vscode/*
!.vscode/extensions.json
.idea
.DS_Store
*.suo
*.ntvs*
*.njsproj
*.sln
*.sw?
EOF
    
    echo "✅ Updated .gitignore"
}

# Make scripts executable
make_executable() {
    echo "🔧 Setting executable permissions..."
    chmod +x *.sh 2>/dev/null || true
    echo "✅ Scripts are executable"
}

# Main function
main() {
    echo "🚀 Starting import preparation..."
    
    create_clean_version
    create_import_instructions  
    update_gitignore
    make_executable
    
    echo ""
    echo "📏 Optimized project size: $(show_size)"
    echo ""
    echo "✅ Project ready for import!"
    echo ""
    echo "📋 Import Steps:"
    echo "1. Zip/tar this folder"
    echo "2. Import to new Replit account"
    echo "3. Run ./post-import-setup.sh"
    echo "4. Start development with ./start-fullstack.sh"
    echo ""
    echo "📖 See IMPORT_INSTRUCTIONS.md for detailed steps"
}

# Run main function
main