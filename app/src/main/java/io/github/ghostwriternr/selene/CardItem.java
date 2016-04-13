package io.github.ghostwriternr.selene;

public class CardItem {
    private String albumart;
    private String songtitle;
    private String albumname;
    private String spotify;
    private String soundcloud;
    private String youtube;
    private String artistname;

    public CardItem(String albumart, String songtitle, String artistname, String albumname, String spotify, String soundcloud, String youtube){
        this.albumart = albumart;
        this.songtitle = songtitle;
        this.artistname = artistname;
        this.albumname = albumname;
        this.spotify = spotify;
        this.soundcloud = soundcloud;
        this.youtube = youtube;
    }

    public String getAlbumart(){
        return albumart;
    }

    public void setAlbumart(String albumart){
        this.albumart = albumart;
    }

    public String getSongtitle(){
        return songtitle;
    }

    public void setSongtitle(String songtitle) {
        this.songtitle = songtitle;
    }

    public String getArtistname() {
        return artistname;
    }

    public void setArtistname(String artistname) {
        this.artistname = artistname;
    }

    public String getAlbumname(){
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public String getSpotify(){
        return spotify;
    }

    public void setSpotify(String spotify) {
        this.spotify = spotify;
    }

    public String getSoundcloud(){
        return soundcloud;
    }

    public void setSoundcloud(String soundcloud) {
        this.soundcloud = soundcloud;
    }

    public String getYoutube(){
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }
}
