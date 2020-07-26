const express = require('express');
const router = express.Router();

router.get('/index', (req, res) => {
    res.render('index', { pageTitle: 'Home Page', path: '/' });
});

// Login Route
router.get('/', (req, res) => {
   //res.render('login', { pageTitle: 'Login', path: '/login' });
   res.redirect('/login');
});


router.get('/quality-check', (req, res) => {
    res.render('qualitycheck', { pageTitle: 'Quality Check', path: '/quality-check' });
});



router.get('/assigning', async (req, res) => {
     res.status(200).render('assigning', { pageTitle: 'Assign', path: '/assigning' });
 });

module.exports = router;
