package com.example.lib.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * @Author jacky.peng
 * @Date 2021/4/14 9:26 AM
 * @Version 1.0
 */
public class DBOperation {
    private static volatile DBOperation sInstance;
    private final SupportSQLiteOpenHelper supportSQLiteOpenHelper;
    EventDatabase database;
    EventDao eventDao;
    EventReportDao eventReportDao;
    Context mContext;
    DBParams dbParams;

    public static DBOperation getInstance(Context context) {
        if (sInstance == null) {
            synchronized (DBOperation.class) {
                if (sInstance == null) {
                    sInstance = new DBOperation(context);
                }
            }
        }
        return sInstance;
    }

    public SupportSQLiteOpenHelper getSupportSQLiteOpenHelper() {
        return supportSQLiteOpenHelper;
    }

    public DBOperation(Context context) {
        database = Room.databaseBuilder(context.getApplicationContext(), EventDatabase.class, DBParams.DB_NAME) //new a database
                .build();
        supportSQLiteOpenHelper = database.getOpenHelper();
        eventDao = database.getEventDao();
        eventReportDao = database.getEventReportDao();

        dbParams = DBParams.getInstance(context.getPackageName());
        this.mContext = context;
    }

    /**
     * @param entity 插入新数据的行数
     * @return
     */
    public long insertEvent(EventEntity entity) {
        return eventDao.insertEvent(entity);
    }

    public EventDao getEventDao() {
        return eventDao;
    }


}
