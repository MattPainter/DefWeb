package com.example.matt.defweb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Matt on 02/11/2014.
 * mostly following tutorial from here:
 * http://www.vogella.com/tutorials/AndroidSQLite/article.html
 * and here:
 * https://developer.android.com/guide/topics/data/data-storage.html#db
 */

public class DatabaseHelper extends SQLiteOpenHelper{
    //Define database and table name
    public static final String DATABASE_NAME = "definitionDatabase.db";
    public static final String TABLE_NAME = "definitions";
    //Define column names
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME_TEXT = "name";
    public static final String COLUMN_IMG_LOC = "img_loc";

    public static final int DATABASE_VERSION = 1;

    private static final String CREATE_DATABASE = "CREATE_TABLE " + TABLE_NAME +
                                           "(" + COLUMN_ID + " integer primary key autoincrement, " +
                                            COLUMN_NAME_TEXT + " text not null, " +
                                            COLUMN_IMG_LOC + " text not null);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

//TODO: create seperate class for each table - ie analysis, algebra etc
