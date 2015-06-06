package com.example.android.googlejamfinalproject;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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

/**
 * Created by John on 5/16/2015.
 */
public class DetailMapFragment extends Fragment implements OnMapReadyCallback {

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

    public DetailMapFragment() {
        // Required empty public constructor
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

            Bundle bundle = getArguments();

            long id;

            if (bundle == null) {
                id = -1;
            } else {
                id = getArguments().getLong("id", -1L);
            }

            new PopulateFragmentTask().execute(id);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        myMap = map;
//        map.addMarker(new MarkerOptions()
//                .position(new LatLng(lat, lon))
//                .title(location));
//
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 14));
    }

    private class PopulateFragmentTask extends AsyncTask<Long, Void, Boolean> {

        String category = "0";
        String dateTime = "0:00 AM";
        Float lat = 0.0f;
        Float lon = 0.0f;
        String location = "Center of World";



        @Override
        protected Boolean doInBackground(Long[] params) {

            Long id = params[0];

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
                category = cursor.getString(8);
                dateTime = cursor.getString(1);
                location = cursor.getString(7);
                lat = cursor.getFloat(5);
                lon = cursor.getFloat(6);

                switch (category) {
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

                tvDateTime.setText(dateTime);

                tvCategory.setText(category);
                tvCategory.setVisibility(View.VISIBLE);

                tv3.setText(location);
                tv3.setVisibility(View.VISIBLE);

                tv4.setText(String.valueOf(lat) + "," + String.valueOf(lon));
                tv4.setVisibility(View.VISIBLE);

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
