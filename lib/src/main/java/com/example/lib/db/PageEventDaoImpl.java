package com.example.lib.db;

import com.example.lib.ReportLog;
import com.example.lib.bean.PageEventWrapper;
import com.example.lib.event.PageEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author jacky.peng
 * @Date 2021/3/18 7:34 PM
 * @Version 1.0
 */
public class PageEventDaoImpl implements PageEventDao {

    List<PageEvent> fakeData = new ArrayList<>();

    public PageEventDaoImpl() {
        for (int i = 0; i < 100; i++) {
            fakeData.add(new PageEvent(i + ""));
        }
    }

    @Override
    public int getEventNum() {
        return fakeData.size();
    }

    @Override
    public void insertEvent(PageEvent event) {
        fakeData.add(event);
    }

    @Override
    public void deleteEvent(String eventId) {
        for (int i = 0; i < fakeData.size(); i++) {
            PageEvent pageEvent = fakeData.get(i);
            if (pageEvent.getId().equals(eventId)) {
                fakeData.remove(pageEvent);
                ReportLog.logD("deleteEvent->"+pageEvent.getId());
            }
        }
    }


    /**
     * @param size 固定取出的数据
     * @return 生成包装类
     */
    @Override
    public PageEventWrapper generateWrapperFromDB(int size) {
        //要被删除的event
        StringBuilder sb = new StringBuilder();
        List<String> toBeDeleteEvent = new ArrayList<>();
        int count=Math.min(size,fakeData.size());
        for (int i = 0; i < count; i++) {
            //取出每一行数据
            PageEvent pageEvent = fakeData.get(i);
            if (pageEvent != null) {
                toBeDeleteEvent.add(pageEvent.getId());
            }
            sb.append(pageEvent.getName());
            sb.append("-");
            sb.append(pageEvent.getTag());
        }
        //生成有效的wrapper数据
        PageEventWrapper pageEventWrapper = new PageEventWrapper(toBeDeleteEvent.size(), sb.toString());
        //删除数据库中的标记数据
         for (int i = 0; i < toBeDeleteEvent.size(); i++) {
            deleteEvent(toBeDeleteEvent.get(i));
        }
        return pageEventWrapper;
    }
}
