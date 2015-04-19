package com.example.android.googlejamfinalproject;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.googlejamfinalproject.contentprovider.MyEventProvider;
import com.example.android.googlejamfinalproject.data.EventContract;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends ActionBarActivity
        implements OnMapReadyCallback {

    Float lat = 0.0f;
    Float lon = 0.0f;
    String location = "Center of World";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_detail);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        try {
            Intent intent = getIntent();
            long id = intent.getLongExtra("id", -1L);
            if (id != -1) {
                String[] mProjection = {EventContract.EventEntry._ID,
                        EventContract.EventEntry.COLUMN_NAME_DATE,
                        EventContract.EventEntry.COLUMN_NAME_MAKE,
                        EventContract.EventEntry.COLUMN_NAME_MODEL,
                        EventContract.EventEntry.COLUMN_NAME_COLOR,
                        EventContract.EventEntry.COLUMN_NAME_LAT,
                        EventContract.EventEntry.COLUMN_NAME_LON,
                        EventContract.EventEntry.COLUMN_NAME_LOCATION,
                        EventContract.EventEntry.COLUMN_NAME_EVENT_ID
                };

                String mSelectionClause = "_id = ?";

                String[] mSelectionArgs = {String.valueOf(id)};

                String mSortOrder = EventContract.EventEntry._ID + " DESC";

                Uri uri = Uri.withAppendedPath(MyEventProvider.CONTENT_URI, String.valueOf(id));

                Cursor cursor = getContentResolver().query(
                        uri,
                        mProjection,
                        mSelectionClause,
                        mSelectionArgs,
                        mSortOrder
                );

                if (cursor.moveToFirst()) {
                    location = cursor.getString(7);
                    lat = cursor.getFloat(5);
                    lon = cursor.getFloat(6);

                    TextView tv = (TextView)findViewById(R.id.textView3);
                    tv.setText(location);

                    tv = (TextView)findViewById(R.id.textView4);
                    tv.setText(String.valueOf(lat) + "," + String.valueOf(lon));

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title(location));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 14));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_map, menu);
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
