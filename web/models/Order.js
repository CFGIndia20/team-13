const mongoose = require('mongoose');

const orderSchema = mongoose.Schema({
   name: {
      type: String,
      required: true,
   },
   product: {
      type: mongoose.Schema.Types.ObjectId,
      required: true,
      ref: 'Product',
   },
   quantity: {
      type: Number,
      default: 0,
      required: true,
   },
   remainingQuantity: {
      type: Number,
      required: true,
   },
   progress: {
      type: Number,
      default: 0,
   },
});

module.exports = mongoose.model('Order', orderSchema);
