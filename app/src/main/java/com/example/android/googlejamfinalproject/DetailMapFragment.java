package com.example.android.googlejamfinalproject;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.googlejamfinalproject.contentprovider.MyEventProvider;
import com.example.android.googlejamfinalproject.data.EventContract;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by John on 5/16/2015.
 */
public class DetailMapFragment extends Fragment implements OnMapReadyCallback {

    private ShareActionProvider mShareActionProvider;

    String category = "0";
    String dateTime = "0:00 AM";
    Float lat = 0.0f;
    Float lon = 0.0f;
    String location = "Center of World";

    TextView tvDateTime;
    TextView tvCategory;
    TextView tv3;
    TextView tv4;
    View mapView;

    GoogleMap myMap;

    SupportMapFragment mapFragment;

    long id;

    public DetailMapFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        try {

            if (savedInstanceState == null) {
                Bundle bundle = getArguments();

                if (bundle == null) {
                    id = -1;
                } else {
                    id = getArguments().getLong("id", -1L);
                }
            } else {
                id = savedInstanceState.getLong("id", -1L);
            }

            new PopulateFragmentTask().execute(id);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong("id", id);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail_map_fragment, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menu_id = item.getItemId();

        if (menu_id == R.id.menu_item_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            if (id < 0) {
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out this new app - AutoCorrect");
            } else {
                TextView share_tvDateTime = (TextView) getView().findViewById(R.id.textView2);
                TextView share_tvCategory = (TextView) getView().findViewById(R.id.textView_map_category);
                TextView share_tvLocation = (TextView) getView().findViewById(R.id.textView3);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "In " + share_tvLocation.getText().toString() + ", I just saw a " + share_tvCategory.getText().toString()
                                + " at " + share_tvDateTime.getText().toString() + ".");
            }
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        myMap = map;
    }

    private class PopulateFragmentTask extends AsyncTask<Long, Void, Boolean> {

        String category = "0";
        String dateTime = "0:00 AM";
        Float lat = 0.0f;
        Float lon = 0.0f;
        String location = "Center of World";

        @Override
        protected Boolean doInBackground(Long[] params) {

            id = params[0];

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

            String mSelectionClause;
            String[] mSelectionArgs;
            Uri uri;

            if (id != -1) {
                mSelectionClause = "_id = ?";
                mSelectionArgs = new String[1];
                mSelectionArgs[0] = String.valueOf(id);
                uri = Uri.withAppendedPath(MyEventProvider.CONTENT_URI, String.valueOf(id));
            } else {
                mSelectionClause = null;
                mSelectionArgs = null;
                uri = MyEventProvider.CONTENT_URI;
            }

            String mSortOrder = EventContract.EventEntry._ID + " DESC";

            Cursor cursor = getActivity().getContentResolver().query(
                    uri,
                    mProjection,
                    mSelectionClause,
                    mSelectionArgs,
                    mSortOrder
            );

            if (cursor.moveToFirst()) {

                id = cursor.getLong(0);
                dateTime = cursor.getString(1);
                location = cursor.getString(7);
                lat = cursor.getFloat(5);
                lon = cursor.getFloat(6);

                switch (cursor.getString(8)) {
                    case "0":
                        category = "Nice!";
                        break;
                    case "1":
                        category = "Drunk Driver";
                        break;
                    case "2":
                        category = "Aggressive Driver";
                        break;
                    default:
                        category = "Safety Issue";
                }

                return true;

            } else {

                return false;
            }

        }

        @Override
        protected void onPreExecute() {
            tvDateTime = (TextView) getView().findViewById(R.id.textView2);
            tvCategory = (TextView) getView().findViewById(R.id.textView_map_category);
            tv3 = (TextView) getView().findViewById(R.id.textView3);
            tv4 = (TextView) getView().findViewById(R.id.textView4);
            mapView = getView().findViewById(R.id.map);
        }

        @Override
        protected void onPostExecute(Boolean isFound) {
            if (isFound) {

                try {

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
                    Date date = simpleDateFormat.parse(dateTime);

                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy MMMM dd hh:mm a");
                    tvDateTime.setText(simpleDateFormat1.format(date));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                tvCategory.setText(category);
                tvCategory.setVisibility(View.VISIBLE);

                tv3.setText(location);
                tv3.setVisibility(View.VISIBLE);

//                tv4.setText(String.valueOf(lat) + "," + String.valueOf(lon));
//                tv4.setVisibility(View.VISIBLE);
                tv4.setVisibility(View.INVISIBLE);

                mapView.setVisibility(View.VISIBLE);

                if (myMap != null) {
                    myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(lat, lon))
                            .title(location));

                    myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 14));
                }

            } else {

                tvDateTime.setText("No alerts.");

                tvCategory.setVisibility(View.INVISIBLE);

                tv3.setVisibility(View.INVISIBLE);

                tv4.setVisibility(View.INVISIBLE);

                mapView.setVisibility(View.INVISIBLE);

            }
        }
    }

}
