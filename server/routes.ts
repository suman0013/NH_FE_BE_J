import express from 'express';
const router = express.Router();

// Health check
router.get('/health', (req, res) => {
  res.json({ status: 'ok', timestamp: new Date().toISOString() });
});

// Countries
router.get('/countries', (req, res) => {
  res.json([{ id: 1, name: 'India' }]);
});

// States
router.get('/states', (req, res) => {
  res.json([{ id: 1, name: 'West Bengal', countryId: 1 }]);
});

// Districts  
router.get('/districts', (req, res) => {
  res.json([{ id: 1, name: 'Purulia', stateId: 1 }]);
});

// Devotees
router.get('/devotees', (req, res) => {
  res.json({ data: [], total: 0, page: 1, limit: 10 });
});

router.post('/devotees', (req, res) => {
  res.json({ message: 'Devotee created', id: 1 });
});

// Namhattas
router.get('/namhattas', (req, res) => {
  res.json({ data: [], total: 0, page: 1, limit: 10 });
});

router.post('/namhattas', (req, res) => {
  res.json({ message: 'Namhatta created', id: 1 });
});

// Dashboard
router.get('/dashboard', (req, res) => {
  res.json({
    totalDevotees: 0,
    totalNamhattas: 0,
    recentActivity: []
  });
});

export default router;