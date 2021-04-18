package com.example.lib.db;

import android.content.Context;

import androidx.room.Room;

import com.example.lib.ReportMessageCenter;
import com.example.lib.core.ReportHandler;

import org.json.JSONObject;

import java.util.List;

/**
 * @Author jacky.peng
 * @Date 2021/4/14 9:26 AM
 * @Version 1.0
 */
public class DBHelper {
    private static volatile DBHelper sInstance;
    EventDatabase database;
    EventDao eventDao;
    EventReportDao eventReportDao;


    public static DBHelper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (DBHelper.class) {
                if (sInstance == null) {
                    sInstance = new DBHelper(context);
                }
            }
        }
        return sInstance;
    }

    public DBHelper(Context context) {
        database = Room.databaseBuilder(context.getApplicationContext(), EventDatabase.class, DBParams.DB_NAME) //new a database
                .build();
        eventDao = database.getEventDao();
        eventReportDao = database.getEventReportDao();
    }

    /**
     * @param entity 插入新数据的行数
     * @return
     */
    public long insertEvent(EventEntity entity) {
        return eventDao.insertEvent(entity);
    }

    /**
     * @param entity 模拟将数据上传到服务器
     * @return
     */
    public long reportEvent(EventReportEntity entity) {
        return eventReportDao.insertEvent(entity);
    }

    /**
     * @param lastId 要删除的最后一行的id
     * @return 表中的count数
     */
    public long deleteEvents(int lastId) {
        eventDao.deleteEventsById(lastId);
        return eventDao.queryCount();
    }


    /**
     * @param limit 最多获取的行数
     * @return 表中的count数
     */
    public List<EventEntity> queryEvents(int limit) {
        return eventDao.getLimitEvents(limit);
    }
}
