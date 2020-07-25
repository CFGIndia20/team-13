const express = require('express');
const router = express.Router();

const District = require('../models/District');

// Get all districts
router.get('/', async (req, res) => {
   try {
      const districts = await District.find({});
      res.status(200).send(districts);
   } catch (err) {
      res.status(403).send(err);
   }
});

// Get particular district
router.get('/:districtId', async (req, res) => {
   try {
      const id = req.params.districtId;
      const district = await District.findById(id);
      res.status(200).send(district);
   } catch (err) {
      res.status(403).send(err);
   }
});

// Post district
router.post('/', async (req, res) => {
   try {
      const name = req.body.name;
      const newDistrict = new District({
         name,
      });
      await newDistrict.save();
      res.status(200).json(newDistrict);
   } catch (err) {
      res.status(403).send(err);
   }
});

module.exports = router;
