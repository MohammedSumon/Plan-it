//import all npm packages needed
var express = require('express');
var mongoose = require('mongoose');
var increment = require('mongoose-auto-increment');
var morgan = require('morgan');
var fs = require('fs');


module.exports = app = express();

var database = require('./database');
//import config file
var config = require('./config');

//import all controllers
var eventCtrl = require('./controllers/event');
var userCtrl = require('./controllers/user');
var msgCtrl = require('./controllers/messages');

database.createConnection();

// create a write stream (in append mode)
var accessLogStream = fs.createWriteStream(__dirname + '/access.log', {flags: 'a'})

// setup the logger
app.use(morgan('combined', {stream: accessLogStream}))

app.get('/events', eventCtrl.getAllEvents);
app.post('/events', eventCtrl.createEvent);

app.put('/events/:id', eventCtrl.updateEvent);
app.get('/events/user', eventCtrl.getUsersEvents);
app.post('/events/:id/paying/:friendId([0-9]*$)', eventCtrl.paying);
app.post('/events/:id/invite/:friendId([0-9]*$)', eventCtrl.inviteFriend);
app.post('/events/:id/invite/:answer(Attending|Declined)', eventCtrl.invitation);
app.post('/events/:id/leave', eventCtrl.leave);
app.get('/events/:id/budget', eventCtrl.budget);
app.delete('/events/:id', eventCtrl.deleteEvent);
app.get('/events/:id/list', eventCtrl.getListItems);
app.post('/events/:id/claim/:item', eventCtrl.claimItem);
app.delete('/events/:id/list/:item', eventCtrl.deleteItem);
app.put('/events/:id/list/:item', eventCtrl.updateItem);
app.post("/events/:id/list", eventCtrl.createListItem);
app.get('/events/:id/members', eventCtrl.getMembersByEventId);
app.post("/user/:id/friend", userCtrl.addNewFriend);
app.get("/user/:id/friend", userCtrl.getAllFriends);
app.get("/search/:id/user", userCtrl.findUserByID);
app.get("/search/:param/friend", userCtrl.findUserByFriendlyNameOrEmail);
app.delete("/user/:id/friend/:friendId", userCtrl.removeFriend);
app.post("/user", userCtrl.createUser);
app.post("/login", userCtrl.loginUser);
app.get('/events/:id([0-9]*$)', eventCtrl.getEventById);
app.post('/message/:event', msgCtrl.createMessage);
app.get('/message/:event/id/:msgid', msgCtrl.getAllMessagesSinceID);



app.listen(443, function() {
  console.log("Server is running on port 3000");
});
