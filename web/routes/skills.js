const express = require('express');
const router = express.Router();

const Skill = require('../models/Skill');

// Get all skills
router.get('/', async (req, res) => {
   try {
      const skills = await Skill.find({});
      res.status(200).send(skills);
   } catch (err) {
      res.status(403).send(err);
   }
});

// Get particular skill
router.get('/:skillId', async (req, res) => {
   try {
      const id = req.params.skillId;
      const skill = await Skill.findById(id);
      res.status(200).send(skill);
   } catch (err) {
      res.status(403).send(err);
   }
});

// Post skill
router.post('/', async (req, res) => {
   try {
      const name = req.body.name;
      console.log(name);
      const newSkill = new Skill({
         name,
      });
      await newSkill.save();
      res.status(200).json(newSkill);
   } catch (err) {
      res.status(403).send(err);
   }
});

module.exports = router;
