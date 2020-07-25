const mongoose = require('mongoose');

const taskSchema = mongoose.Schema({
   order: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Order',
   },
   product: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Product',
   },
   woman: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Woman',
   },
   quantity: {
      type: Number,
      default: 0,
      required: true,
   },
   currentQuantity: {
      type: Number,
      default: 0,
      required: true,
   },
   approvedQuantity: {
      type: Number,
      default: 0,
   },
   image: {
      type: Buffer,
   },
   imageType: {
      type: String,
   },
   lastModified: {
      type: Date,
      default: Date.now,
   },
   isApproved: {
      type: Boolean,
      default: false,
   },
   date: {
      type: Date,
      default: Date.now,
   },
});

taskSchema.virtual('imagePath').get(function () {
   if (this.image != null && this.imageType != null) {
      return `data:${this.imageType};charset=utf-8;base64,${this.image.toString(
         'base64'
      )}`;
   }
});

taskSchema.virtual('efficiency').get(function () {
   return this.quantity != 0 ? this.approvedQuantity / this.quantity : 0;
});

module.exports = mongoose.model('Task', taskSchema);
