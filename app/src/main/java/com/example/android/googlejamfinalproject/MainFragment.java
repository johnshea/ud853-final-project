package com.example.android.googlejamfinalproject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.android.googlejamfinalproject.contentprovider.MyEventProvider;
import com.example.android.googlejamfinalproject.data.EventContract;
import com.example.android.googlejamfinalproject.services.CreateAlertService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainFragment extends Fragment {

    Button btnListCurrentAlerts;
    Button btnCreateNewAlert;
    Button btnStartService;
    Button btnStopService;

    public MainFragment() {
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
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnListCurrentAlerts = (Button) getView().findViewById(R.id.button_mainfragment_listcurrentalerts);
        btnCreateNewAlert = (Button) getView().findViewById(R.id.button_mainfragment_createnewalert);
        btnStartService = (Button) getView().findViewById(R.id.button_mainfragment_startservice);
        btnStopService =  (Button) getView().findViewById(R.id.button_mainfragment_stopservice);

        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateAlertService.class);
                getActivity().startService(intent);


            }
        });

        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateAlertService.class);
                getActivity().stopService(intent);
            }
        });

        btnListCurrentAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ListActivity.class);
                startActivity(intent);
            }
        });

        btnCreateNewAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.container, new ReportEventFragment())
//                        .addToBackStack(null)
//                        .commit();

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSZ", Locale.US);

                String dateStamp = sdf.format(new Date());

                ContentValues values = new ContentValues();

                values.put(EventContract.EventEntry.COLUMN_NAME_DATE, dateStamp);
                values.put(EventContract.EventEntry.COLUMN_NAME_EVENT_ID, "0");
                values.put(EventContract.EventEntry.COLUMN_NAME_MAKE, "Ferrari");
                values.put(EventContract.EventEntry.COLUMN_NAME_MODEL, "488GTB");
                values.put(EventContract.EventEntry.COLUMN_NAME_COLOR, "Red");
                values.put(EventContract.EventEntry.COLUMN_NAME_LOCATION, "New York, NY");
                values.put(EventContract.EventEntry.COLUMN_NAME_LAT, 40.752400);
                values.put(EventContract.EventEntry.COLUMN_NAME_LON, -73.978381);

                Uri mNewUri;

                mNewUri = getActivity().getContentResolver().insert(MyEventProvider.CONTENT_URI, values);

                Intent resultIntent = new Intent(getActivity(), MapActivity.class);
                resultIntent.putExtra("id", Long.valueOf(mNewUri.getLastPathSegment()));

                TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getActivity());
                taskStackBuilder.addParentStack(MapActivity.class);
                taskStackBuilder.addNextIntent(resultIntent);

                PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.drawable.ic_plusone_small_off_client)
                        .setContentTitle("Nice car!")
                        .setContentText("Red ferrari spotted");

                mBuilder.setContentIntent(resultPendingIntent);

                NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(1, mBuilder.build());
            }
        });

    }
}
