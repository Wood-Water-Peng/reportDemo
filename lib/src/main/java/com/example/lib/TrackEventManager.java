package com.example.lib;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author jacky.peng
 * @Date 2021/4/13 3:43 PM
 * @Version 1.0
 * <p>
 * 监听任务队列
 */
public class TrackEventManager {
    private static volatile TrackEventManager sInstance;
    //提交的缓存事件
    private final LinkedBlockingQueue<Runnable> mTrackEventTasks;

    private TrackEventManager() {
        this.mTrackEventTasks = new LinkedBlockingQueue<>();
    }

    public static TrackEventManager getsInstance() {
        if (sInstance == null) {
            synchronized (FakeDB.class) {
                if (sInstance == null) {
                    sInstance = new TrackEventManager();
                }
            }
        }
        return sInstance;
    }

    public void addTrackEventTask(Runnable task) {
        try {
            mTrackEventTasks.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Runnable takeTrackEventTask() {
        try {
            return mTrackEventTasks.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Runnable pollTrackEventTask() {
        return mTrackEventTasks.poll();
    }
}
