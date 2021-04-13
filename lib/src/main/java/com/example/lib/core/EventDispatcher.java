package com.example.lib.core;

import android.os.AsyncTask;

import com.example.lib.ReportLog;
import com.example.lib.Write2DBTask;
import com.example.lib.event.Event;
import com.example.lib.executor.JobExecutors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @Author jacky.peng
 * @Date 2021/4/13 11:06 AM
 * @Version 1.0
 * <p>
 * 处理上报事件的缓存
 */
public class EventDispatcher {
    private static volatile EventDispatcher sInstance;

    private final List<Event> pendingEvents = new ArrayList<>(8);

    private EventDispatcher() {

    }

    public static EventDispatcher getsInstance() {
        if (sInstance == null) {
            synchronized (EventDispatcher.class) {
                if (sInstance == null) {
                    sInstance = new EventDispatcher();
                }
            }
        }
        return sInstance;
    }


    public void enqueue(Event event) {
        //1.如果队列中的任务超过了20，那么开启一个线程去执行写入数据库操作
        pendingEvents.add(event);
        if (pendingEvents.size() >= 8) {
            ReportLog.logD("当前缓存数量->" + pendingEvents.size());
            Write2DBTask write2DBTask = new Write2DBTask();
            List<Event> list = new ArrayList<>(8);
            for (Event event1 : pendingEvents
            ) {
                list.add(event1);
            }
            pendingEvents.clear();
            write2DBTask.execute(list);
        }
    }
}
