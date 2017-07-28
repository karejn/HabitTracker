package com.example.ekzhu.habittracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ekzhu.habittracker.data.NovelContract.NovelEntry;
import com.example.ekzhu.habittracker.data.NovelDbHelper;

/**
 * Created by ekzhu on 15.07.2017.
 */

public class CatalogActivity extends AppCompatActivity{
    /** Database helper that will provide us access to the database */
    private NovelDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new NovelDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                NovelEntry._ID,
                NovelEntry.COLUMN_NOVEL_NAME,
                NovelEntry.COLUMN_NOVEL_GENRE,
                NovelEntry.COLUMN_READING_MODE,
                NovelEntry.COLUMN_TIME };

        // Perform a query on the novels table
        Cursor cursor = db.query(
                NovelEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        TextView displayView = (TextView) findViewById(R.id.text_view_novel);

        try {
            // Create a header in the Text View that looks like this:
            //
            // The novels table contains <number of rows in Cursor> novels.
            // _id - name - genre - reading mode - time
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            displayView.setText("The novels table contains " + cursor.getCount() + " novels.\n\n");
            displayView.append(NovelEntry._ID + " - " +
                    NovelEntry.COLUMN_NOVEL_NAME + " - " +
                    NovelEntry.COLUMN_NOVEL_GENRE + " - " +
                    NovelEntry.COLUMN_READING_MODE + " - " +
                    NovelEntry.COLUMN_TIME + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(NovelEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(NovelEntry.COLUMN_NOVEL_NAME);
            int genreColumnIndex = cursor.getColumnIndex(NovelEntry.COLUMN_NOVEL_GENRE);
            int modeColumnIndex = cursor.getColumnIndex(NovelEntry.COLUMN_READING_MODE);
            int durationColumnIndex = cursor.getColumnIndex(NovelEntry.COLUMN_TIME);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentGenre = cursor.getString(genreColumnIndex);
                int currentMode = cursor.getInt(modeColumnIndex);
                int currentDuration= cursor.getInt(durationColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentGenre + " - " +
                        currentMode + " - " +
                        currentDuration));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    /**
     * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
     */
    private void insertNovel() {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NovelEntry.COLUMN_NOVEL_NAME, "Gone with the wind");
        values.put(NovelEntry.COLUMN_NOVEL_GENRE, "roman");
        values.put(NovelEntry.COLUMN_READING_MODE, NovelEntry.MODE_PHYSICAL);
        values.put(NovelEntry.COLUMN_TIME, 45);

        long newRowId = db.insert(NovelEntry.TABLE_NAME, null, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertNovel();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
