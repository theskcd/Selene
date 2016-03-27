var http=require('http');
var request=require('request');

var test=function(){
	console.log('here');
	request.get({'url':'https://gdata.youtube.com/feeds/api/users/115251366266761042421/playlists?v=2','proxy':"http://10.3.100.207:8080"},function(error,response,body){
		 console.log(body);
		});
}

test();
