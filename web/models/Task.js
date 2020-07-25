const mongoose = require('mongoose');

const taskSchema = mongoose.Schema({
   order: {
      type: mongoose.Schema.Types.ObjectId,
      rel: 'Order',
   },
   product: {
      type: mongoose.Schema.Types.ObjectId,
      rel: 'Product',
   },
   woman: {
      type: mongoose.Schema.Types.ObjectId,
      rel: 'Woman',
   },
   quantity: {
      type: Number,
      default: 0,
      required: true,
   },
   image: {
      type: Buffer,
   },
   imageType: {
      type: String,
   },
   isApproved: {
      type: Boolean,
      default: false,
   },
});

taskSchema.virtual('imagePath').get(function () {
   if (this.image != null && this.imageType != null) {
      return `data:${this.imageType};charset=utf-8;base64,${this.image.toString(
         'base64'
      )}`;
   }
});

module.exports = mongoose.model('Task', taskSchema);
