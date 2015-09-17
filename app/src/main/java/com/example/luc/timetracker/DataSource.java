package com.example.luc.timetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] itemsAllColumns = {MySQLiteHelper.COLUMN_ITEM_ID,
            MySQLiteHelper.COLUMN_ITEM_DESCRIPTION};

    public DataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
        database = dbHelper.getWritableDatabase();
        dbHelper.close();
    }

    // Opens the database to use it
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    // Closes the database when you no longer need it
    public void close() {
        dbHelper.close();
    }

    public long createItem(String description, long tagId) {
        // If the database is not open yet, open it
        if (!database.isOpen())
            open();
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_ITEM_DESCRIPTION, description);
        values.put(MySQLiteHelper.COLUMN_TAG_ID, tagId);
        long insertId = database.insert(MySQLiteHelper.TABLE_TIMES, null, values);
        // If the database is open, close it
        if (database.isOpen())
            close();
        return insertId;
    }

    public long createTag(String course) {
        if (!database.isOpen())
            open();
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_TAG, course);
        long insertId = database.insert(MySQLiteHelper.TABLE_TAGS, null, values);
        if (database.isOpen())
            close();
        return insertId;
    }

    public void deleteTag(Tag tag) {
        if (!database.isOpen())
            open();
        Cursor cursor = database.rawQuery(
                "SELECT" +
                        " times.*," + " tags.*" +
                        " FROM " + MySQLiteHelper.TABLE_TIMES + " times" +
                        " INNER JOIN " + MySQLiteHelper.TABLE_TAGS + " tags" +
                        " ON times." + MySQLiteHelper.COLUMN_TAG_ID + " = tags."
                        + MySQLiteHelper.COLUMN_TAG_ID +
                        " WHERE times." + MySQLiteHelper.COLUMN_TAG_ID + " = " +
                        tag.getId(), null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Item item = cursorToItem(cursor);
            deleteItem(item.getId());
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        database.delete(MySQLiteHelper.TABLE_TAGS, MySQLiteHelper.COLUMN_TAG + "=?", new String[]{Long.toString(tag.getId())});
        if (database.isOpen())
            close();
    }

    public void deleteItem(long id) {
        if (!database.isOpen())
            open();
        database.delete(MySQLiteHelper.TABLE_TIMES,
                MySQLiteHelper.COLUMN_ITEM_ID + " =?", new String[]{
                        Long.toString(id)});
        if (database.isOpen())
            close();
    }

    public void updateItem(Item item) {
        if (!database.isOpen())
            open();
        ContentValues args = new ContentValues();
        args.put(MySQLiteHelper.COLUMN_ITEM_DESCRIPTION, item.getDescription());
        args.put(MySQLiteHelper.COLUMN_TAG_ID, item.getTag().getId());
        database.update(MySQLiteHelper.TABLE_TIMES, args,
                MySQLiteHelper.COLUMN_ITEM_ID + "=?", new String[]{Long.toString(item.getId())
                });
        if (database.isOpen())
            close();
    }

    public Cursor getAllItems() {
        if (!database.isOpen())
            open();

        Cursor cursor = database.rawQuery(
                "SELECT " +
                        MySQLiteHelper.COLUMN_ITEM_ID + " AS _id, " +
                        MySQLiteHelper.COLUMN_ITEM_DESCRIPTION +
                        " FROM " + MySQLiteHelper.TABLE_TIMES, null);


        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (database.isOpen())
            close();

        return cursor;
    }

    public Cursor getAllItemsCursor() {
        if (!database.isOpen())
            open();

        Cursor cursor = database.rawQuery(
                "SELECT" +
                        " times." + MySQLiteHelper.COLUMN_ITEM_ID + " AS _id," +
                        " times." + MySQLiteHelper.COLUMN_ITEM_DESCRIPTION + "," +
                        " tags." + MySQLiteHelper.COLUMN_TAG +
                        " FROM " + MySQLiteHelper.TABLE_TIMES + " times" +
                        " INNER JOIN " + MySQLiteHelper.TABLE_TAGS + " tags" +
                        " ON times." + MySQLiteHelper.COLUMN_TAG_ID + " = tags." +
                        MySQLiteHelper.COLUMN_TAG_ID, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (database.isOpen())
            close();

        return cursor;
    }

    public Cursor getAllTagsCursor() {
        if (!database.isOpen())
            open();

        Cursor cursor = database.rawQuery(
                "SELECT " +
                        MySQLiteHelper.COLUMN_TAG_ID + " AS _id," +
                        MySQLiteHelper.COLUMN_TAG +
                        " FROM " + MySQLiteHelper.TABLE_TAGS, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        if (database.isOpen())
            close();
        return cursor;
    }

    public Item getItem(long columnId) {
        if (!database.isOpen())
            open();

        Cursor cursor = database.rawQuery(
                "SELECT times.*, tags.*" +
                        " FROM " + MySQLiteHelper.TABLE_TIMES + " times" +
                        " INNER JOIN " + MySQLiteHelper.TABLE_TAGS + " tags" +
                        " ON times." + MySQLiteHelper.COLUMN_TAG_ID + " = tags." +
                        MySQLiteHelper.COLUMN_TAG_ID +
                        " WHERE times." + MySQLiteHelper.COLUMN_ITEM_ID + " = " +
                        columnId, null);

        cursor.moveToFirst();
        Item item = cursorToItem(cursor);
        cursor.close();
        if (database.isOpen())
            close();
        return item;
    }

    private Item cursorToItem(Cursor cursor) {
        try {
            Item item = new Item();
            Tag tag = new Tag();

            item.setId(cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_ITEM_ID)));
            item.setItem(cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_ITEM_DESCRIPTION)));
            tag.setId(cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_TAG_ID)));
            tag.setTag(cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_TAG)));
            item.setTag(tag);
            return item;
        } catch (CursorIndexOutOfBoundsException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
