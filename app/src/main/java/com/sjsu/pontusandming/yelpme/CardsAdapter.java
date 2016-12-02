package com.sjsu.pontusandming.yelpme;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sjsu.pontusandming.yelpme.data.DBHelper;
import com.sjsu.pontusandming.yelpme.data.Location;
import com.sjsu.pontusandming.yelpme.data.PhotoModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.MyViewHolder> {

    public static boolean SHOWING_SAVED = false;
    private List<PhotoModel> photoList;
    private Context mContext;
    private int width;
    public  DBHelper mDbHelper;
    private final int USER_PHOTO_SIZE = 70;
    private final int RECYCLER_SIZE_WIDTH_DIFF = 20;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView photographer, likes, location;
        public ImageView photo, user_photo;

        public MyViewHolder(View view) {
            super(view);
            photographer = (TextView) view.findViewById(R.id.mPhotographerTxt);
            photo = (ImageView) view.findViewById(R.id.mPhotoView);
            user_photo = (ImageView) view.findViewById(R.id.mUserPhoto);
            location = (TextView) view.findViewById(R.id.mPositionTxt);
            photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!SHOWING_SAVED)
                        showSaveDbDialog(getPosition());
                    else{
                        showRemoveFromDbDialog(getPosition());
                    }




                }
            });
            likes = (TextView) view.findViewById(R.id.mLikesTxt);
        }


    }


    public CardsAdapter(List<PhotoModel> photoList) {
        this.photoList = photoList;
    }


    /**
     * Method to save a photo to database. Invoked when image is tapped.
     * @param pos
     */
    private void savePhotoToDb(int pos)
    {
        Log.d("CardsAdapter", "save to db url: " + photoList.get(pos).getUrl_regular());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        PhotoModel photo = photoList.get(pos);
        Location loc = photo.getLocation();
        String city = "";
        String country = "";
        if(loc != null){
            if(loc.getCity() != null)
                city = loc.getCity();
            if(loc.getCountry() != null)
                country = loc.getCountry();
        }
        values.put(DBHelper.COLUMN_NAME_URL, photo.getUrls().getRegular());
        values.put(DBHelper.COLUMN_NAME_USER, photo.getUser().getUsername());
        values.put(DBHelper.COLUMN_NAME_LIKES, "" + photo.getLikes());
        values.put(DBHelper.COLUMN_NAME_USER_URL, photo.getUser().getProfile_image().getSmall());
        values.put(DBHelper.COLUMN_NAME_LOCATION_CITY, city);
        values.put(DBHelper.COLUMN_NAME_LOCATION_COUNTRY, country);

        Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME+";", null);
        String url = photo.getUrls().getRegular();
        boolean found = false;

        /*
        * Checking if the image is already in database. If not, save to database.
         */
        if (c.moveToFirst()) {
            do {
                if (c != null) {
                    c.getString(c.getColumnIndex(DBHelper._ID));
                    String db_url = c.getString(1);
                    Log.d("CardsAdapter", "savePhotoDb loop db_url: " + db_url);
                    if (db_url.equals(url)) {
                        found = true;
                        break;
                    }
                }
            } while (c.moveToNext());
        }

        if(!found) {
            Log.d("CardsAdapter", "not found SAVING: " + values.get(DBHelper.COLUMN_NAME_URL));
            db.insert(DBHelper.TABLE_NAME, null, values);
        }

        db.close();

    }

    /**
     * Method to remove image from database. Invoked when image tapped in "Saved pics" view.
     * @param pos
     */
    private void removeFromDb(int pos){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String url = photoList.get(pos).getUrl_regular();
        Log.d("CardsAdapter", "remove from Db url: " + url);

        String[] projection = {
                DBHelper._ID,
                DBHelper.COLUMN_NAME_URL,
                DBHelper.COLUMN_NAME_USER,
                DBHelper.COLUMN_NAME_LIKES
        };

        Cursor c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME, null);



        if (c.moveToFirst()) {
            do {
                if (c != null) {
                    c.getString(c.getColumnIndex(DBHelper._ID));
                    int id = c.getInt(0);
                    String urlmain = c.getString(1);
                    Log.d("CardsAdapter", "removeFrom db url: " + urlmain + "with db id: " + id);
                    if (urlmain.equals(url)) {
                        Log.d("CardsAdapter", "Found URL, deleting! " + "DELETE FROM " + DBHelper.TABLE_NAME + " WHERE " + DBHelper._ID + " = " + id + ";");
                        String selection = DBHelper._ID + " LIKE ?";
                        String[] selectionArgs = {"" + id};
                        db.delete(DBHelper.TABLE_NAME, selection, selectionArgs);
                        photoList.remove(pos);
                        this.notifyDataSetChanged();
                        break;
                    }
                }
            } while (c.moveToNext());
        }

        db.close();
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_card, parent, false);

        mContext = parent.getContext();
        mDbHelper = new DBHelper(mContext);
        //mDbHelper = DBHelper.getHelperInstance();
        width = mContext.getResources().getDisplayMetrics().widthPixels;
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PhotoModel photo = photoList.get(position);
        String city = "";
        String country = "";
        String between = "";
        if(photo.getLocation() != null) {
            between = ", ";
            city = photo.getLocation().getCity();
            country = photo.getLocation().getCountry();
            Log.d("CardsAdapter", "city:" + city + "country: " + country);
            if(city == null || city.equals("")) {
                city = "";
                between = "";
            }
            if(country == null || country.equals("")) {
                country = "";
                between = "";
            }


        }

        Log.d("CardsAdapter", "Loading view photo url: " + photo.getUrl_regular());


        //Final declaration to use in callback so load rest of info when image has been loaded!
        final String city_final = city;
        final String country_final = country;
        final String between_final = between;
        final MyViewHolder holder_final = holder;
        final PhotoModel photo_final = photo;
        Picasso.with(mContext)
                .load(photo.getUrl_regular())
                .resize(width-RECYCLER_SIZE_WIDTH_DIFF,width-RECYCLER_SIZE_WIDTH_DIFF)
                .into(holder.photo, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Picasso.with(mContext)
                                .load(photo_final.getUser().getProfile_image().getSmall()).resize(USER_PHOTO_SIZE, USER_PHOTO_SIZE).into(holder_final.user_photo);
                        holder_final.photographer.setText(photo_final.getUser().getUsername());
                        holder_final.likes.setText(photo_final.getLikes() + " likes");
                        holder_final.location.setText(city_final + between_final + country_final);
                    }

                    @Override
                    public void onError() {
                        Log.d("CardsAdapter", "Error loading image with picasso");
                    }
                });


    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }


    /**
     * Method to show dialog asking if save image
     * @param pos
     */
    private void showSaveDbDialog(final int pos){
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Save image");
        alertDialog.setMessage("Do you want to save image for later? You find them " +
                "pressing the menu list up in top right corner!");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        savePhotoToDb(pos);
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    /**
     * Method to show dialog to ask if remove saved images
     * @param pos
     */
    private void showRemoveFromDbDialog(final int pos){
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Remove saved image");
        alertDialog.setMessage("Do you want to remove this saved image?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        removeFromDb(pos);
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}