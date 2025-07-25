const http = require('http');
const url = require('url');

const PORT = process.env.PORT || 5000;

const server = http.createServer((req, res) => {
  const parsedUrl = url.parse(req.url, true);
  
  // Set CORS headers
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept, Authorization');
  
  if (req.method === 'OPTIONS') {
    res.writeHead(200);
    res.end();
    return;
  }
  
  res.setHeader('Content-Type', 'application/json');
  
  if (parsedUrl.pathname === '/health') {
    res.writeHead(200);
    res.end(JSON.stringify({ status: 'ok', timestamp: new Date().toISOString() }));
  } else if (parsedUrl.pathname === '/api/auth/login') {
    res.writeHead(200);
    res.end(JSON.stringify({ token: 'demo-token', user: { id: 1, username: 'admin', role: 'ADMIN' } }));
  } else if (parsedUrl.pathname === '/api/dashboard') {
    res.writeHead(200);
    res.end(JSON.stringify({ totalDevotees: 0, totalNamhattas: 0, recentActivity: [] }));
  } else {
    res.writeHead(404);
    res.end(JSON.stringify({ error: 'Not found' }));
  }
});

server.listen(PORT, '0.0.0.0', () => {
  console.log(`🚀 Test server running on http://0.0.0.0:${PORT}`);
});