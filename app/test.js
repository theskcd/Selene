var http=require('http');
var request=require('request');

var test=function(){
	console.log('here');
	request.get({'url':'https://graph.facebook.com/me/friends?access_token=CAAX5cSWqgnoBAMwNOQTXMtblZCNd6vxH6CtovwAmh7fY1ZCWLk8YQKatnHqMcBrCZAoGUkyJunz1xjRT9UjFccbMiQVtORUhAyE0fjoHeracjAZB03ShraMLGpFQkk3XEWjQ0G9DPwlTZAXqpXshYcEvVnhwRxxZA8tytPAuJGNiqOcquIoZCgIOY8HgzdQNS8ZD','proxy':"http://10.3.100.207:8080"},function(error,response,body){
		 console.log(body);
		});
}

test();