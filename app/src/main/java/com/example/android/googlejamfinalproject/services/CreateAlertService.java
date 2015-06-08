package com.example.android.googlejamfinalproject.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.android.googlejamfinalproject.MapActivity;
import com.example.android.googlejamfinalproject.R;
import com.example.android.googlejamfinalproject.contentprovider.MyEventProvider;
import com.example.android.googlejamfinalproject.data.EventContract;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by John on 4/22/2015.
 */
public class CreateAlertService extends Service {

    private Handler mHandler;
    private Boolean blnKeepCreating = true;
    Random rnd = new Random();

    int count = 0;

    public class MyWaitRunnable implements Runnable {

        @Override
        public void run() {
            try {
                wait(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class MyToastRunnable implements Runnable {

        private String mText;

        public MyToastRunnable(String text) {
            mText = text;
        }

        @Override
        public void run() {
//            Toast.makeText(getApplicationContext(), mText, Toast.LENGTH_LONG).show();
        }
    }

    public class MyRunnable implements Runnable {

        @Override
        public void run() {

//            mHandler.post(new MyToastRunnable("Create alert - sleeping for 5 seconds"));
            while( blnKeepCreating ) {

                count++;

                if (count == 5) {

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSZ", Locale.US);

                    String dateStamp = sdf.format(new Date());

                    ContentValues values = new ContentValues();

                    values.put(EventContract.EventEntry.COLUMN_NAME_DATE, dateStamp);
                    values.put(EventContract.EventEntry.COLUMN_NAME_EVENT_ID, "0");
                    values.put(EventContract.EventEntry.COLUMN_NAME_MAKE, "Lamborghini");
                    values.put(EventContract.EventEntry.COLUMN_NAME_MODEL, "Huracán");
                    values.put(EventContract.EventEntry.COLUMN_NAME_COLOR, "Green");
                    values.put(EventContract.EventEntry.COLUMN_NAME_LOCATION, "New York, NY");
                    values.put(EventContract.EventEntry.COLUMN_NAME_LAT, 40.752400);
                    values.put(EventContract.EventEntry.COLUMN_NAME_LON, -73.978381);

                    Uri mNewUri;

                    mNewUri = getContentResolver().insert(MyEventProvider.CONTENT_URI, values);

                    Intent resultIntent = new Intent(getApplicationContext(), MapActivity.class);
                    resultIntent.putExtra("id", Long.valueOf(mNewUri.getLastPathSegment()));

                    TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(getApplicationContext());
                    taskStackBuilder.addParentStack(MapActivity.class);
                    taskStackBuilder.addNextIntent(resultIntent);

                    PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );


                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.drawable.ic_car)
                            .setContentTitle("Nice car!")
                            .setContentText("Green Lamborghini spotted");

                    mBuilder.setContentIntent(resultPendingIntent);

                    NotificationManager notificationManager = (NotificationManager)
                            getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                    notificationManager.notify(1, mBuilder.build());

                    count=0;
                }

                try {
                    int randomInt = rnd.nextInt(5) + 5;

                    Thread.sleep(randomInt * 1000);
//                    Thread.sleep(1000);

                    String[] category = {"1", "2", "3"};

                    String[] colors = {"Blue", "Green", "Red", "White", "Black", "Yellow", "Brown"};

                    String[] cars = {"Honda Accord", "Honda Civic", "Honda Pilot",
                            "Toyota Camry", "Toyota Corolla",
                            "Subaru Forester", "Subaru Impreza",
                            "Ford Taurus", "Ford Explorer"};


                    String[] town = {"Carmel, NY", "Brewster, NY","Danbury, CT","Poughkeepsie, NY","Kent, NY","Mahopac, NY","Yorktown, NY"};
                    Double[] lat = {41.4129884,  41.3971734,  41.3953429, 41.6939296, 41.4733805,  41.3713729,  41.2728285};
                    Double[] lon = {-73.690875, -73.6132881, -73.4723945, -73.916076, -73.743121, -73.7371036, -73.8084955};

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSZ", Locale.US);

                    String dateStamp = sdf.format(new Date());
                    String randomCategory = category[rnd.nextInt(category.length)];
                    String[] carType = cars[rnd.nextInt(cars.length)].split(" ");
                    String randomColor = colors[rnd.nextInt(colors.length)];

                    int randomTown = rnd.nextInt(town.length);

                    ContentValues values = new ContentValues();

                    values.put(EventContract.EventEntry.COLUMN_NAME_DATE, dateStamp);
                    values.put(EventContract.EventEntry.COLUMN_NAME_EVENT_ID, randomCategory);
                    values.put(EventContract.EventEntry.COLUMN_NAME_MAKE, carType[0]);
                    values.put(EventContract.EventEntry.COLUMN_NAME_MODEL, carType[1]);
                    values.put(EventContract.EventEntry.COLUMN_NAME_COLOR, randomColor);
                    values.put(EventContract.EventEntry.COLUMN_NAME_LOCATION, town[randomTown]);
                    values.put(EventContract.EventEntry.COLUMN_NAME_LAT, lat[randomTown]);
                    values.put(EventContract.EventEntry.COLUMN_NAME_LON, lon[randomTown]);

                    Uri mNewUri;

                    mNewUri = getContentResolver().insert(MyEventProvider.CONTENT_URI, values);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                mHandler.post(new MyToastRunnable("Create alert - waking up from 5 seconds"));
            }
        }
    }

    @Override
    public void onCreate() {

        Thread thread = new Thread(new MyRunnable());
        thread.start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler = new Handler();

//        Toast.makeText(this, "onStartCommand - CreateAlertService", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
//        super.onDestroy();
        blnKeepCreating=false;
//        Toast.makeText(this, "onDestroy - CreateAlertService", Toast.LENGTH_SHORT).show();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
