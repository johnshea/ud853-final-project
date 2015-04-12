package com.example.android.googlejamfinalproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by John on 4/12/2015.
 */
public class EventOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "googlejam.db";

    private static final String EVENT_TABLE_CREATE =
            "CREATE TABLE " + EventContract.EventEntry.TABLE_NAME + " (" +
                    EventContract.EventEntry._ID + " INTEGER PRIMARY KEY," +
                    EventContract.EventEntry.COLUMN_NAME_EVENT_ID + " int," +
                    EventContract.EventEntry.COLUMN_NAME_DATE + " TEXT," +
                    EventContract.EventEntry.COLUMN_NAME_CATEGORY + " int," +
                    EventContract.EventEntry.COLUMN_NAME_MAKE + " varchar(25)," +
                    EventContract.EventEntry.COLUMN_NAME_MODEL + " varchar(25)," +
                    EventContract.EventEntry.COLUMN_NAME_COLOR + " varchar(25)," +
                    EventContract.EventEntry.COLUMN_NAME_PLATE + " varchar(25)," +
                    EventContract.EventEntry.COLUMN_NAME_PLATELOCALE + " varchar(25)," +
                    EventContract.EventEntry.COLUMN_NAME_MISC + " varchar(255)," +
                    EventContract.EventEntry.COLUMN_NAME_LAT + " decimal(9,6)," +
                    EventContract.EventEntry.COLUMN_NAME_LON + " decimal(9,6)," +
                    EventContract.EventEntry.COLUMN_NAME_DIRECTION + " varchar(10)," +
                    EventContract.EventEntry.COLUMN_NAME_USERID + " int," +
                    EventContract.EventEntry.COLUMN_NAME_LOCATION + " varchar(25)" +
                    " )";

    private static final String EVENT_TABLE_DELETE =
            "DROP TABLE IF EXISTS " + EventContract.EventEntry.TABLE_NAME;

    public EventOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EVENT_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(EVENT_TABLE_DELETE);
        onCreate(db);
    }
}
