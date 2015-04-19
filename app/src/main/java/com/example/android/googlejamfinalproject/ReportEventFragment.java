package com.example.android.googlejamfinalproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.googlejamfinalproject.data.EventContract;
import com.example.android.googlejamfinalproject.data.EventOpenHelper;

/**
 * Created by John on 4/12/2015.
 */

public class ReportEventFragment extends Fragment {

    Button btnSubmit;
    Button btnCancel;
    Button btnListEvents;
    //Button btnShowDetailMap;

    EditText editText_event;
    EditText editText_color;
    EditText editText_make;
    EditText editText_model;
    EditText editText_plate;
    EditText editText_state;
    EditText editText_misc;
    EditText editText_time;
    EditText editText_latlon;

    public ReportEventFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnSubmit = (Button) getView().findViewById(R.id.buttonSubmit);
        btnCancel = (Button) getView().findViewById(R.id.buttonCancel);
        btnListEvents = (Button) getView().findViewById(R.id.buttonList);
        //btnShowDetailMap = (Button) getView().findViewById(R.id.buttonMap);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editText_event = (EditText) getView().findViewById(R.id.editText_report_event);
                editText_color = (EditText) getView().findViewById(R.id.editText_report_color);
                editText_make = (EditText) getView().findViewById(R.id.editText_report_make);
                editText_model = (EditText) getView().findViewById(R.id.editText_report_model);
                editText_plate = (EditText) getView().findViewById(R.id.editText_report_plate);
                editText_state = (EditText) getView().findViewById(R.id.editText_report_state);
                editText_misc = (EditText) getView().findViewById(R.id.editText_report_misc);
                editText_time = (EditText) getView().findViewById(R.id.editText_report_time);
                editText_latlon = (EditText) getView().findViewById(R.id.editText_report_latlon);

                EventOpenHelper mDbHelper = new EventOpenHelper(getActivity());

                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(EventContract.EventEntry.COLUMN_NAME_DATE, editText_time.getText().toString());
                values.put(EventContract.EventEntry.COLUMN_NAME_CATEGORY, editText_event.getText().toString());
                values.put(EventContract.EventEntry.COLUMN_NAME_MAKE, editText_make.getText().toString());
                values.put(EventContract.EventEntry.COLUMN_NAME_MODEL, editText_model.getText().toString());
                values.put(EventContract.EventEntry.COLUMN_NAME_COLOR, editText_color.getText().toString());

                long newRowId;
                newRowId = db.insert(
                        EventContract.EventEntry.TABLE_NAME,
                        "null",
                        values);

                Toast.makeText(getActivity(), "Inserted: " + newRowId, Toast.LENGTH_LONG).show();

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

        btnListEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ListActivity.class);
                startActivity(intent);
            }
        });

        /*
        btnShowDetailMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MapActivity.class);
                startActivity(intent);
            }
        });
        */
    }
}
