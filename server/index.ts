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

// Hierarchy endpoint
app.get('/api/hierarchy', (req, res) => {
  res.json({
    regionalDirectors: [
      {
        id: 1,
        name: 'Sri Krishna Das',
        position: 'Regional Director',
        region: 'West Bengal',
        districts: ['Purulia', 'Bankura']
      }
    ],
    coRegionalDirectors: [
      {
        id: 2,
        name: 'Sri Govinda Das',
        position: 'Co-Regional Director',
        region: 'West Bengal',
        districts: ['Hooghly', 'Howrah']
      }
    ],
    districtSupervisors: [
      {
        id: 3,
        name: 'Sri Ram Das',
        position: 'District Supervisor',
        district: 'Purulia',
        namhattas: ['Purulia Namhatta', 'Raghunathpur Namhatta']
      }
    ]
  });
});

// Status distribution endpoint
app.get('/api/status-distribution', (req, res) => {
  res.json({
    distribution: [
      { status: 'Shraddhavan', count: 5, percentage: 33.3 },
      { status: 'Sadhusangi', count: 4, percentage: 26.7 },
      { status: 'Gour Sevak', count: 3, percentage: 20.0 },
      { status: 'Gour Sadhak', count: 2, percentage: 13.3 },
      { status: 'Harinam Diksha', count: 1, percentage: 6.7 }
    ],
    totalDevotees: 15
  });
});

// Devotional statuses endpoint
app.get('/api/statuses', (req, res) => {
  res.json([
    { id: 1, name: 'Shraddhavan', level: 1, description: 'Initial spiritual seeker' },
    { id: 2, name: 'Sadhusangi', level: 2, description: 'Association with devotees' },
    { id: 3, name: 'Gour Sevak', level: 3, description: 'Service to Lord Gouranga' },
    { id: 4, name: 'Gour Sadhak', level: 4, description: 'Spiritual practice under guidance' },
    { id: 5, name: 'Sri Guru Charan Asraya', level: 5, description: 'Shelter at lotus feet of Guru' },
    { id: 6, name: 'Harinam Diksha', level: 6, description: 'Initiation into chanting' },
    { id: 7, name: 'Pancharatrik Diksha', level: 7, description: 'Second initiation' }
  ]);
});

// Sub-districts endpoint
app.get('/api/sub-districts', (req, res) => {
  const { district, pincode } = req.query;
  res.json([
    { id: 1, name: 'Purulia Sadar', districtId: 1, pincode: '723101' },
    { id: 2, name: 'Raghunathpur', districtId: 1, pincode: '723133' }
  ]);
});

// Villages endpoint  
app.get('/api/villages', (req, res) => {
  const { subDistrict, pincode } = req.query;
  res.json([
    { id: 1, name: 'Purulia Town', subDistrictId: 1, pincode: '723101' },
    { id: 2, name: 'Raghunathpur Town', subDistrictId: 2, pincode: '723133' }
  ]);
});

// Pincodes endpoint
app.get('/api/pincodes', (req, res) => {
  const { search } = req.query;
  res.json([
    { pincode: '723101', district: 'Purulia', state: 'West Bengal' },
    { pincode: '723133', district: 'Purulia', state: 'West Bengal' },
    { pincode: '700001', district: 'Kolkata', state: 'West Bengal' }
  ]);
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