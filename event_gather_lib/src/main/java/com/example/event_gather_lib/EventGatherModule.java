package com.example.event_gather_lib;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lib.EventConstants;
import com.example.lib.ReportLog;
import com.example.lib.event.ActivityEvent;
import com.example.lib.event.ApplicationEvent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author jacky.peng
 * @Date 2021/4/12 11:12 AM
 * @Version 1.0
 */
public class EventGatherModule {

    AtomicInteger activityCount = new AtomicInteger();

    public void onCreate(Application app) {
        ApplicationEvent event = new ApplicationEvent.Builder().setTag(EventConstants.APPLICATION_EVENT_ON_CREATED).setPackageName(app.getPackageName()).setOnCreateTimeStamp(System.currentTimeMillis()).build();
        ReportLog.logD(app.getPackageName() + " onCreate");

        app.registerActivityLifecycleCallbacks(new SimpleActivityLifecycleCallBack() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                activityCount.incrementAndGet();

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                if (activityCount.get() == 1) {
                    ApplicationEvent event = new ApplicationEvent.Builder().setTag(EventConstants.APPLICATION_EVENT_ON_RESUMED).setPackageName(activity.getApplication().getPackageName()).setOnCreateTimeStamp(System.currentTimeMillis()).build();
                    ReportLog.logD(activity.getApplication().getPackageName() + " onResume");
                }

                ActivityEvent event = new ActivityEvent.Builder().setTag(EventConstants.ACTIVITY_EVENT_ON_RESUME).setActivityName(activity.getClass().getCanonicalName()).setOnCreateTimeStamp(System.currentTimeMillis()).build();
                ReportLog.logD(event.getActivityName() + " onResume");


            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                activityCount.decrementAndGet();
                if (activityCount.get() == 0) {
                    onDestroy(activity.getApplication());
                }
            }
        });
    }

    public void onDestroy(Application app) {
        ApplicationEvent event = new ApplicationEvent.Builder().setTag(EventConstants.APPLICATION_EVENT_ON_DESTROY).setPackageName(app.getPackageName()).setOnDestroyTimeStamp(System.currentTimeMillis()).build();
        ReportLog.logD(app.getPackageName() + " onDestroy");
    }
}
