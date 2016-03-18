package com.apps.gill.loginviaexistingaccount.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.apps.gill.loginviaexistingaccount.models.UserDetails;

/**
 * Created by gill on 18-03-2016.
 */
public class ProjectSQLiteDatabase extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LoginViaExistingAccount";
    private static final String TABLE_USER_INFO = "UserInfo";
    private static final String KEY_ID = "id";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_CONTACT = "contact";
    private static final String KEY_PINCODE = "pincode";
    private static final String KEY_CITY = "city";
    private static final String KEY_STATE = "state";
    private static final String KEY_LOCALITY = "locality";
    private static final String KEY_PIC_URL = "picUrl";


    public ProjectSQLiteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER_INFO + "(" + KEY_ID + "INTEGER PRIMARY KEY,"
                + KEY_FIRST_NAME + " TEXT," + KEY_LAST_NAME + " TEXT," + KEY_CONTACT + " TEXT,"
                + KEY_EMAIL + "TEXT," + KEY_CITY + "TEXT," + KEY_STATE + "TEXT," + KEY_GENDER + "TEXT,"
                + KEY_LOCALITY + "TEXT," + KEY_PINCODE + "TEXT," + KEY_PIC_URL + "TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_INFO);
        onCreate(db);
    }

    void addContact(UserDetails userDetails) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_FIRST_NAME, userDetails.firstName);
        contentValues.put(KEY_LAST_NAME, userDetails.lastName);
        contentValues.put(KEY_GENDER, userDetails.gender);
        contentValues.put(KEY_EMAIL, userDetails.email);
        contentValues.put(KEY_LOCALITY, userDetails.locality);
        contentValues.put(KEY_CITY, userDetails.city);
        contentValues.put(KEY_STATE, userDetails.state);
        contentValues.put(KEY_PINCODE, userDetails.pincode);
        contentValues.put(KEY_PIC_URL, userDetails.picUrl);
        contentValues.put(KEY_CONTACT, userDetails.contact);
        database.insert(TABLE_USER_INFO, null, contentValues);
        database.close();
    }
}
