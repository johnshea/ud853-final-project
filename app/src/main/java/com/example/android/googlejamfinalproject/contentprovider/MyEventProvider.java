package com.example.android.googlejamfinalproject.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.example.android.googlejamfinalproject.data.EventContract;
import com.example.android.googlejamfinalproject.data.EventOpenHelper;

/**
 * Created by John on 4/12/2015.
 */
public class MyEventProvider extends ContentProvider {

    private EventOpenHelper database;

    private static final String AUTHORITY = "com.example.android.googlejamfinalproject.eventprovider";

    private static final String BASE_PATH = "events";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/events";

    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/event";

    private static final int EVENTS = 10;
    private static final int EVENT_ID = 20;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, EVENTS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", EVENT_ID);
    }

    @Override
    public boolean onCreate() {
        database = new EventOpenHelper(getContext());
        return false;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        int uriType = sURIMatcher.match(uri);

        SQLiteDatabase db = database.getWritableDatabase();

        long id = 0;

        switch (uriType) {
            case EVENTS:
                id = db.insert(EventContract.EventEntry.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(EventContract.EventEntry.TABLE_NAME);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case EVENTS:
                break;
            case EVENT_ID:
                queryBuilder.appendWhere(EventContract.EventEntry._ID + "="
                + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getReadableDatabase();
//        Cursor cursor = queryBuilder.query(db, projection, selection,
//                selectionArgs, null, null, sortOrder);
        Cursor cursor = queryBuilder.query(db, projection, null,
                null, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }
}
