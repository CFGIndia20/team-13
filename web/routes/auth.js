const express = require('express');
const router = express.Router();
const bcyrpt = require('bcryptjs');
const passport = require('passport');

const User = require('../models/User');

// Login Route
router.get('/login', (req, res) => {
   res.render('auth/login', { pageTitle: 'Login', path: '/login' });
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
            })
         );
      }
   } catch (err) {
      res.status(403).send(err);
   }
});

// Login API
router.get('/login', (req, res, next) => {
   passport.authenticate('local', {
      successRedirect: '/',
      failureRedirect: '/users/login',
      failureFlash: true,
   })(req, res, next);
});

module.exports = router;
