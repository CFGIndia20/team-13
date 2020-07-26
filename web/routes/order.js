const express = require('express');
const router = express.Router();

const Order = require('../models/Order');

// Get all orders
router.get('/', async (req, res) => {
   try {
      const orders = await Order.find({});
      //res.status(200).send(orders);
      res.render('orders', { pageTitle: 'Orders Page', path: '/orders' ,orders: orders });
   } catch (err) {
      res.status(403).send(err);
   }
});

// Get particular order
router.get('/:orderId', async (req, res) => {
   try {
      const id = req.params.orderId;
      const order = await Order.findById(id);
      res.status(200).send(order);
   } catch (err) {
      res.status(403).send(err);
   }
});

// Post order
router.post('/', async (req, res) => {
   try {
      const { name, product, quantity } = req.body;
      const newOrder = new Order({
         name,
         product,
         quantity,
      });
      await newOrder.save();
      res.status(200).json(newOrder);
   } catch (err) {
      res.status(403).send(err);
   }
});

module.exports = router;
