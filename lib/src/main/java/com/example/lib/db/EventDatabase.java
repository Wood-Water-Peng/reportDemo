package com.example.lib.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * @Author jacky.peng
 * @Date 2021/4/14 9:24 AM
 * @Version 1.0
 */
@Database(entities = {EventEntity.class}, version = 2, exportSchema = false)
public abstract class EventDatabase extends RoomDatabase {
    public abstract EventDao getEventDao();
}
