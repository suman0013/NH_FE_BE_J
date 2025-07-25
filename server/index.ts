import express from 'express';
import { fileURLToPath } from 'url';
import { dirname, join } from 'path';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

const app = express();
const PORT = parseInt(process.env.PORT || '5000');

// Basic middleware
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// CORS headers
app.use((req, res, next) => {
  res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
  res.header('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept, Authorization');
  if (req.method === 'OPTIONS') {
    res.sendStatus(200);
  } else {
    next();
  }
});

// Health check
app.get('/health', (req, res) => {
  res.json({ status: 'ok', timestamp: new Date().toISOString() });
});

// Auth routes
app.post('/api/auth/login', (req, res) => {
  const { username, password } = req.body;
  
  if (username === 'admin' && password === 'Admin@123456') {
    res.json({
      token: 'demo-token',
      user: {
        id: 1,
        username: 'admin',
        role: 'ADMIN'
      }
    });
  } else {
    res.status(401).json({ error: 'Invalid credentials' });
  }
});

app.post('/api/auth/logout', (req, res) => {
  res.json({ message: 'Logged out successfully' });
});

app.get('/api/auth/verify', (req, res) => {
  res.json({
    user: {
      id: 1,
      username: 'admin',
      role: 'ADMIN'
    }
  });
});

// Basic API routes with demo data
app.get('/api/dashboard', (req, res) => {
  res.json({
    totalDevotees: 15,
    totalNamhattas: 8,
    recentActivity: [
      { id: 1, type: 'devotee_added', message: 'New devotee Ram Das registered', timestamp: new Date().toISOString() },
      { id: 2, type: 'namhatta_updated', message: 'Purulia Namhatta status updated', timestamp: new Date().toISOString() }
    ]
  });
});

app.get('/api/devotees', (req, res) => {
  res.json({ 
    data: [
      { id: 1, name: 'Ram Das', email: 'ram@example.com', phone: '1234567890', status: 'Initiated' },
      { id: 2, name: 'Shyam Das', email: 'shyam@example.com', phone: '0987654321', status: 'Bhakta' }
    ], 
    total: 2, 
    page: 1, 
    limit: 10 
  });
});

app.get('/api/namhattas', (req, res) => {
  res.json({ 
    data: [
      { id: 1, name: 'Purulia Namhatta', secretary: 'Krishna Das', status: 'Approved', establishedDate: '2023-01-15' },
      { id: 2, name: 'Raghunathpur Namhatta', secretary: 'Govinda Das', status: 'Pending', establishedDate: '2023-06-20' }
    ], 
    total: 2, 
    page: 1, 
    limit: 10 
  });
});

// Geographic data
app.get('/api/countries', (req, res) => {
  res.json([{ id: 1, name: 'India' }]);
});

app.get('/api/states', (req, res) => {
  res.json([{ id: 1, name: 'West Bengal', countryId: 1 }]);
});

app.get('/api/districts', (req, res) => {
  res.json([{ id: 1, name: 'Purulia', stateId: 1 }]);
});

// Build client if needed
const buildExists = async () => {
  try {
    const fs = await import('fs');
    const clientDistPath = join(__dirname, '..', 'dist', 'public');
    return fs.existsSync(clientDistPath);
  } catch {
    return false;
  }
};

// Serve static files from client (if built)
const clientDistPath = join(__dirname, '..', 'dist', 'public');

// Static files middleware with fallback
app.use(express.static(clientDistPath, {
  fallthrough: true,
  index: false
}));

// SPA fallback
app.get('*', (req, res) => {
  if (req.path.startsWith('/api/')) {
    res.status(404).json({ error: 'API endpoint not found' });
    return;
  }
  
  // Serve the built React app
  const indexPath = join(__dirname, '..', 'dist', 'public', 'index.html');
  res.sendFile(indexPath, (err) => {
    if (err) {
      console.error('Error serving index.html:', err);
      // Fallback to simple HTML if built files don't exist
      res.send(`
        <!DOCTYPE html>
        <html>
        <head>
          <title>Namhatta Management System</title>
          <meta charset="utf-8">
          <meta name="viewport" content="width=device-width, initial-scale=1">
        </head>
        <body>
          <div id="root">
            <h1>🕉️ Namhatta Management System</h1>
            <p>✅ Backend server is running successfully!</p>
            <p>❌ Frontend build not found. Run: <code>vite build</code></p>
            <p>📍 API Health Check: <a href="/health">/health</a></p>
            <p>🔑 Demo Login: username: <code>admin</code>, password: <code>Admin@123456</code></p>
            <p>📊 Dashboard: <a href="/api/dashboard">/api/dashboard</a></p>
            <p>👥 Devotees: <a href="/api/devotees">/api/devotees</a></p>
            <p>🏛️ Namhattas: <a href="/api/namhattas">/api/namhattas</a></p>
          </div>
        </body>
        </html>
      `);
    }
  });
});

app.listen(PORT, '0.0.0.0', () => {
  console.log(`🚀 Server running on http://0.0.0.0:${PORT}`);
  console.log(`📖 Environment: ${process.env.NODE_ENV || 'development'}`);
  console.log(`✅ Migration from Spring Boot to Node.js/Express completed!`);
});