const express = require('express');
const router = express.Router();
const bcrypt = require('bcryptjs');

const Woman = require('../models/Woman');

// Login woman
router.post('/login', async (req, res) => {
   try {
      const { phone, password } = req.body;
      console.log(phone, password);
      Woman.findOne({ phone: phone })
         .then((user) => {
            if (!user) {
               res.status(403).json({ msg: 'No user found' });
            }

            // Match Password
            bcrypt.compare(password, user.password, (err, isMatch) => {
               if (err) throw err;
               if (isMatch) {
                  res.status(200).json(user);
               } else {
                  res.status(403).json({
                     msg: 'Password incorrect',
                  });
               }
            });
         })
         .catch((err) => console.error(err));
   } catch (err) {
      res.status(403).send(err);
   }
});

// Get all woman
router.get('/', async (req, res) => {
   try {
      const women = await Woman.find({});
      res.status(200).send(women);
   } catch (err) {
      res.status(403).send(err);
   }
});

// Register woman
router.post('/register', async (req, res) => {
   try {
      const { name, phone, password, district } = req.body;
      const newWoman = new Woman({
         name,
         phone,
         password,
         district,
      });

      // Hash Password
      bcrypt.genSalt(10, (err, salt) =>
         bcrypt.hash(newWoman.password, salt, async (err, hash) => {
            if (err) throw err;

            newWoman.password = hash;

            await newWoman.save();
            res.status(200).send(newWoman);
         })
      );
   } catch (err) {
      res.status(403).send(err);
   }
});

module.exports = router;
