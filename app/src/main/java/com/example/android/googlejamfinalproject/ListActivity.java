package com.example.android.googlejamfinalproject;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.android.googlejamfinalproject.contentprovider.MyEventProvider;
import com.example.android.googlejamfinalproject.data.EventContract;


public class ListActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    SimpleCursorAdapter mCursorAdapter;
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        getLoaderManager().initLoader(0, null, this);

//        String[] mProjection = {EventContract.EventEntry._ID,
//                EventContract.EventEntry.COLUMN_NAME_DATE,
//                EventContract.EventEntry.COLUMN_NAME_MAKE,
//                EventContract.EventEntry.COLUMN_NAME_MODEL,
//                EventContract.EventEntry.COLUMN_NAME_COLOR
//        };
//
//        String mSelectionClause = null;
//
//        String[] mSelectionArgs = {""};
//
//        String mSortOrder = EventContract.EventEntry._ID + " DESC";
//
//        Cursor cursor = getContentResolver().query(
//                MyEventProvider.CONTENT_URI,
//                mProjection,
//                mSelectionClause,
//                mSelectionArgs,
//                mSortOrder
//        );

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
                null,
                mColumns,
                mListItems,
                0
        );

        mCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if(columnIndex==1) {
                    TextView tv = (TextView) view;

                    String carColor = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_NAME_COLOR));
                    switch(carColor) {
                        case "Blue":
                            tv.setTextColor(Color.BLUE);
                            break;
                        case "Red":
                            tv.setTextColor(Color.RED);
                            break;
                        case "Green":
                            tv.setTextColor(Color.GREEN);
                            break;
                        case "Yellow":
                            tv.setTextColor(Color.YELLOW);
                            break;
                        case "White":
                            tv.setTextColor(Color.WHITE);
                            break;
                        default:
                            tv.setTextColor(Color.BLACK);
                    }
                    String text = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_NAME_DATE));
                    int index = text.lastIndexOf(":");
                    String result = text.substring(0, index);
                    tv.setText(result);
                    return true;
                }
                return false;
            }
        });

        mListView = (ListView) findViewById(R.id.listView);

        mListView.setAdapter(mCursorAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), "id: " + String.valueOf(id), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MapActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        return null;
        String[] mProjection = {EventContract.EventEntry._ID,
                EventContract.EventEntry.COLUMN_NAME_DATE,
                EventContract.EventEntry.COLUMN_NAME_MAKE,
                EventContract.EventEntry.COLUMN_NAME_MODEL,
                EventContract.EventEntry.COLUMN_NAME_COLOR
        };

        String mSelectionClause = null;

        String[] mSelectionArgs = {""};

        String mSortOrder = EventContract.EventEntry._ID + " DESC";

        return new CursorLoader(getApplicationContext(), MyEventProvider.CONTENT_URI,
                mProjection, mSelectionClause, mSelectionArgs, mSortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mCursorAdapter.swapCursor(null);
    }
}
