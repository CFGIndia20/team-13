const express = require('express');
const router = express.Router();

const Task = require('../models/Task');
const Order = require('../models/Order');
const Woman = require('../models/Woman');
const Product = require('../models/Product');

// Get all tasks by order id
router.get('/order/:orderId', async (req, res) => {
   try {
      const id = req.params.orderId;
      const tasks = await Task.find({ order: id });
      res.status(200).send(tasks);
   } catch (err) {
      res.status(403).send(err);
   }
});

router.get('/get/tasks', async (req, res) => {
   try {
      const tasks = await Task.find();
      res.status(200).send(tasks);
   } catch (err) {
      res.status(403).send(err);
   }
});

// Get particular task
router.get('/:taskId', async (req, res) => {
   try {
      const id = req.params.taskId;
      const task = await Task.findById(id);
      res.status(200).send(task);
   } catch (err) {
      res.status(403).send(err);
   }
});

// Get all tasks by woman id
router.get('/woman/:womanId', async (req, res) => {
   try {
      const id = req.params.womanId;
      const tasks = await Task.find({ woman: id });
      res.status(200).send(tasks);
   } catch (err) {
      res.status(403).send(err);
   }
});

// Get suggested workload
router.post('/suggested', async (req, res) => {
   try {
      console.log('reqqqqq', req.body);
      const { orderId, womanId } = req.body;
      const order = await Order.findById(orderId);
      const woman = await Woman.findById(womanId);
      const product = await Product.findById(order.product);
      const hoursCanBeDone = Math.floor(woman.amountOfHours / product.hours);
      res.status(200).json({
         quantity:
            order.quantity <= hoursCanBeDone ? order.quantity : hoursCanBeDone,
      });
   } catch (err) {
      res.status(403).send(err);
   }
});

// Assign task
router.post('/assign', async (req, res) => {
   try {
      const { orderId, womanId, quantity } = req.body;
      const order = await Order.findById(orderId);
      const woman = await Woman.findById(womanId);
      const product = await Product.findById(order.product);

      const newTask = new Task({
         order: orderId,
         product: product._id,
         woman: womanId,
         quantity,
      });

      await newTask.save();
      res.status(200).send(task);
   } catch (err) {
      res.status(403).send(err);
   }
});

// Get Progress of each order
router.get('/calculate/progress', async (req, res) => {
   try {
      const orders = await Order.find();
      for (let i = 0; i < orders.length; i++) {
         order = orders[i];
         const tasks = await Task.find({ order: order._id });
         let completed = 0;
         for (let j = 0; j < tasks.length; j++) {
            const task = tasks[i];
            completed += task.currentQuantity;
         }
         order.progress = (completed / order.quantity) * 100;
      }
      res.status(200).send(orders);
   } catch (err) {
      console.log(err);
      res.status(403).send(err);
   }
});

module.exports = router;
