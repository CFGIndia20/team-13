const mongoose = require('mongoose');

const orderSchema = mongoose.Schema({
   product: {
      type: mongoose.Schema.Types.ObjectId,
      rel: 'Product',
   },
   no: {
      type: Number,
      default: 0,
      required: true,
   },
});

module.exports = mongoose.model('Order', orderSchema);