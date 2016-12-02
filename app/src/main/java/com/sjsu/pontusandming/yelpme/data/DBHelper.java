package com.sjsu.pontusandming.yelpme.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PontusArfwedson on 2016-10-03.
 */
public class DBHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Unsplashphotos.db";
    Context mContext;


    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }


    /**
     * SQL STRINGS
     */

    public static final String _ID = "id";
    public static final String TABLE_NAME = "Photos";
    public static final String COLUMN_NAME_USER = "user";
    public static final String COLUMN_NAME_LIKES = "likes";
    public static final String COLUMN_NAME_URL = "url";
    public static final String COLUMN_NAME_USER_URL = "user_url";
    public static final String COLUMN_NAME_LOCATION_CITY = "loc_city";
    public static final String COLUMN_NAME_LOCATION_COUNTRY = "loc_country";
    private static final String TEXT_TYPE = " VARCHAR(255)";
    private static final String COMMA_SEP = ",";

    /**
     * SQL QUERIES FOR SETUP AND DELETE TABLE
     */
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID+ " INTEGER PRIMARY KEY NOT NULL" + COMMA_SEP +
                    COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_USER + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_LIKES + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_USER_URL + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_LOCATION_CITY + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_LOCATION_COUNTRY + TEXT_TYPE
                    + " )";
     private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
