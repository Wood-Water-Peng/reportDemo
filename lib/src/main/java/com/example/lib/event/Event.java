package com.example.lib.event;

import com.example.lib.core.EventDispatcher;
import com.example.lib.core.ReportHandler;

/**
 * @Author jacky.peng
 * @Date 2021/4/12 11:07 AM
 * @Version 1.0
 */
public class Event {

    public String tag;

    public void report() {
//        EventDispatcher.getsInstance().enqueue(this);
        ReportHandler.getInstance().track(this);
    }
}
