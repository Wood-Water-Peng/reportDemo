package com.example.lib.event;

import com.example.lib.core.ReportHandler;

/**
 * @Author jacky.peng
 * @Date 2021/3/18 4:10 PM
 * @Version 1.0
 */
public class PageEvent {
    public String name;
    public String id;
    String tag;
    public PageEvent(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public PageEvent(String id) {
        this.id = id;
        this.name = "default";
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void report() {
//        ReportHandler.getInstance().getReportEngine().reportEvent(this, 0);
    }
}
