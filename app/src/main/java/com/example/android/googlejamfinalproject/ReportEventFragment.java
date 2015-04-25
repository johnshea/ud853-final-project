package com.example.android.googlejamfinalproject;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.googlejamfinalproject.data.EventContract;
import com.example.android.googlejamfinalproject.data.EventOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by John on 4/12/2015.
 */

public class ReportEventFragment extends Fragment {

    Button btnSubmit;
    Button btnCancel;
    Spinner eventSpinner;
    Spinner colorSpinner;
    Spinner makeSpinner;
    Spinner modelSpinner;

    EditText editText_event;
    EditText editText_color;
    AutoCompleteTextView editText_make;
    EditText editText_model;
    EditText editText_plate;
    EditText editText_state;
    EditText editText_misc;
    EditText editText_time;
    EditText editText_latlon;

    public ReportEventFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem menuItem = menu.findItem(R.id.action_create_new_alert);
        menuItem.setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_report_event, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        eventSpinner = (Spinner) getView().findViewById(R.id.spinner_report_event);
        ArrayAdapter<CharSequence> eventAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.events_array, android.R.layout.simple_spinner_item);
        eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSpinner.setAdapter(eventAdapter);

        colorSpinner = (Spinner) getView().findViewById(R.id.spinner_report_color);
        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.colors_array, android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(colorAdapter);

        makeSpinner = (Spinner) getView().findViewById(R.id.spinner_report_make);
        ArrayAdapter<CharSequence> makeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.makes_array, android.R.layout.simple_spinner_item);
        makeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        makeSpinner.setAdapter(makeAdapter);

        modelSpinner = (Spinner) getView().findViewById(R.id.spinner_report_model);
        ArrayAdapter<CharSequence> modelAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.models_array, android.R.layout.simple_spinner_item);
        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(modelAdapter);

        btnSubmit = (Button) getView().findViewById(R.id.buttonSubmit);
        btnCancel = (Button) getView().findViewById(R.id.buttonCancel);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] events = getResources().getStringArray(R.array.events_array);
                int eventId = eventSpinner.getSelectedItemPosition();

//                editText_event = (EditText) getView().findViewById(R.id.editText_report_event);
//                editText_color = (EditText) getView().findViewById(R.id.editText_report_color);
//                editText_make = (AutoCompleteTextView) getView().findViewById(R.id.editText_report_make);
//                editText_model = (EditText) getView().findViewById(R.id.editText_report_model);
//                editText_plate = (EditText) getView().findViewById(R.id.editText_report_plate);
                editText_state = (EditText) getView().findViewById(R.id.editText_report_state);
//                editText_misc = (EditText) getView().findViewById(R.id.editText_report_misc);
//                editText_time = (EditText) getView().findViewById(R.id.editText_report_time);
//                editText_latlon = (EditText) getView().findViewById(R.id.editText_report_latlon);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSZ", Locale.US);

                String dateStamp = sdf.format(new Date());

                EventOpenHelper mDbHelper = new EventOpenHelper(getActivity());

                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(EventContract.EventEntry.COLUMN_NAME_DATE, dateStamp);
                values.put(EventContract.EventEntry.COLUMN_NAME_EVENT_ID, String.valueOf(eventId));
                values.put(EventContract.EventEntry.COLUMN_NAME_CATEGORY, String.valueOf(eventId));
                values.put(EventContract.EventEntry.COLUMN_NAME_MAKE, makeSpinner.getSelectedItem().toString());
                values.put(EventContract.EventEntry.COLUMN_NAME_MODEL, modelSpinner.getSelectedItem().toString());
                values.put(EventContract.EventEntry.COLUMN_NAME_COLOR, colorSpinner.getSelectedItem().toString());
                values.put(EventContract.EventEntry.COLUMN_NAME_LAT, 41.742685);
                values.put(EventContract.EventEntry.COLUMN_NAME_LON, -74.084215);
                values.put(EventContract.EventEntry.COLUMN_NAME_LOCATION, "New Paltz, NY");

                long newRowId;
                newRowId = db.insert(
                        EventContract.EventEntry.TABLE_NAME,
                        "null",
                        values);

//                Toast.makeText(getActivity(), "Inserted: " + newRowId, Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), "Alert submitted", Toast.LENGTH_LONG).show();

                db.close();
                mDbHelper.close();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                EventOpenHelper eventOpenHelper = new EventOpenHelper(getActivity());
//                SQLiteDatabase db = eventOpenHelper.getReadableDatabase();
//
//                String[] projection = {
//                        EventContract.EventEntry._ID,
//                        EventContract.EventEntry.COLUMN_NAME_DATE,
//                        EventContract.EventEntry.COLUMN_NAME_MAKE,
//                        EventContract.EventEntry.COLUMN_NAME_MODEL,
//                        EventContract.EventEntry.COLUMN_NAME_COLOR
//                };
//
//                String sortOrder = EventContract.EventEntry.COLUMN_NAME_COLOR + " DESC";
//
//                Cursor c = db.query(
//                        EventContract.EventEntry.TABLE_NAME,
//                        projection,
//                        null,
//                        null,
//                        null,
//                        null,
//                        sortOrder
//                );
//
//                Toast.makeText(getActivity(), "Rows in db: " + c.getCount(), Toast.LENGTH_SHORT).show();
//
//                c.close();
//                db.close();
//                eventOpenHelper.close();
            }
        });

    }
}
