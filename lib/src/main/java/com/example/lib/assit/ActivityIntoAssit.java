package com.example.lib.assit;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Parcelable;

import com.example.lib.ReportLog;
import com.example.lib.core.DBAdapter;
import com.example.lib.core.ReportCenterAPI;
import com.example.lib.event.Event;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 保存activity相关信息
 */
public class ActivityIntoAssit {
    //存活的activity
    private AtomicInteger activityCount = new AtomicInteger();
    //可见的activity
    private AtomicInteger resumeActivityCount = new AtomicInteger();
    private static ActivityIntoAssit instance;
    private Handler mHandler;
    //app被杀死
    private final int MESSAGE_CODE_APP_END = 0;

    //app启动
    private final int MESSAGE_CODE_APP_START = 1;
    //activity启动
    private final int MESSAGE_CODE_ACTIVITY_START = 100;
    //activity被杀死
    private final int MESSAGE_CODE_ACTIVITY_STOP = 200;
    ReportCenterAPI reportCenterAPI;
    private long appStartTime;

    protected ActivityIntoAssit(ReportCenterAPI reportCenterAPI) {
        this.reportCenterAPI = reportCenterAPI;
        initHandler();
    }

    public static ActivityIntoAssit getInstance(ReportCenterAPI reportCenterAPI) {
        if (instance == null) {
            instance = new ActivityIntoAssit(reportCenterAPI);
        }
        return instance;
    }


    public void increaseActivityCount() {
        activityCount.getAndIncrement();
    }

    public void decreaseActivityCount() {
        activityCount.getAndDecrement();
    }

    public int getActivityCount() {
        return activityCount.get();
    }


    private void initHandler() {
        //单独开一个线程来接受app相关的事件
        HandlerThread handlerThread = new HandlerThread("SENSORS_DATA_THREAD");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                int code = msg.what;
                switch (code) {
                    case MESSAGE_CODE_APP_START:
//                        handleAppStart(msg);
                        break;
                    case MESSAGE_CODE_ACTIVITY_START:
//                        handleActivityStart(msg);
                        break;
                    case MESSAGE_CODE_ACTIVITY_STOP:
//                        handleActivityStop(msg);
                        break;
                    case MESSAGE_CODE_APP_END:
                        handleAppEnd();
                        break;
                }
            }
        };
    }

    public void handleActivityStop(Activity activity) {
        int count = resumeActivityCount.decrementAndGet();
        ReportLog.logD("handleActivityStop resumeActivityCount:" + count);
        if (count == 0) {
            //暂且认为app进入后台
            ReportLog.logD("handleActivityStop: app进入后台");
        }
    }

    private void handleAppEnd() {
        ReportLog.logD("handleActivity appEnd");
        JSONObject object = new JSONObject();
        try {
            long endTime = System.currentTimeMillis();
            object.put("app_start_time", appStartTime);
            object.put("app_end_time", endTime);
            object.put("app_duration", endTime - appStartTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        reportCenterAPI.trackEvent(Event.APP_END, object);
        mHandler.getLooper().quit();
    }

    public void handleActivityResume(Activity activity) {
//        int count = activityCount.incrementAndGet();
//        ReportLog.logD("handleActivityResume:" + count);
//        if (count == 1) {
//            //暂且认为app启动
//            handleAppStart(activity);
//        }
    }

    public void handleActivityCreated(Activity activity) {
        int count = activityCount.incrementAndGet();
        ReportLog.logD("handleActivityResume activityCount:" + count);
        if (count == 1) {
            //暂且认为app启动
            handleAppStart(activity);
        }
    }

    public void handleActivityStart(Activity activity) {
        int count = resumeActivityCount.incrementAndGet();
        ReportLog.logD("handleActivityStart resumeActivityCount:" + resumeActivityCount);
        if (count == 1) {
            ReportLog.logD("handleActivityStart: app进入前台");
        }

    }

    public void handleActivityDestroyed(Activity activity) {
        int count = activityCount.decrementAndGet();
        ReportLog.logD("handleActivityDestroyed activityCount:" + count);
        if (count == 0) {
            //暂且认为app结束
            Message obtain = Message.obtain();
            obtain.what = MESSAGE_CODE_APP_END;
            mHandler.sendMessageDelayed(obtain, reportCenterAPI.getSessionTime());
        }
    }

    public void handleAppStart(Activity activity) {
        ReportLog.logD("handleActivity appStart");
        JSONObject object = new JSONObject();
        try {
            appStartTime = System.currentTimeMillis();
            object.put("app_start_time", appStartTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        reportCenterAPI.trackEvent(Event.APP_START, object);
    }
}