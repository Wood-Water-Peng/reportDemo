package com.example.lib.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.text.TextUtils;

import com.example.lib.db.DBParams;

import org.json.JSONObject;

public class EventDataOperation extends DataOperation {
    public EventDataOperation(Context context) {
        super(context);
    }

    @Override
    public int insertData(Uri uri, JSONObject jsonObject) {
        ContentValues cv = new ContentValues();
        cv.put(DBParams.KEY_DATA, jsonObject.toString() + "\t" + jsonObject.toString().hashCode());
        cv.put(DBParams.KEY_CREATED_AT, System.currentTimeMillis());
        contentResolver.insert(uri, cv);
        return 0;
    }

    @Override
    public String[] queryData(Uri uri, int limit) {
        Cursor cursor = null;
        String data = null;
        String last_id = null;
        try {
            cursor = contentResolver.query(uri, null, null, null, DBParams.KEY_CREATED_AT + " ASC LIMIT " + limit);
            if (cursor != null) {
                StringBuilder dataBuilder = new StringBuilder();
                final String flush_time = ",\"_flush_time\":";
                String suffix = ",";
                dataBuilder.append("[");
                String keyData;
                while (cursor.moveToNext()) {
                    if (cursor.isLast()) {
                        suffix = "]";
                        last_id = cursor.getString(cursor.getColumnIndex("id"));
                    }
                    try {
                        keyData = cursor.getString(cursor.getColumnIndex(DBParams.KEY_DATA));
                        keyData = parseData(keyData);
                        if (!TextUtils.isEmpty(keyData)) {
                            dataBuilder.append(keyData, 0, keyData.length() - 1)
                                    .append(flush_time)
                                    .append(System.currentTimeMillis())
                                    .append("}").append(suffix);
                        }
                    } catch (Exception e) {
                    }
                }
                data = dataBuilder.toString();
            }
        } catch (final SQLiteException e) {
            last_id = null;
            data = null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        if (last_id != null) {
            return new String[]{last_id, data, DBParams.GZIP_DATA_EVENT};
        }
        return null;
    }

    String parseData(String keyData) {
        try {
            if (TextUtils.isEmpty(keyData)) return "";
            int index = keyData.lastIndexOf("\t");
            if (index > -1) {
                String crc = keyData.substring(index).replaceFirst("\t", "");
                keyData = keyData.substring(0, index);
                if (TextUtils.isEmpty(keyData) || TextUtils.isEmpty(crc)
                        || !crc.equals(String.valueOf(keyData.hashCode()))) {
                    return "";
                }
            }
        } catch (Exception ex) {
        }
        return keyData;
    }
}
