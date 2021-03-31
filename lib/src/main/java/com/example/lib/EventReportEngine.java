package com.example.lib;

import com.example.lib.event.PageEvent;
import com.example.lib.executor.ReportExecutor;

/**
 * @Author jacky.peng
 * @Date 2021/3/18 4:09 PM
 * @Version 1.0
 */
public class EventReportEngine {
    private ReportExecutor mReportExecutor;

    public EventReportEngine(ReportExecutor mReportExecutor) {
        this.mReportExecutor = mReportExecutor;
    }

    public void reportEvent(PageEvent event, long delay) {
        mReportExecutor.enqueueEvent(event);
    }
}
