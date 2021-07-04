package com.example.reportdemo;

import android.app.Application;

import com.example.lib.EventGatherModule;
import com.example.lib.ActivityLifecycleTracker;
import com.example.lib.core.ReportCenterAPI;
import com.example.lib.core.ReportConfig;
import com.example.lib.core.ReportHandler;
import com.example.lib.core.TestReportConfig;

/**
 * @Author jacky.peng
 * @Date 2021/3/18 11:18 AM
 * @Version 1.0
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ReportHandler.getInstance().init(this);
        TestReportConfig configOptions = new TestReportConfig.Builder("test").build();

        ReportCenterAPI.initWithConfigOptions(this, configOptions);
        ActivityLifecycleTracker activityLifecycleTracker = new ActivityLifecycleTracker(this);
        activityLifecycleTracker.startTrack();


        //采集事件模块
        EventGatherModule eventGatherModule = new EventGatherModule();
        eventGatherModule.onCreate(this);
    }


}
