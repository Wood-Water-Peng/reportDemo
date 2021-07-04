package com.example.lib.core;

import android.content.Context;

import com.example.lib.db.DBParams;
import com.example.lib.data.DataOperation;
import com.example.lib.data.EventDataOperation;
import com.example.lib.db.EventEntity;
import com.example.lib.db.EventReportEntity;

import org.json.JSONObject;

import java.util.List;

//操作数据库相关
public class DBAdapter {
    private static DBAdapter instance;
    private final DBParams mDbParams;
    private EventDataOperation mTrackEventOperation;

    private DBAdapter(Context context, String packageName) {
        mDbParams = DBParams.getInstance(packageName);
        mTrackEventOperation = new EventDataOperation(context.getApplicationContext());
    }

    public static DBAdapter getInstance() {
        if (instance == null) {
            throw new IllegalStateException("The static method getInstance(Context context, String packageName) should be called before calling getInstance()");
        }
        return instance;
    }

    public static DBAdapter getInstance(Context context, String packageName) {
        if (instance == null) {
            instance = new DBAdapter(context, packageName);
        }
        return instance;
    }

    /**
     * Removes events with an _id &lt;= last_id from table
     *
     * @param lastId the last id to delete
     * @return the number of rows in the table
     */
    public int deleteEvents(String lastId) {
        mTrackEventOperation.deleteData(mDbParams.getUri(), lastId);
        return mTrackEventOperation.queryDataCount(mDbParams.getUri());
    }

    public String[] queryEvents(int limit) {
        return mTrackEventOperation.queryData(mDbParams.getUri(), limit);
    }

    /**
     * @param eventJson
     * @return >0 这条数据在表中的行号    -1 出错   -2数据库数据已满
     */
    public int addJSON(JSONObject eventJson) {
        int code = mTrackEventOperation.insertData(mDbParams.getUri(), eventJson);
        if (code == 0) {
            //插入数据成功
            return mTrackEventOperation.queryDataCount(mDbParams.getUri());
        }
        return code;
    }
}
