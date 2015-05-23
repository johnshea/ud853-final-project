package com.example.android.googlejamfinalproject;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.googlejamfinalproject.contentprovider.MyEventProvider;
import com.example.android.googlejamfinalproject.data.EventContract;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlertListFragment extends Fragment implements LoaderCallbacks<Cursor> {

    OnAlertSelectedListener mCallback;

    public interface OnAlertSelectedListener {
        public void onAlertSelected(long id);
        public void onDefaultSelected(long id);
        public boolean isDualPane();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnAlertSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnAlertSelectedListener");
        }
    }

    SimpleCursorAdapter mCursorAdapter;
    ListView mListView;

    public AlertListFragment() {
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
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(0, null, this);

        String[] mColumns = {EventContract.EventEntry._ID,
                EventContract.EventEntry.COLUMN_NAME_DATE,
                EventContract.EventEntry.COLUMN_NAME_MAKE,
                EventContract.EventEntry.COLUMN_NAME_MODEL,
                EventContract.EventEntry.COLUMN_NAME_COLOR,
                EventContract.EventEntry.COLUMN_NAME_EVENT_ID
        };

        int[] mListItems = { R.id.textView_provider_id,
                R.id.textView_provider_date,
                R.id.textView_provider_make,
                R.id.textView_provider_model,
                R.id.textView_provider_color,
                R.id.textView_provider_event_id
        };

        mCursorAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.provider_row,
                null,
                mColumns,
                mListItems,
                0
        );

        mCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                switch( columnIndex ) {
                    case 1:
                        TextView tv = (TextView) view;
                        String text = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_NAME_DATE));
                        int index = text.lastIndexOf(":");
                        String result = text.substring(0, index);
                        tv.setText(result);
                        return true;

                    case 5:
                        TextView tvColor = (TextView) view;

                        String eventId = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.COLUMN_NAME_EVENT_ID));
                        if (eventId == null) {
                            eventId = "3";
                        }
                        switch( eventId ) {
                            case "0":
                                tvColor.setTextColor(Color.DKGRAY);
                                tvColor.setText("Nice!");
                                break;
                            case "1":
                                tvColor.setTextColor(Color.RED);
                                tvColor.setText("Drunk");
                                break;
                            case "2":
                                tvColor.setTextColor(Color.rgb(0xFF, 0x99, 0x00));
                                tvColor.setText("Aggressive");
                                break;
                            default:
                                tvColor.setTextColor(Color.BLACK);
                                tvColor.setText("Safety");
                        }
                        return true;

                }

                return false;
            }
        });

        mListView = (ListView) getView().findViewById(R.id.listView_list);

        mListView.setAdapter(mCursorAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallback.onAlertSelected(id);
            }
        });

    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] mProjection = {EventContract.EventEntry._ID,
                EventContract.EventEntry.COLUMN_NAME_DATE,
                EventContract.EventEntry.COLUMN_NAME_MAKE,
                EventContract.EventEntry.COLUMN_NAME_MODEL,
                EventContract.EventEntry.COLUMN_NAME_COLOR,
                EventContract.EventEntry.COLUMN_NAME_EVENT_ID
        };

        String mSelectionClause = null;

        String[] mSelectionArgs = {""};

        String mSortOrder = EventContract.EventEntry._ID + " DESC";

        return new CursorLoader(getActivity(), MyEventProvider.CONTENT_URI,
                mProjection, mSelectionClause, mSelectionArgs, mSortOrder);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);

        if (data != null && data.getCount() > 0) {
            if (mCallback.isDualPane()) {
                mCallback.onDefaultSelected(0);
            }
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader loader) {
        mCursorAdapter.swapCursor(null);
    }

}
