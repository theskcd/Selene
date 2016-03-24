var google = require('googleapis')
var youtube = google.youtube({version: 'v3', auth: "AIzaSyD6CYoeesmOj6j91VPRvdngxFR7XqSTjgw",proxy:"http://10.3.100.207:8080"})

var tryRetriv=function(){
  youtube.playlistItems.list({
    part:"snippet",
    playlistId:"PL7mSAxuBjwt-rE7raeScE6AFpaTh56242"
  },function(error,docs){
    if(error){
      console.log(error);
    }
    else{
      console.log(docs);
      for(var index=0;index<docs.items.length;index++){
        console.log(docs.items[index].snippet.title);
      }
    }
  })
}

tryRetriv();