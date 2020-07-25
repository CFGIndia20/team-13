const express = require('express');
const router = express.Router();

// Login Route
router.get('/login', (req, res) => {
   res.render('auth/login', { pageTitle: 'Login', path: '/login' });
});

module.exports = router;
