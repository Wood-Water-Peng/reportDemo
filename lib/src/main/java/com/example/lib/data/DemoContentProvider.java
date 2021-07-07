package com.example.lib.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.renderscript.Allocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;

import com.example.lib.db.DBOperation;
import com.example.lib.db.DBParams;
import com.example.lib.db.EventEntity;
import com.example.lib.utils.AppInfoUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

//提供埋点的数据操作
public class DemoContentProvider extends ContentProvider {

    private ContentResolver contentResolver;
    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private final static int EVENTS = 1;
    private DBOperation dbOperation;

    @Override
    public boolean onCreate() {
        //如果这个类运行在单独的进程，那么这里的context代表了他启动他的App进程的相关Context
        String packageName = getContext().getApplicationContext().getPackageName();
        String authority = packageName + ".DemoContentProvider";
        contentResolver = getContext().getContentResolver();
        uriMatcher.addURI(authority, DBParams.EVENT_TABLE_NAME, EVENTS);
        dbOperation = DBOperation.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        int code = uriMatcher.match(uri);
        Cursor cursor = null;
        if (code == EVENTS) {
            SupportSQLiteDatabase db = dbOperation.getSupportSQLiteOpenHelper().getWritableDatabase();
            SupportSQLiteQuery query =
                    SupportSQLiteQueryBuilder.builder(DBParams.EVENT_TABLE_NAME)
                            .columns(projection)
                            .orderBy(sortOrder)
                            .selection(selection, selectionArgs)
                            .create();
            cursor = db.query(query);
        }
        return cursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        try {
            int code = uriMatcher.match(uri);

            if (code == EVENTS) {
                return insertEvent(uri, values);
            }
            return uri;
        } catch (Exception e) {
        }
        return uri;
    }

    private Uri insertEvent(Uri uri, ContentValues values) {
        SupportSQLiteDatabase db = dbOperation.getSupportSQLiteOpenHelper().getWritableDatabase();
        long d = db.insert(DBParams.EVENT_TABLE_NAME, SQLiteDatabase.CONFLICT_NONE, values);

        return ContentUris.withAppendedId(uri, d);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deletedCounts = 0;
        try {
            int code = uriMatcher.match(uri);
            if (EVENTS == code) {
                try {
                    SupportSQLiteDatabase db = dbOperation.getSupportSQLiteOpenHelper().getWritableDatabase();
                    db.delete(DBParams.EVENT_TABLE_NAME, selection, selectionArgs);
                } catch (SQLiteException e) {
                }
            }
        } catch (Exception e) {
        }
        return deletedCounts;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
