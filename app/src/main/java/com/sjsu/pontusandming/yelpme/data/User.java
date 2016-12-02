package com.sjsu.pontusandming.yelpme.data;

/**
 * Created by PontusArfwedson on 2016-10-02.
 */
public class User
{
    private String id;

    private String username;

    private String bio;
    private String total_photos;
    private String total_likes;
    private String portfolio_url;
    private String name;
    private Profile_image profile_image;

    public Profile_image getProfile_image(){
        return profile_image;
    }

    public void setProfile_image(Profile_image profile_image){
        this.profile_image = profile_image;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public String getBio ()
    {
        return bio;
    }

    public void setBio (String bio)
    {
        this.bio = bio;
    }



    public String getPortfolio_url ()
    {
        return portfolio_url;
    }

    public void setPortfolio_url (String portfolio_url)
    {
        this.portfolio_url = portfolio_url;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

}