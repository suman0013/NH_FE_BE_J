import express from 'express';
const router = express.Router();

// Login
router.post('/login', (req, res) => {
  const { username, password } = req.body;
  
  // Demo authentication
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

// Logout
router.post('/logout', (req, res) => {
  res.json({ message: 'Logged out successfully' });
});

// Verify token
router.get('/verify', (req, res) => {
  res.json({
    user: {
      id: 1,
      username: 'admin',
      role: 'ADMIN'
    }
  });
});

export default router;