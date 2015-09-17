package com.example.luc.timetracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mytimes.db";
    private static final int DATABASE_VERSION = 3;

    public static final String TABLE_TIMES = "times";
    public static final String COLUMN_ITEM_ID = "_id";
    public static final String COLUMN_ITEM_DESCRIPTION = "description";

    public static final String TABLE_TAGS = "tags";
    public static final String COLUMN_TAG_ID = "tags_id";
    public static final String COLUMN_TAG = "tag";

    // Creating the table
    private static final String DATABASE_CREATE_TIMES =
            "CREATE TABLE " + TABLE_TIMES +
                    "(" +
                    COLUMN_ITEM_ID + " integer primary key autoincrement, " +
                    COLUMN_ITEM_DESCRIPTION + " text not null, " +
                    COLUMN_TAG_ID + " integer, " +
                    "FOREIGN KEY(" + COLUMN_TAG_ID + ") REFERENCES " + TABLE_TAGS + "(" +
                    COLUMN_TAG_ID + ") ON DELETE CASCADE" +
                    ");";

    private static final String DATABASE_CREATE_TAGS =
            "CREATE TABLE " + TABLE_TAGS +
                    "(" +
                    COLUMN_TAG_ID + " integer primary key autoincrement, " +
                    COLUMN_TAG + " text not null" +
                    ");";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        // Execute the sql to create the table assignments
        database.execSQL(DATABASE_CREATE_TAGS);
        database.execSQL(DATABASE_CREATE_TIMES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        * When the database gets upgraded you should handle the update to make sure there
        is no data loss.
        * This is the default code you put in the upgrade method , to delete the table and
        call the oncreate again.
        */
        Log.w(MySQLiteHelper.class.getName(), "Upgrading database from version " +
                oldVersion + " to " + newVersion + ", which will destroy all old data");
        if (oldVersion < 3) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS);
            onCreate(db);
        }
    }
}
