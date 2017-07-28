package com.example.ekzhu.habittracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ekzhu.habittracker.data.NovelContract.NovelEntry;
import com.example.ekzhu.habittracker.data.NovelDbHelper;

/**
 * Created by ekzhu on 15.07.2017.
 */

public class EditorActivity extends AppCompatActivity{

    private EditText mNameEditText;
    private EditText mGenreEditText;
    private EditText mDurationEditText;
    private Spinner mModeSpinner;

    private int mMode = NovelEntry.MODE_NA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_novel_name);
        mGenreEditText = (EditText) findViewById(R.id.edit_novel_genre);
        mDurationEditText = (EditText) findViewById(R.id.edit_duration);
        mModeSpinner = (Spinner) findViewById(R.id.spinner_mode);

        setupSpinner();
    }

    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_reading_mode_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mModeSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.mode_physical))) {
                        mMode = NovelEntry.MODE_PHYSICAL;
                    } else if (selection.equals(getString(R.string.mode_digital))) {
                        mMode = NovelEntry.MODE_DIGITAL;
                    } else {
                        mMode = NovelEntry.MODE_NA;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mMode = NovelEntry.MODE_NA;
            }
        });
    }

    /**
     * Get user input from editor and save new novel into database.
     */
    private void insertNovel() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String genreString = mGenreEditText.getText().toString().trim();
        String durationString = mDurationEditText.getText().toString().trim();
        int duration = Integer.parseInt(durationString);

        // Create database helper
        NovelDbHelper mDbHelper = new NovelDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and novels attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(NovelEntry.COLUMN_NOVEL_NAME, nameString);
        values.put(NovelEntry.COLUMN_NOVEL_GENRE, genreString);
        values.put(NovelEntry.COLUMN_READING_MODE, mMode);
        values.put(NovelEntry.COLUMN_TIME, duration);

        // Insert a new row for novel in the database, returning the ID of that new row.
        long newRowId = db.insert(NovelEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving novel", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Novel saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save novel to database
                insertNovel();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
