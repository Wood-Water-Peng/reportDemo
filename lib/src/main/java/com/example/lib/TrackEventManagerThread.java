package com.example.lib;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author jacky.peng
 * @Date 2021/4/13 3:42 PM
 * @Version 1.0
 * <p>
 * 在初始化时启动，用于监听缓冲队列中的任务
 */
public class TrackEventManagerThread implements Runnable {
    private boolean isStop;
    private TrackEventManager mTrackTaskManager;
    private ExecutorService mPool;
    private static final int POOL_SIZE = 1;

    public TrackEventManagerThread() {
        mTrackTaskManager = TrackEventManager.getsInstance();
        mPool = Executors.newFixedThreadPool(POOL_SIZE);
    }

    @Override
    public void run() {
        while (!isStop) {
            Runnable downloadTask = mTrackTaskManager.takeTrackEventTask();
            mPool.execute(downloadTask);
        }
        //将队列中的剩余任务处理完
        while (true) {
            Runnable downloadTask = mTrackTaskManager.pollTrackEventTask();
            if (downloadTask == null) {
                break;
            }
            mPool.execute(downloadTask);
        }
        mPool.shutdown();
    }
}
