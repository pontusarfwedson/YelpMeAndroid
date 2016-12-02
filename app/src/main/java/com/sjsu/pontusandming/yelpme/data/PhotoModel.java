package com.sjsu.pontusandming.yelpme.data;

/**
 * Created by PontusArfwedson on 2016-09-29.
 */
public class PhotoModel {

    String id;
    String created_at;
    Urls urls;
    User user;
    int width;
    int height;
    String color;
    int downloads;
    int likes;
    String url_raw;
    String url_full;
    String url_regular;
    String url_small;
    String url_thumb;
    private Location location;

    public PhotoModel(){
        urls = new Urls();
        user = new User();
    }

    public int getLikes(){
        return likes;
    }

    public void setLikes(int likes){
        this.likes = likes;
    }

    public Location getLocation ()
    {
        return location;
    }

    public void setLocation (Location location)
    {
        this.location = location;
    }


    public String getId(){
        return id;
    }

    public Urls getUrls(){
        return urls;
    }

    public User getUser(){
        return user;
    }

    public String getUrl_regular(){
        return urls.getRegular();
    }

    public void setUrl_regular(String url_regular){
        urls.setRegular(url_regular);
    }

    public void setUsername(String username){
        user.setUsername(username);
    }

    public String getUsername(){
        return user.getUsername();
    }

    public void setCreated_at(String created_at){
        this.created_at = created_at;
    }

    public String getCreated_at()
    {
        return created_at;
    }

    @Override
    public String toString(){
        return "Photo url: " + url_regular + " user: " + user + " likes: " + likes;
    }

}



