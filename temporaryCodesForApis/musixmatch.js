var http=require('http');
var request=require('request');

var test=function(){
	console.log('here');
	request.get('http://api.musixmatch.com/ws/1.1/track.search?apikey=eed5fbf7c747a67e2bded3ec443cd1e9&fomat=json?q_artist=american%20authors&q_track=best%20day%20of%20my%20life&f_has_lyrics=1&page=1&page_size=1&s_track_rating=desc',function(error,response,body){
		 console.log(body);
		var responseData=JSON.parse(body);
		 console.log(responseData);
		var try_=function(){
			for(var index=0;index<responseData.message.body.track_list.length;index++){
				var lengthGenre=responseData.message.body.track_list[index].track.primary_genres.music_genre_list.length;
				console.log(responseData.message.body.track_list[index].track.track_name+"->");
				var printGenre=function(){
					console.log(lengthGenre);
					for(var indexGenre=0;indexGenre<lengthGenre;indexGenre++){
						console.log(responseData.message.body.track_list[index].track.primary_genres.music_genre_list[indexGenre].music_genre.music_genre_name);
					}
				}
				printGenre();
			}
		}
		try_();
		// var getLenngth=function(){
		// 	 console.log('Get length-> '+responseData.message.body.track_list.length);
		// 	//console.log(responseData.message.body.track_list[0]);
		// }
		// getLenngth();
	})
}

test();