const express = require('express');
const router = express.Router();

const Product = require('../models/Product');

// Get all products
router.get('/', async (req, res) => {
   try {
      const products = await Product.find({});
      res.status(200).send(products);
   } catch (err) {
      res.status(403).send(err);
   }
});

// Get particular product
router.get('/:productId', async (req, res) => {
   try {
      const id = req.params.productId;
      const product = await Product.findById(id);
      res.status(200).send(product);
   } catch (err) {
      res.status(403).send(err);
   }
});

// Post product
router.post('/', async (req, res) => {
   try {
      const { name, skill, hours } = req.body;
      const newProduct = new Product({
         name,
         skill,
         hours,
      });
      await newProduct.save();
      res.status(200).json(newProduct);
   } catch (err) {
      res.status(403).send(err);
   }
});

module.exports = router;
