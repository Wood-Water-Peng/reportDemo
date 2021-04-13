package com.example.lib.core;

import com.example.lib.EventReportEngine;
import com.example.lib.ReportLog;
import com.example.lib.TrackEventManager;
import com.example.lib.TrackEventManagerThread;
import com.example.lib.event.Event;
import com.example.lib.executor.ReportExecutor;

/**
 * @Author jacky.peng
 * @Date 2021/3/18 4:13 PM
 * @Version 1.0
 */
public class ReportHandler {
    EventReportEngine reportEngine;
    //监听任务的任务
    protected TrackEventManagerThread mTrackTaskManagerThread;
    protected TrackEventManager mTrackTaskManager;

    public EventReportEngine getReportEngine() {
        return reportEngine;
    }

    private ReportHandler() {
    }

    public static ReportHandler getInstance() {
        return ReportHandlerHolder.INSTANCE;
    }

    private static class ReportHandlerHolder {
        static final ReportHandler INSTANCE = new ReportHandler();
    }

    private volatile boolean _hasInit = false;

    public void init() {
        if (_hasInit) {
            throw new IllegalStateException("has already inited");
        }
        reportEngine = new EventReportEngine(new ReportExecutor());
        _hasInit = true;
        if (mTrackTaskManagerThread == null) {
            mTrackTaskManagerThread = new TrackEventManagerThread();
            new Thread(mTrackTaskManagerThread).start();
            ReportLog.logD("Data collection thread has been started");
        }
        mTrackTaskManager = TrackEventManager.getsInstance();
    }

    public void track(final Event event) {
        mTrackTaskManager.addTrackEventTask(new Runnable() {
            @Override
            public void run() {
                _trackEvent(event);
            }
        });
    }

    private void _trackEvent(Event event) {
        //1.将事件添加到数据库

        //2.根据数据库中当前的条目，立刻上传或者延迟20s之后执行上传任务
    }
}
