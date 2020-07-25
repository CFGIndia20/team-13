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

app.use(bodyParser.urlencoded({ extended: false }));
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

app.use(errorController.get404);

const PORT = process.env.PORT || 3000;

app.listen(PORT, console.log(`Server started at port ${PORT}`));
