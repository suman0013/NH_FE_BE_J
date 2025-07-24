// Proxy server to run Spring Boot backend
import { spawn } from 'child_process';
import { join, dirname } from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);
const projectRoot = join(__dirname, '..');

console.log('🚀 Starting Spring Boot Namhatta Management System...');
console.log('📁 Project root:', projectRoot);

// Start Spring Boot application
const springBootProcess = spawn('mvn', ['spring-boot:run'], {
  cwd: projectRoot,
  stdio: 'inherit',
  env: {
    ...process.env,
    DATABASE_URL: process.env.DATABASE_URL,
    JWT_SECRET: process.env.JWT_SECRET,
    SESSION_SECRET: process.env.SESSION_SECRET,
    JAVA_OPTS: '-Xmx1024m -Xms512m'
  }
});

springBootProcess.on('error', (error) => {
  console.error('❌ Failed to start Spring Boot application:', error.message);
  if (error.message.includes('ENOENT')) {
    console.error('💡 Maven may not be installed. Install Maven first:');
    console.error('   - On Ubuntu/Debian: sudo apt-get install maven');
    console.error('   - On macOS: brew install maven');
  }
  process.exit(1);
});

springBootProcess.on('exit', (code) => {
  if (code !== 0) {
    console.error(`❌ Spring Boot application exited with code ${code}`);
    process.exit(code);
  }
});

// Graceful shutdown
process.on('SIGINT', () => {
  console.log('\n🛑 Shutting down Spring Boot application...');
  springBootProcess.kill('SIGTERM');
  setTimeout(() => {
    springBootProcess.kill('SIGKILL');
    process.exit(0);
  }, 5000);
});

console.log('✅ Spring Boot application starting...');
console.log('📍 Application will be available at: http://localhost:5000');
console.log('📖 API Documentation: http://localhost:5000/swagger-ui.html');