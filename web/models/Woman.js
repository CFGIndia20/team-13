const mongoose = require('mongoose');

const womanSchema = mongoose.Schema({
   name: {
      type: String,
      required: true,
   },
   password: {
      type: String,
      required: true,
   },
   district: {
      type: mongoose.Schema.Types.ObjectId,
      required: true,
      rel: 'District',
   },
   skills: [
      {
         type: mongoose.Schema.Types.ObjectId,
         rel: 'Skill',
      },
   ],
   phone: {
      type: String,
      required: true,
   },
   compensation: {
      type: Number,
      default: 0,
   },
   amountOfHours: {
      type: Number,
      default: 0,
   },
});

module.exports = mongoose.model('Woman', womanSchema);
