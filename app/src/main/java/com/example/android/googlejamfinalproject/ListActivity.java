package com.example.android.googlejamfinalproject;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.android.googlejamfinalproject.contentprovider.MyEventProvider;
import com.example.android.googlejamfinalproject.data.EventContract;


public class ListActivity extends ActionBarActivity {

    SimpleCursorAdapter mCursorAdapter;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        String[] mProjection = {EventContract.EventEntry._ID,
                EventContract.EventEntry.COLUMN_NAME_DATE,
                EventContract.EventEntry.COLUMN_NAME_MAKE,
                EventContract.EventEntry.COLUMN_NAME_MODEL,
                EventContract.EventEntry.COLUMN_NAME_COLOR
        };

        String mSelectionClause = null;

        String[] mSelectionArgs = {""};

        String mSortOrder = EventContract.EventEntry._ID + " DESC";

        Cursor cursor = getContentResolver().query(
                MyEventProvider.CONTENT_URI,
                mProjection,
                mSelectionClause,
                mSelectionArgs,
                mSortOrder
        );

        String[] mColumns = {EventContract.EventEntry._ID,
                EventContract.EventEntry.COLUMN_NAME_DATE,
                EventContract.EventEntry.COLUMN_NAME_MAKE,
                EventContract.EventEntry.COLUMN_NAME_MODEL,
                EventContract.EventEntry.COLUMN_NAME_COLOR
        };

        int[] mListItems = { R.id.textView_provider_id,
                R.id.textView_provider_date,
                R.id.textView_provider_make,
                R.id.textView_provider_model,
                R.id.textView_provider_color
        };

        mCursorAdapter = new SimpleCursorAdapter(
                getApplicationContext(),
                R.layout.provider_row,
                cursor,
                mColumns,
                mListItems,
                0
        );

        mListView = (ListView) findViewById(R.id.listView);

        mListView.setAdapter(mCursorAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
