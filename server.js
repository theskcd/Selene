// ===========
// BASIC SETUP
// ==========

var express  = require('express');
var app 	 = express();
var port     = process.env.PORT || 8080;

var morgan       = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser   = require('body-parser');
var session      = require('express-session');
var path 		 = require('path');

var mongoose = require('mongoose');

mongoose.connect('mongodb://localhost/Selena');

var APIRouter = require('./app/routes/APIRouter');


// set up our express application
app.use(morgan('dev')); // log every request to the console
app.use(cookieParser()); // read cookies (needed for auth)
app.use(bodyParser.json()); // get information from html forms
app.use(bodyParser.urlencoded({ extended: true }));
app.use(express.static(path.join(__dirname, 'public')));

// view engine setup
app.set('view engine', 'jade');

// ======
// ROUTES
// ======

app.use('/api/v1/',APIRouter);

app.get('/', function (req, res) {
	res.send('Hello! The API is at http://localhost:'+port+"/api");
});

// ===================
// Run The Express App
// ===================
app.listen(port);
console.log('The Hacking Begins at port ' + port);
