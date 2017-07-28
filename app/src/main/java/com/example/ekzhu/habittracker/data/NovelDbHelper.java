package com.example.ekzhu.habittracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ekzhu.habittracker.data.NovelContract.NovelEntry;

/**
 * Created by ekzhu on 15.07.2017.
 */

public class NovelDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = NovelDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "library.db";

    private static final int DATABASE_VERSION = 1;

    public NovelDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the novels table
        String SQL_CREATE_NOVELS_TABLE = "CREATE TABLE " + NovelEntry.TABLE_NAME + " ("
                + NovelEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NovelEntry.COLUMN_NOVEL_NAME + " TEXT NOT NULL, "
                + NovelEntry.COLUMN_NOVEL_GENRE + " TEXT, "
                + NovelEntry.COLUMN_READING_MODE + " INTEGER NOT NULL, "
                + NovelEntry.COLUMN_TIME + " INTEGER NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_NOVELS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
