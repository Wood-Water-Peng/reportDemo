package com.example.lib.db;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * @Author jacky.peng
 * @Date 2021/4/14 9:21 AM
 * @Version 1.0
 */
@Dao
public interface EventReportDao {
    @Insert
    long insertEvent(EventReportEntity entity);


    @Insert
    long[] insertEvents(EventReportEntity... entities);

    @Update
    int updateWords(EventReportEntity... entities);

    @Delete
    void deleteWords(EventReportEntity... entities);

    @Query("DELETE FROM event_report_table WHERE id<=:id")
    void deleteEventsById(int id);

    @Query("SELECT * FROM event_report_table ORDER BY ID ASC LIMIT:limit")
    List<EventReportEntity> getLimitEvents(int limit);

    @Query("SELECT * FROM event_report_table ORDER BY ID ASC LIMIT :limit")
    EventReportEntity queryEntityByLimit(int limit);


    @Query("SELECT COUNT(*) FROM event_report_table")
    long queryCount();
}
