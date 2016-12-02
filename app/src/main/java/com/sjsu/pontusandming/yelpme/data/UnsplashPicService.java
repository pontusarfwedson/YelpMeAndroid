package com.sjsu.pontusandming.yelpme.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Modeling the unsplash.com API.
 */
public interface UnsplashPicService {



    @GET("/photos/random")
    Call<PhotoModel> getPhotoRandom(@Query("client_id") String app_key);


    @GET("/search/photos")
    Call<SearchModel> getPhotosSearch(@Query("client_id") String app_key, @Query("query") String query, @Query("page") int pagenum);

}