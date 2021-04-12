package com.example.lib.db;

import com.example.lib.bean.PageEventWrapper;
import com.example.lib.event.PageEvent;

import java.util.List;

/**
 * @Author jacky.peng
 * @Date 2021/3/18 7:29 PM
 * @Version 1.0
 */
public interface PageEventDao {
    //获取表中的行数和
    int getEventNum();

    //插入到表的末位行
    void insertEvent(PageEvent event);


    void insertEventList(List<PageEvent> event);

    void deleteEvent(String eventId);

    PageEventWrapper generateWrapperFromDB(int size);

}
