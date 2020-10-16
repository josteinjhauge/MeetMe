package com.example.meetme;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ContactContentProvider extends ContentProvider {

    private MeetMeDao meetMeDao;

    public static final String AUTHORITY = "com.example.meetme";
    public static final String CONTACT_TABLE = "contact_table";
    public static final int ALL_CONTACTS_ID = 1;
    public static final int CONTACT_ID = 2;

    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, CONTACT_TABLE, ALL_CONTACTS_ID);
        uriMatcher.addURI(AUTHORITY, CONTACT_TABLE + "/*", CONTACT_ID);
    }

    @Override
    public boolean onCreate() {
        meetMeDao = MeetMeDatabase.getDatabase(getContext())
                .meetMeDao();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor;
        final int code = uriMatcher.match(uri);
        if (code == ALL_CONTACTS_ID || code == CONTACT_ID) {
            final Context context = getContext();
            if (context == null) {
                Log.d("ContactContentProvider", "query: Context == null");
                return null;
            }
            if (code == ALL_CONTACTS_ID) {
                cursor = meetMeDao.getAllContactsForCP();
            } else {
                cursor = meetMeDao.getContactForCP(ContentUris.parseId(uri));
            }
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        } else {
            Log.d("ContactContentProvider", "query: Uri codene matcher ikke");
            return null;
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
