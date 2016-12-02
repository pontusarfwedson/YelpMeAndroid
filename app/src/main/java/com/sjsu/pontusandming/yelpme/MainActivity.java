package com.sjsu.pontusandming.yelpme;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sjsu.pontusandming.yelpme.data.DBHelper;
import com.sjsu.pontusandming.yelpme.data.Location;
import com.sjsu.pontusandming.yelpme.data.PhotoModel;
import com.sjsu.pontusandming.yelpme.data.Profile_image;
import com.sjsu.pontusandming.yelpme.data.Results;
import com.sjsu.pontusandming.yelpme.data.RetrofitHelper;
import com.sjsu.pontusandming.yelpme.data.SearchModel;
import com.sjsu.pontusandming.yelpme.data.UnsplashPicService;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final int NUM_PHOTOS = 10;
    private int num_photos_added = 0;
    private final String ACTIVITY_TAG = "MainActivity";
    private final String APP_KEY = "8fcabeeadf1bfccacda6d5b9437fcf4359be9ae447b4022a893067a1c8a183ec";



    private UnsplashPicService unsplashPicService;

    private List<PhotoModel> photoList = new ArrayList<>();
    private RecyclerView mPhotosView;
    private CardsAdapter mAdapter;
    private EditText mSearchTxt;
    private TextView mInfoTxt;

    private DBHelper mDbHelper;


    //*************YELP**************
    private final String consumerKey= "E5CV8mVecxYbtpIZA42O2w";
    private final String consumerSecret= "e5O-BgLxS3WWUsvq5uebexznFwY";
    private final String token= "OiJ3aLURjfj5zrKXiBtqf9GMQtQiZWCx";
    private final String tokenSecret= "0lvGHcMblg7mEANDqJSKAEOxQ7w";
    private YelpAPIFactory apiFactory;
    private YelpAPI yelpAPI = apiFactory.createAPI();
    //*******************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPhotosView = (RecyclerView) findViewById(R.id.cards_list);
        mSearchTxt = (EditText) findViewById(R.id.mSearchEt);
        mInfoTxt = (TextView) findViewById(R.id.mInfoTxt);

        mAdapter = new CardsAdapter(photoList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mPhotosView.setLayoutManager(mLayoutManager);
        mPhotosView.setItemAnimator(new DefaultItemAnimator());
        mPhotosView.setAdapter(mAdapter);

        mDbHelper = new DBHelper(this);

        RetrofitHelper.init();
        unsplashPicService = RetrofitHelper.getRandomPicService();


        //********* YELP ***********
        apiFactory = new YelpAPIFactory(consumerKey, consumerSecret, token, tokenSecret);
        yelpAPI = apiFactory.createAPI();
        //***************************
    }




    /**
     * Simple method to reset som variables
     */
    public void resetVars(){
        num_photos_added=0;
        mSearchTxt.setText("");
        photoList.clear();
    }


    /**
     * Method run when clicking "Search". If search is not empty it will perform it.
     * @param v
     */
    public void getSearchPhotos(View v)
    {

        String searchQuery = mSearchTxt.getText().toString().toLowerCase().trim();
        if(!searchQuery.equals("")) {
            CardsAdapter.SHOWING_SAVED = false;
            getUnsplashSearch(searchQuery);
        }else{

            Toast.makeText(this, "\"It is hard to find if do not know what to look for\" - old chinese saying",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void getYelpSearch(String town) throws IOException {
        Map<String, String> params = new HashMap<>();

        // general params
        params.put("term", "food");
        params.put("limit", "3");

        // locale params
        params.put("lang", "en");

        Call<SearchResponse> call = yelpAPI.search(town, params);
        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                SearchResponse searchResponse = response.body();
                Toast.makeText(getApplicationContext(), searchResponse.businesses().get(0).name(), Toast.LENGTH_LONG).show();

                // Update UI text with the searchResponse.
            }
            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                // HTTP error happened, do something to handle it.
            }
        };
        call.enqueue(callback);
    }


    /**
     * Method called when seaching. If gets error 403 shows dialog that rate limit exceeded.
     * @param searchQuery
     */
    public void getUnsplashSearch(String searchQuery){
       Call<SearchModel> searchModel = unsplashPicService.getPhotosSearch(APP_KEY, searchQuery, 1);
        searchModel.enqueue(new Callback<SearchModel>() {
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {

                if(response.body() == null) {
                    mInfoTxt.setText(getResources().getString(R.string.search_failed));
                    boolean rateLimit = false;
                    try {
                        String errorBody = response.errorBody().string();
                        if(errorBody.substring(0,3).equals("403"))
                            rateLimit = true;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(rateLimit)
                        showRateDialog();
                }
                else {
                    mInfoTxt.setText(getResources().getString(R.string.search_photos) + " \"" + mSearchTxt.getText().toString().trim()+"\"");
                    //photoList = response.body();
                    Results results[] = response.body().getResults();
                    photoList.clear();
                    if(results.length == 0){
                        mInfoTxt.setText("No results found for \"" + mSearchTxt.getText().toString().trim() + "\"");
                    }else{
                        mInfoTxt.setText(getResources().getString(R.string.search_photos) + " \"" + mSearchTxt.getText().toString().trim()+"\"");
                    }
                    for( int i = 0; i < results.length; i++){
                        PhotoModel photo = new PhotoModel();
                        photo.setUrl_regular(results[i].getUrls().getRegular());
                        photo.setUsername(results[i].getUser().getUsername());
                        photo.setLikes(results[i].getLikes());
                        Profile_image prof = new Profile_image();
                        prof.setSmall(results[i].getUser().getProfile_image().getSmall());
                        photo.getUser().setProfile_image(prof);
                        Location loc = new Location();
                        String city = "";
                        String country = "";
                        if(results[i].getLocation() != null){
                            if(results[i].getLocation().getCity() != null){
                                city = results[i].getLocation().getCity();

                            }
                            if(results[i].getLocation().getCountry() != null){
                                country = results[i].getLocation().getCountry();
                            }
                        }
                        loc.setCity(city);
                        loc.setCountry(country);
                        photo.setLocation(loc);
                        photoList.add(photo);
                    }
                    mAdapter.notifyDataSetChanged();
                }



            }

            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {
                Log.d(ACTIVITY_TAG, "Retrofit failed");

            }
        });

    }

    /**
     * Inflates the menu
     * @param menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Defines the functionality when clicking menu items
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getTitle().toString().equals(getResources().getString(R.string.saved_menu)))
        {
            mSearchTxt.setText("");
            setPhotosFromDb();
        }
        else
        {

        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Method to show dialog when rate limit exceedd.
     */
    private void showRateDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Rate limit exceeded");
        Calendar c = Calendar.getInstance();
        int minutes = c.get(Calendar.MINUTE);

        alertDialog.setMessage("App in dev mode, rate limit exceeded on unsplash.com. Please wait until next hour, so "+
                (60-minutes)  +" minutes!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


    /**
     * Method to show photos stored in DB. Used when user clicks "Saved pics" menu item.
     */
    public void setPhotosFromDb(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String[] projection = {
                DBHelper._ID,
                DBHelper.COLUMN_NAME_URL,
                DBHelper.COLUMN_NAME_USER,
                DBHelper.COLUMN_NAME_LIKES
        };

        Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME, null);
        photoList.clear();

        if (c.moveToFirst()) {
            do {
                if (c != null) {
                    c.getString(c.getColumnIndex(DBHelper._ID));
                    String url = c.getString(1);
                    String user = c.getString(2);
                    int likes = Integer.parseInt(c.getString(3));
                    String user_url = c.getString(4);
                    String loc_city = c.getString(5);
                    String loc_country = c.getString(6);

                    Log.d("MainActivity", "user_url: " + user_url + "  city: " + loc_city + "  country: " + loc_country);

                    PhotoModel photo = new PhotoModel();
                    photo.setUrl_regular(url);
                    photo.setUsername(user);
                    photo.setLikes(likes);
                    Profile_image prof = new Profile_image();
                    prof.setSmall(user_url);
                    photo.getUser().setProfile_image(prof);
                    Location loc = new Location();
                    loc.setCity(loc_city);
                    loc.setCountry(loc_country);
                    photo.setLocation(loc);
                    photoList.add(photo);

                    Log.d("MainActivity", "db url: " + url);
                }
            } while (c.moveToNext());
        }

        db.close();
        mAdapter.notifyDataSetChanged();
        CardsAdapter.SHOWING_SAVED = true;
        mInfoTxt.setText(getResources().getString(R.string.saved_photos));
    }


}
