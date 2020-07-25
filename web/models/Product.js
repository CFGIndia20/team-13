const mongoose = require('mongoose');

const productSchema = mongoose.Schema({
   name: {
      type: String,
      required: true,
   },
   skill: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Skill',
   },
   hours: {
      type: Number,
      default: 0,
   },
});

module.exports = mongoose.model('Product', productSchema);
