#!/usr/bin/env node

const { spawn } = require('child_process');
const path = require('path');
const fs = require('fs');

console.log('🚀 Starting Namhatta Management System Development Environment...\n');

// Check if client dependencies are installed
const clientNodeModules = path.join(__dirname, 'client', 'node_modules');
if (!fs.existsSync(clientNodeModules)) {
    console.log('📦 Installing frontend dependencies...');
    const installProcess = spawn('npm', ['install'], { 
        cwd: path.join(__dirname, 'client'),
        stdio: 'inherit' 
    });
    
    installProcess.on('close', (code) => {
        if (code === 0) {
            console.log('✅ Frontend dependencies installed successfully!\n');
            startServices();
        } else {
            console.error('❌ Failed to install frontend dependencies');
            process.exit(1);
        }
    });
} else {
    startServices();
}

function startServices() {
    console.log('🎯 Starting React Frontend (port 3000)...');
    const frontend = spawn('npm', ['run', 'dev'], {
        cwd: path.join(__dirname, 'client'),
        stdio: ['inherit', 'pipe', 'pipe']
    });

    console.log('🏗️  Starting Spring Boot Backend (port 8080)...');
    const backend = spawn('mvn', ['spring-boot:run'], {
        cwd: __dirname,
        stdio: ['inherit', 'pipe', 'pipe']
    });

    // Handle frontend output
    frontend.stdout.on('data', (data) => {
        process.stdout.write(`[FRONTEND] ${data}`);
    });
    frontend.stderr.on('data', (data) => {
        process.stderr.write(`[FRONTEND] ${data}`);
    });

    // Handle backend output
    backend.stdout.on('data', (data) => {
        process.stdout.write(`[BACKEND] ${data}`);
    });
    backend.stderr.on('data', (data) => {
        process.stderr.write(`[BACKEND] ${data}`);
    });

    // Handle process exits
    frontend.on('close', (code) => {
        console.log(`\n🛑 Frontend process exited with code ${code}`);
        if (backend.pid) backend.kill();
        process.exit(code);
    });

    backend.on('close', (code) => {
        console.log(`\n🛑 Backend process exited with code ${code}`);
        if (frontend.pid) frontend.kill();
        process.exit(code);
    });

    // Handle Ctrl+C
    process.on('SIGINT', () => {
        console.log('\n🛑 Shutting down development environment...');
        if (frontend.pid) frontend.kill();
        if (backend.pid) backend.kill();
        process.exit(0);
    });

    console.log('\n✅ Development environment started successfully!');
    console.log('📱 Frontend: http://localhost:3000');
    console.log('🔧 Backend API: http://localhost:8080');
    console.log('\n💡 Use Ctrl+C to stop both services\n');
}