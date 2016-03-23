var http=require('http');
var request=require('request');

var test=function(){
	console.log('here');
	request.get({'url':'https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=PL7mSAxuBjwt8MDfDOq6nswV8z7a5Yq5_I&key=AIzaSyD6CYoeesmOj6j91VPRvdngxFR7XqSTjgw','proxy':"http://10.3.100.207:8080"},function(error,response,body){
		 console.log(body);
		});
		// var responseData=JSON.parse(body);
		//  console.log(responseData);
		// var try_=function(){
		// 	for(var index=0;index<responseData.message.body.track_list.length;index++){
		// 		var lengthGenre=responseData.message.body.track_list[index].track.primary_genres.music_genre_list.length;
		// 		console.log(responseData.message.body.track_list[index].track.track_name+"->");
		// 		var printGenre=function(){
		// 			console.log(lengthGenre);
		// 			for(var indexGenre=0;indexGenre<lengthGenre;indexGenre++){
		// 				console.log(responseData.message.body.track_list[index].track.primary_genres.music_genre_list[indexGenre].music_genre.music_genre_name);
		// 			}
		// 		}
		// 		printGenre();
		// 	}
		// }
		// try_();
}

test();