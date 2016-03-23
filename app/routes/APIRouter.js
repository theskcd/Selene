var express = require('express');
var router = express.Router();
var request = require('request');

var Users=require('../models/Users');

router.get('/',function(req,res){
	res.send({'message':'You Made It!'});
});

router.use(function (req, res, next) {
	console.log('herer');
	if(req.query.token && req.query.fbid) {
		var FB_URL = "https://graph.facebook.com/me?";
		request.get(FB_URL + 'access_token=' + req.query.token, function (error, response, body) {
			if(error) {
				res.send({ status : 500, err : err});
			}
			var responseData = JSON.parse(body);
			if(responseData.id != req.query.fbid) {
				res.send({ status : 400, err : "Not Authorized"});
			}
			Users.count({ 'facebook.id' : responseData.id }, function(err, count) {
				if(error) {
					res.send({ status : 500, err : err}); return;
				}
				if(count == 1) {
					next();
					return;
				} else {
					var user = new Users();
						user.fname = responseData.first_name;
						user.lname = responseData.last_name;
						user.facebook.id = responseData.id;

					user.save(function (err) {
						if(err) {
							res.send({ status : 500, err : err}); return;
						}
						next();
						return;
					});
				}
			});
		});
	} else {
		res.send({ status : 400, err : "No Access Token or User ID Passed"});
	}
});

router.route('/users')
	.get(function(req,res){
		console.log('request for users');
	})

module.exports=router;