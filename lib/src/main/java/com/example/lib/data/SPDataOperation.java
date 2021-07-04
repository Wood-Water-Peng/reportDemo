package com.example.lib.data;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import org.json.JSONObject;

//
public class SPDataOperation extends DataOperation{
    public SPDataOperation(Context context) {
        super(context);
    }

    @Override
    public int insertData(Uri uri, JSONObject jsonObject) {
        return 0;
    }

    @Override
    public String[] queryData(Uri uri, int limit) {
        return new String[0];
    }
}
