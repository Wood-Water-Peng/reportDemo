package com.example.lib.event;

import com.example.lib.core.EventDispatcher;
import com.example.lib.core.ReportCenterAPI;
import com.example.lib.core.ReportHandler;

/**
 * @Author jacky.peng
 * @Date 2021/4/12 11:07 AM
 * @Version 1.0
 */
public class Event {
    public static final String APP_INSTALL_EVENT = "$AppInstall";
    public static final String APP_FIRST_OPEN = "$AppInstall";
    public static final String VIEW_CLICK_EVENT = "$ViewClickEvent";
    public static final String VIEW_SHOW_EVENT = "$ViewShowEvent";
    public static final String PAGE_SHOW_EVENT = "$PageShowEvent";
    public static final String PAGE_HIDE_EVENT = "$PageHideEvent";
    public String tag;

    public void report() {
//        EventDispatcher.getsInstance().enqueue(this);

    }
}
