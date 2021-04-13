package com.example.lib;

import com.example.lib.event.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author jacky.peng
 * @Date 2021/4/13 11:06 AM
 * @Version 1.0
 * <p>
 * 处理上报事件的缓存
 */
public class FakeDB {
    private static volatile FakeDB sInstance;

    private final List<Event> dbEvents = new ArrayList<>(32);

    private FakeDB() {

    }

    public static FakeDB getsInstance() {
        if (sInstance == null) {
            synchronized (FakeDB.class) {
                if (sInstance == null) {
                    sInstance = new FakeDB();
                }
            }
        }
        return sInstance;
    }

    /**
     * 模拟事件写入数据库
     * 当事件总数>20    触发上报事件
     *
     * @param event
     * @return
     */
    public boolean write(Event event) {
        try {
            Thread.sleep(100);
            Random random = new Random();
            boolean b = random.nextBoolean();
            if (b) {
                dbEvents.add(event);
                return true;
            } else {
                return false;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
