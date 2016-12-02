package com.sjsu.pontusandming.yelpme.data;

/**
 * Created by PontusArfwedson on 2016-10-03.
 */
public class Results
{
    private String id;
    private String height;
    private String[] current_user_collections;
    private String color;
    private Urls urls;
    private int likes;
    private String width;
    private String created_at;
    private User user;
    private Location location;
    private String liked_by_user;
    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getHeight ()
    {
        return height;
    }

    public void setHeight (String height)
    {
        this.height = height;
    }

    public Location getLocation ()
    {
        return location;
    }

    public void setLocation (Location location)
    {
        this.location = location;
    }

    public String[] getCurrent_user_collections ()
    {
        return current_user_collections;
    }

    public void setCurrent_user_collections (String[] current_user_collections)
    {
        this.current_user_collections = current_user_collections;
    }

    public String getColor ()
    {
        return color;
    }

    public void setColor (String color)
    {
        this.color = color;
    }

    public Urls getUrls ()
    {
        return urls;
    }

    public void setUrls (Urls urls)
    {
        this.urls = urls;
    }

    public int getLikes ()
    {
        return likes;
    }

    public void setLikes (int likes)
    {
        this.likes = likes;
    }

    public String getWidth ()
    {
        return width;
    }

    public void setWidth (String width)
    {
        this.width = width;
    }

    public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }




    public User getUser ()
    {
        return user;
    }

    public void setUser (User user)
    {
        this.user = user;
    }

    public String getLiked_by_user ()
    {
        return liked_by_user;
    }

    public void setLiked_by_user (String liked_by_user)
    {
        this.liked_by_user = liked_by_user;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", height = "+height+", current_user_collections = "+current_user_collections+", color = "+color+", urls = "+urls+", likes = "+likes+", width = "+width+", created_at = "+created_at+", user = "+user+", liked_by_user = "+liked_by_user+"]";
    }
}