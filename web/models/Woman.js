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
      ref: 'District',
   },
   skills: [
      {
         type: mongoose.Schema.Types.ObjectId,
         ref: 'Skill',
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
   efficiency: {
      type: Number,
      default: 0,
   },
   amountOfHours: {
      type: Number,
      default: 0,
   },
   noOfSessions: {
      type: Number,
      default: 0,
   },
   isFree: {
      type: Boolean,
      default: true,
   },
});

module.exports = mongoose.model('Woman', womanSchema);
