package com.example.lib.db;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * @Author jacky.peng
 * @Date 2021/4/14 9:21 AM
 * @Version 1.0
 */
@Dao
public interface EventDao {
    @Insert
    long insertEvent(EventEntity entity);

    @Query("DELETE FROM event_table WHERE id<=:id")
    void deleteEventsById(String id);

    @Query("SELECT * FROM event_table ORDER BY ID ASC LIMIT:limit")
    List<EventEntity> getLimitEvents(int limit);


    @Query("SELECT COUNT(*) FROM event_table")
    long queryCount();

    @Query("SELECT * FROM event_table ORDER BY ID ASC LIMIT :limit")
    Cursor queryCountWithCursor(int limit);
}
