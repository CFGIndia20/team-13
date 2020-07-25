const express = require('express');
const router = express.Router();
const bcrypt = require('bcryptjs');

const Woman = require('../models/Woman');
const District = require('../models/District');

// Login woman
router.post('/login', async (req, res) => {
   try {
      const { phone, password } = req.body;

      let woman = await Woman.findOne({ phone: phone })
         .populate('district')
         .exec();
      if (!woman) {
         res.status(403).json({ msg: 'No user found' });
      }

      bcrypt.compare(password, woman.password, async (err, isMatch) => {
         if (err) throw err;
         if (isMatch) {
            res.status(200).json(woman);
         } else {
            res.status(403).json({
               msg: 'Password incorrect',
            });
         }
      });
   } catch (err) {
      res.status(403).send(err);
   }
});

// Get all women
router.get('/', async (req, res) => {
   try {
      const women = await Woman.find({});
      res.status(200).send(women);
   } catch (err) {
      res.status(403).send(err);
   }
});

// Get all women by district id
router.get('/:districtId', async (req, res) => {
   try {
      const id = req.params.districtId;
      const women = await Woman.find({ district: id });
      res.status(200).send(women);
   } catch (err) {
      res.status(403).send(err);
   }
});

// Get all women by district id
router.get('/sort/hours', async (req, res) => {
   try {
      const women = await Woman.find().sort('-amountOfHours -efficiency');
      res.status(200).send(women);
   } catch (err) {
      res.status(403).send(err);
   }
});

// Register woman
router.post('/register', async (req, res) => {
   try {
      const { name, phone, password, district } = req.body;
      const woman = await Woman.findOne({ phone: phone });
      if (woman) {
         res.status(403).send({ msg: 'Already registered' });
      } else {
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
      }
   } catch (err) {
      res.status(403).send(err);
   }
});

module.exports = router;
