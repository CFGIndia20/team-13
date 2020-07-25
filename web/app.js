const path = require('path');
const express = require('express');
const bodyParser = require('body-parser');
const mongoose = require('mongoose');
const session = require('express-session');
const passport = require('passport');
require('dotenv').config();

const errorController = require('./controllers/error');

const app = express();

// Passport config
require('./passport')(passport);

// Connect to Mongo
mongoose.connect(process.env.MONGO_URL, {
   useNewUrlParser: true,
   useUnifiedTopology: true,
});
const db = mongoose.connection;
db.on('error', (error) => console.error(error));
db.once('open', () => console.log('Connected to Mongoose'));

// Middleware
app.set('view engine', 'ejs');
app.set('views', 'views');

app.use(bodyParser.urlencoded({ extended: true }));
app.use(express.json());
app.use(express.static(path.join(__dirname, 'public')));

// Session
app.use(
   session({
      secret: 'secret',
      resave: true,
      saveUninitialized: true,
   })
);

// Passport middleware
app.use(passport.initialize());
app.use(passport.session());

// Routes
app.use('/', require('./routes/index'));
app.use('/users', require('./routes/auth'));
app.use('/skills', require('./routes/skills'));
app.use('/districts', require('./routes/district'));
app.use('/women', require('./routes/woman'));
app.use('/product', require('./routes/product'));
app.use('/order', require('./routes/order'));

app.use(errorController.get404);

const PORT = process.env.PORT || 3000;

app.listen(PORT, console.log(`Server started at port ${PORT}`));

const Woman = require('./models/Woman');

// Reset compensation on the 5th day of every month
const schedule = require('node-schedule');
const Task = require('./models/Task');
const Product = require('./models/Product');
let rule = new schedule.RecurrenceRule();
rule.month = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11];
rule.date = 5;
rule.hour = 0;
rule.minute = 0;

let resetJob = schedule.scheduleJob(rule, async () => {
   try {
      await Woman.update({}, { $set: { compensation: 0 } }, { multi: true });
   } catch (err) {
      console.log(err);
   }
});

// Calculate compensation on the 1st day of every month
let rule2 = new schedule.RecurrenceRule();
rule2.month = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11];
rule2.date = 1;
rule2.hour = 0;
rule2.minute = 0;

let calculateJob = schedule.scheduleJob(rule2, async () => {
   try {
      const month = new Date().getMonth();
      const women = await Woman.find();
      for (woman in women) {
         let hours = 0;
         const tasks = await Task.find({
            $expr: {
               $eq: [{ $month: '$date' }, month],
            },
         });
         for (task in tasks) {
            const product = await Product.findById(task.product);
            hours += task.approvedQuantity * product.hours;
         }
         const compensation = hours * 19 + woman.noOfSessions * 200;
         await Woman.findByIdAndUpdate(
            { id: woman._id },
            { compensation: compensation }
         );
      }
   } catch (err) {
      console.log(err);
   }
});
