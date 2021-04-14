package com.example.lib.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @Author jacky.peng
 * @Date 2021/4/14 9:19 AM
 * @Version 1.0
 */
@Entity(tableName = DBParams.EVENT_TABLE_NAME)
public class EventEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "event_name")
    private String name;

    @ColumnInfo(name = "create_time")
    private String create_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
