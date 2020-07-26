const express = require('express');
const router = express.Router();
const bcyrpt = require('bcryptjs');
const passport = require('passport');

const User = require('../models/User');

router.get('/', (req, res) => {
   res.render('login', { pageTitle: 'Login', path: '/login' });
});

// Register API
router.post('/register', async (req, res) => {
   const { name, phone, password, isAdmin } = req.body;

   if (!name || !phone || !password)
      res.status(401).send({ msg: 'Please fill in all fields' });

   try {
      const user = await User.findOne({ phone: phone });
      if (user) {
         res.status(403).send('Already registered');
      } else {
         const newUser = new User({
            name,
            phone,
            password,
            isAdmin,
         });

         // Hash Password
         bcyrpt.genSalt(10, (err, salt) =>
            bcyrpt.hash(newUser.password, salt, async (err, hash) => {
               if (err) throw err;

               newUser.password = hash;

               await newUser.save();
               res.status(200).json(newUser);
            })
         );
      }
   } catch (err) {
      res.status(403).send(err);
   }
});

// Login API
router.post('/login', (req, res, next) => {
   passport.authenticate('local', {
      successRedirect: '/order',
      failureRedirect: '/login',
      failureFlash: true,
   })(req, res, next);
});

// Logout API
router.get('/logout', (req, res) => {
   req.logout();
   req.flash('success_msg', 'You are logged out');
   res.redirect('/users/login');
});

module.exports = router;
