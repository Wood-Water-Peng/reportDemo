package com.example.lib.db;

import android.net.Uri;

import java.security.PublicKey;

/**
 * @Author jacky.peng
 * @Date 2021/4/14 9:43 AM
 * @Version 1.0
 */
public class DBParams {
    private static DBParams instance;
    private final Uri mUri;
    public static final int DB_OUT_OF_MEMORY_ERROR = -2;

    public static final String GZIP_DATA_EVENT = "1";
    /* Event 表字段 */
    public static final String KEY_DATA = "data";
    public static final String KEY_CREATED_AT = "create_time";
    public Uri getUri() {
        return mUri;
    }

    public static DBParams getInstance(String packageName) {
        if (instance == null) {
            instance = new DBParams(packageName);
        }
        return instance;
    }

    private DBParams(String packageName) {
        mUri = Uri.parse("content://" + packageName + ".DemoContentProvider/" + EVENT_TABLE_NAME);
    }

    /**
     * 表名称
     */
    public static final String EVENT_TABLE_NAME = "event_table";

    /**
     * 表名称 ---模拟上传到服务的数据
     */
    public static final String EVENT_REPORT_TABLE_NAME = "event_report_table";

    /**
     * 数据库名称
     */
    public static final String DB_NAME = "report_db";
}
