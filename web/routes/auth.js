const express = require('express');
const router = express.Router();

// Login Route
router.get('/login', (req, res) => {
   res.send('login');
});

module.exports = router;
