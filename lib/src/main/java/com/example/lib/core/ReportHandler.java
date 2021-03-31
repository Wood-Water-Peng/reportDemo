package com.example.lib.core;

import com.example.lib.EventReportEngine;
import com.example.lib.executor.ReportExecutor;

/**
 * @Author jacky.peng
 * @Date 2021/3/18 4:13 PM
 * @Version 1.0
 */
public class ReportHandler {
    EventReportEngine reportEngine;

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
    }
}
