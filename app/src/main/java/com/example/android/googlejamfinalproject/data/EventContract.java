package com.example.android.googlejamfinalproject.data;

import android.provider.BaseColumns;

/**
 * Created by John on 4/12/2015.
 */
public final class EventContract {

    public EventContract() {}

    public static abstract class EventEntry implements BaseColumns {
        public static final String TABLE_NAME = "event";
        public static final String COLUMN_NAME_EVENT_ID = "eventid";
        public static final String COLUMN_NAME_DATE = "dt";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_MAKE = "make";
        public static final String COLUMN_NAME_MODEL = "model";
        public static final String COLUMN_NAME_COLOR = "color";
        public static final String COLUMN_NAME_PLATE = "plate";
        public static final String COLUMN_NAME_PLATELOCALE = "platelocale";
        public static final String COLUMN_NAME_MISC = "misc";
        public static final String COLUMN_NAME_LAT = "lat";
        public static final String COLUMN_NAME_LON = "lon";
        public static final String COLUMN_NAME_DIRECTION = "direction";
        public static final String COLUMN_NAME_USERID = "userid";
        public static final String COLUMN_NAME_LOCATION = "location";
    }
}
