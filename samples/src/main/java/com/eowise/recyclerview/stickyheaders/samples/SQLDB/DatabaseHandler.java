package com.eowise.recyclerview.stickyheaders.samples.SQLDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.eowise.recyclerview.stickyheaders.samples.Notification.Notification_Settings;
import com.eowise.recyclerview.stickyheaders.samples.SingleTon;
import com.eowise.recyclerview.stickyheaders.samples.data.PeopleData;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "LastNightDress";
    private static final String TABLE_FAVORITE = "favorite";
    private static final String TABLE_USER = "users";
    private static final String TABLE_NOTIFICATION = "notification";
    private static final String KEY_POSTID = "postid";
    private static final String KEY_IMAGEURL = "imageurl";
    private static final String KEY_COST = "cost";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USERID = "userid";
    private static final String KEY_IS_FOLLOW_ENABLE="follow_noti";
    private static final String KEY_IS_LIKES_ENABLE="likes_noti";
    private static final String KEY_IS_COMMENT_ENABLE="comment_noti";
    private static final String KEY_IS_SHARE_ENABLE="share_noti";
    private static final String KEY_IS_SWAP_ENABLE="swap_noti";
    private static final String KEY_IS_SALE_ENABLE="sale_noti";
    private static final String KEY_IS_MESSAGE_ENABLE="message_noti";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_FAVORITE + "("
                + KEY_POSTID + " TEXT PRIMARY KEY," + KEY_IMAGEURL + " TEXT,"
                + KEY_COST + " TEXT" + ")";

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_USERNAME + " TEXT," + KEY_IMAGEURL + " TEXT,"
                 +  KEY_USERID + " TEXT PRIMARY KEY "+ ")";
        String CREATE_NOTIFICATION_TABLE = "CREATE TABLE " + TABLE_NOTIFICATION + "("
                + KEY_IS_FOLLOW_ENABLE + " TINYINT," + KEY_IS_LIKES_ENABLE + " TINYINT,"
                +  KEY_IS_COMMENT_ENABLE +" TINYINT,"+KEY_IS_SHARE_ENABLE+" TINYINT,"+KEY_IS_SWAP_ENABLE+" TINYINT,"+KEY_IS_SALE_ENABLE+" TINYINT,"+KEY_IS_MESSAGE_ENABLE+" TINYINT,"+KEY_USERID+" TEXT PRIMARY KEY"+ ")";

        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_NOTIFICATION_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
   public long addContact(FavoriteData fav) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_POSTID,fav.getPostid()); // Contact Name

        values.put(KEY_IMAGEURL,fav.getImageurl()); // Contact Name
        values.put(KEY_COST, fav.getCost()); // Contact Phone

              // Inserting Row
         long rows=     db.insertWithOnConflict(TABLE_FAVORITE, null, values, SQLiteDatabase.CONFLICT_REPLACE);


        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    return rows;
    }

    // code to add current user notification settings
    public long addValues(Notification_Settings noti,String userid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERID,userid);
        values.put(KEY_IS_FOLLOW_ENABLE,noti.getFollow());
        values.put(KEY_IS_LIKES_ENABLE,noti.getLikes());
        values.put(KEY_IS_COMMENT_ENABLE,noti.getComment());
        values.put(KEY_IS_SHARE_ENABLE,noti.getShare());
        values.put(KEY_IS_SWAP_ENABLE,noti.getSwap());
        values.put(KEY_IS_SALE_ENABLE,noti.getSale());
        values.put(KEY_IS_MESSAGE_ENABLE,noti.getMessage());

        // Inserting Row
        long rows=     db.insertWithOnConflict(TABLE_NOTIFICATION, null, values, SQLiteDatabase.CONFLICT_REPLACE);


        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
        return rows;
    }
    // code to get the single contact
  public  FavoriteData getContact(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FAVORITE, new String[] { KEY_POSTID,
                        KEY_IMAGEURL, KEY_COST }, KEY_POSTID + "=?",
                new String[] { id }, null, null, null, null);
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();

            FavoriteData fav = new FavoriteData(cursor.getString(0).toString(), cursor.getString(1).toString(), cursor.getString(2).toString());
            // return contact
            return fav;
        }
        return null;
    }

    // code to get the user settings
    public  ArrayList<Integer> getSettings(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTIFICATION, new String[] { KEY_IS_FOLLOW_ENABLE,
                        KEY_IS_LIKES_ENABLE,KEY_IS_COMMENT_ENABLE,KEY_IS_SHARE_ENABLE,KEY_IS_SWAP_ENABLE,KEY_IS_SALE_ENABLE,KEY_IS_MESSAGE_ENABLE }, KEY_USERID + "=?",
                new String[] { id }, null, null, null, null);
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            ArrayList<Integer> values=new ArrayList<>();
            values.add(cursor.getInt(0));
            values.add(cursor.getInt(1));
            values.add(cursor.getInt(2));
            values.add(cursor.getInt(3));
            values.add(cursor.getInt(4));
            values.add(cursor.getInt(5));
            values.add(cursor.getInt(6));

            // return contact
            return values;
        }
        return null;
    }


    // code to get all contacts in a list view
    public List<FavoriteData> getAllContacts() {
        List<FavoriteData> favList = new ArrayList<FavoriteData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FAVORITE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FavoriteData fav = new FavoriteData();
                fav.setPostid(cursor.getString(0));
                fav.setImageurl(cursor.getString(1));
                fav.setCost(cursor.getString(2));
                // Adding contact to list
                favList.add(fav);
            } while (cursor.moveToNext());
        }

        // return contact list
        return favList;
    }

    // code to update the single contact
    public int updateContact(FavoriteData fav) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IMAGEURL,fav.getImageurl());
        values.put(KEY_COST, fav.getCost());

        // updating row
        return db.update(TABLE_FAVORITE, values, KEY_POSTID + " = ?",
                new String[] { fav.getPostid() });
    }
    // code to update the settings
    public int updateSettings(ArrayList<Integer> noti) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IS_FOLLOW_ENABLE,noti.get(0));
        values.put(KEY_IS_LIKES_ENABLE,noti.get(1));
        values.put(KEY_IS_COMMENT_ENABLE,noti.get(2));
        values.put(KEY_IS_SHARE_ENABLE,noti.get(3));
        values.put(KEY_IS_SWAP_ENABLE,noti.get(4));
        values.put(KEY_IS_SALE_ENABLE,noti.get(5));
        values.put(KEY_IS_MESSAGE_ENABLE,noti.get(6));


        // updating row
        return db.update(TABLE_NOTIFICATION, values, KEY_USERID + " = ?",
                new String[] {SingleTon.pref.getString("user_id","")});
    }

    // Deleting single contact
    public void deleteContact(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITE, KEY_POSTID + " = ?",
                new String[]{id});
        db.close();
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FAVORITE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }


    // code to get all contacts in a list view
    public List<PeopleData> getAllUsers() {
        List<PeopleData> peopleList = new ArrayList<PeopleData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PeopleData people = new PeopleData();
                people.setUname(cursor.getString(0));
                people.setImageurl(cursor.getString(1));
                people.setUserid(cursor.getString(2));

                // Adding people to list
                peopleList.add(people);
            } while (cursor.moveToNext());
        }

        // return contact list
        return peopleList;
    }

    // code to add the people
    public long addPoeple(PeopleData people){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME,people.getUname()); // user Name

        values.put(KEY_IMAGEURL,people.getImageurl()); // imageurl
        values.put(KEY_USERID,people.getUserid()); // userid
        // Inserting Row
        long rows=     db.insertWithOnConflict(TABLE_USER, null, values, SQLiteDatabase.CONFLICT_REPLACE);


        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
        Log.e("values", rows+"");
        return 0;
    }
public void clearAll()
{
    SQLiteDatabase db = this.getWritableDatabase();


    db.execSQL("delete from "+ TABLE_USER);
}
}