package com.example.lib.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.json.JSONObject;

//操作数据库的抽象类
public abstract class DataOperation {
    ContentResolver contentResolver;
    Context context;

    public DataOperation(Context context) {
        this.context = context;
        contentResolver = context.getContentResolver();
    }

    /**
     * 保存数据
     */
    public abstract int insertData(Uri uri, JSONObject jsonObject);


    /**
     * 查询数据
     */
    public abstract String[] queryData(Uri uri, int limit);

    /**
     * 查询数据条数
     *
     * @param uri Uri
     * @return 条数
     */
    public int queryDataCount(Uri uri) {
        return queryDataCount(uri, null, null, null, null);
    }

    /**
     * 查询数据条数
     */
    public int queryDataCount(Uri uri, String[] projection, String selection,
                       String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(uri, projection, selection, selectionArgs, sortOrder);
            if (cursor != null) {
                return cursor.getCount();
            }
        } catch (Exception ex) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }

    /**
     * 删除数据   删除比id小的所有数据
     *
     * @param id
     */
    public void deleteData(Uri uri, String id) {
        try {
            contentResolver.delete(uri, "id <= ?", new String[]{id});
        } catch (Exception e) {

        }
    }

}
