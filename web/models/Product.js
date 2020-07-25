const mongoose = require('mongoose');

const productSchema = mongoose.Schema({
   name: {
      type: String,
      required: true,
   },
   skill: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Skill',
      required: true,
   },
   hours: {
      type: Number,
      required: true,
   },
});

module.exports = mongoose.model('Product', productSchema);
