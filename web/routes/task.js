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
router.post('/woman', async (req, res) => {
   try {
      const id = req.body.womanId;
      const tasks = await Task.find({ woman: id });
      res.status(200).send(tasks);
   } catch (err) {
      res.status(403).send(err);
   }
});

// Get suggested workload
router.post('/suggested', async (req, res) => {
   try {
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
      const remainingHours = Math.max(
         woman.amountOfHours - product.hours * quantity,
         0
      );
      await Woman.findByIdAndUpdate(womanId, {
         amountOfHours: remainingHours,
      });
      const remaining = order.remainingQuantity - quantity;
      await Order.findByIdAndUpdate(orderId, { remainingQuantity: remaining });
      res.status(200).send(newTask);
   } catch (err) {
      console.log(err);
      res.status(403).send(err);
   }
});

// Get all tasks for woman id
router.get('/all', async (req, res) => {
   try {
      const womanId = req.body.womanId;
      const tasks = await Task.find({ woman: womanId });
      res.status(200).send(tasks);
   } catch (err) {
      res.status(403).send(err);
   }
});

// Upload Image API
router.post('/upload', async (req, res) => {
   try {
      const { taskId, image, imageType, timestamp } = req.body;
      if (image == null) return res.status(403).json({ msg: 'Image is null' });
      if (imageType == 'jpg' || imageType == 'png') {
         const buffer = Buffer.from(image, 'base64');
         const type = 'image/' + imageType;
         const date = new Date(timestamp);
         const months = [
            'Jan',
            'Feb',
            'Mar',
            'Apr',
            'May',
            'Jun',
            'Jul',
            'Aug',
            'Sep',
            'Oct',
            'Nov',
            'Dec',
         ];
         const formattedDate =
            date.getDate() +
            ' ' +
            months[date.getMonth()] +
            ' ' +
            date.getFullYear() +
            ' ' +
            date.getHours() +
            ':' +
            date.getMinutes();
         await Task.findByIdAndUpdate(taskId, {
            image: buffer,
            imageType: type,
            lastModified: formattedDate,
         });
         res.status(200).send({ msg: 'Successful' });
      }

      res.status(200).send(task);
   } catch (err) {
      res.status(403).send(err);
   }
});

module.exports = router;
