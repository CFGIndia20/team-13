const LocalStrategy = require('passport-local').Strategy;
const mongoose = require('mongoose');
const bcrypt = require('bcryptjs');

// Load User Model
const User = require('./models/User');

module.exports = function (passport) {
   passport.use(
      new LocalStrategy({ usernameField: 'phone' }, (phone, password, done) => {
         // Match User
         User.findOne({ phone: phone })
            .then((user) => {
               if (!user) {
                  return done(null, false, {
                     message: 'Phone is not registered',
                  });
               }

               // Match Password
               bcrypt.compare(password, user.password, (err, isMatch) => {
                  if (err) throw err;
                  if (isMatch) {
                     return done(null, user);
                  } else {
                     return done(null, false, {
                        message: 'Password incorrect',
                     });
                  }
               });
            })
            .catch((err) => console.error(err));
      })
   );

   passport.serializeUser((user, done) => {
      done(null, user.id);
   });

   passport.deserializeUser((id, done) => {
      User.findById(id, function (err, user) {
         done(err, user);
      });
   });
};
