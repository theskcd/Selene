var youtube = require("youtube-api");
var ytdl = require("ytdl-core");

youtube.authenticate({
	type:"key",key:"AIzaSyDGwNXYUqRgDraCTf5z7lBdUTgVAfqeSPM"
});

var trying=function(){
	youtube.playlistItems.list({
		part:"snippet",
		pageToken:null,
		maxResults:50,
		playlistId:"PL7mSAxuBjwt8MDfDOq6nswV8z7a5Yq5_I"
	},function(err,data){
		if(err){
			console.log("error");
		}
		else{
			console.log(data);
		}
	})
}

trying();