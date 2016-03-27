var express = require('express');
var router = express.Router();
var request = require('request');

var Users = require('../models/Users');

router.use(function (req, res, next) {
	// console.log("here->");
	// console.log(req.query.token);
	if(req.query.token && req.query.fbid) {
		var FB_URL = "https://graph.facebook.com/me?fields=name,email&";
		FB_URL+='access_token=' + req.query.token;
		request.get({'url':FB_URL,'proxy':"http://10.3.100.207:8080"}, function (error, response, body) {
			var responseData=JSON.parse(body);
			// console.log(body);
			if(error) {
				console.log("in 500");
				res.send({ status : 500, err : error});
				return ;
			}
			var responseData = JSON.parse(body);
			if(responseData.id != req.query.fbid) {
				res.send({ status : 400, err : "Not Authorized"});
				return ;
			}
			Users.count({ 'facebook.id' : responseData.id }, function(err, count) {
				if(error) {
					res.send({ status : 500, err : err}); return;
				}
				if(count == 1) {
					// var FB_Fr="https://graph.facebook.com/me/friends?"+"access_token="+req.query.token;
					// var counter1="proceed";
					// console.log(FB_Fr);
					// while(counter1=="proceed"){
					// 	 console.log('here'+ FB_Fr);
					// 	request.get({'url':FB_Fr,'proxy':"http://10.3.100.207:8080"},function (error,response,body){
					// 		var responseData=JSON.parse(body);
					// 		console.log("lol"+" "+responseData);
					// 		FB_Fr=responseData.paging.next;
					// 		if(responseData.paging.hasOwnProperty('previous')){
					// 			counter1="naye";
					// 		}
					// 	});
					// }
					next();
					return;
				} else {
					console.log(responseData);
					var user = new Users();
						user.name = responseData.name;
						user.facebook.id = responseData.id;

					user.save(function (err) {
						console.log({'error':err});
						if(err) {
							res.send({ status : 500, err : err}); return;
						}

						// 	var FB_Fr="https://graph.facebook.com/me/friends?"+"access_token="+req.query.token;
						// 	var counter1=0;
						// 	while(counter1==0){
						// 		request.get({'url':FB_Fr,'proxy':"http://10.3.100.207:8080"},function(error,response,body){
						// 			var responseData=JSON.parse(body);
						// 			console.log("lol"+" "+responseData);
						// 			FB_Fr=responseData.paging.next;
						// 			if(responseData.paging.previous){
						// 				counter1=counter1+1;
						// 			}
						// 	});
						// }
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

// ===========
// User Routes
// ===========
router.route('/users')
	.get(function (req, res) {
		Users.find({}, 'fname lname email', function(err, users) {
			if(err) { 
				res.send({ status : 500, err : err });
			}
			res.json({ status : 200, response : users });
		});
	});

router.route('/user/:user_id')
	.get(function(req, res) {
		Users.findById(req.params.user_id, 'fname lname email', function(err, user) {
			if(err) { 
				res.send({ status : 500, err : err });
			}
			res.json({ status : 200, response : user });
		});
	});


router.route('/login')
	.get(function(req,res){
		res.send({message:'You Made It!'});
	})

router.route('/googleData')
	.get(function(req,res){
		res.send({'userid':req.query.token});
	})


module.exports=router;