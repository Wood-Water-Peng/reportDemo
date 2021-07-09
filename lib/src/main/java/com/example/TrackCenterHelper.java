package com.example;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.example.lib.core.ReportCenterAPI;
import com.example.lib.core.ReportHandler;
import com.example.lib.event.FragmentEvent;
import com.example.lib.event.ViewClickEvent;

///
///@author jacky.peng on
/// 收集事件的入口类
///
public class TrackCenterHelper {
    private static final String TGA = "TrackCenterHelper";

    //切勿修改！！！该方法可能被修改后的class文件调用
    public static void onViewClicked(View view) {
        Log.i(TGA, "TrackCenterHelper onViewClicked---");
        if (view == null) return;

        //1.判断View所属的activity是否被过滤
        //2.判断View所属的fragment是否被过滤
        //3.判断View是否被过滤
        ViewClickEvent clickEvent = new ViewClickEvent.Builder().setActivityName(view.getContext().getClass().getCanonicalName()).build();
        clickEvent.report();
    }


    /**
     * 这里用Object是因为并不清楚用户使用的Fragment，父类也可能有多个版本
     *
     * @param object
     */
    public static void trackFragmentResume(Object object) {
        Log.i(TGA, "trackFragmentResume object--->" + object.getClass().getCanonicalName());
        Log.i(TGA, "trackOnFragmentViewCreated object--->" + object.getClass().getCanonicalName());
        FragmentEvent fragmentEvent = new FragmentEvent.Builder().setTag((FragmentEvent.NAME_ON_VIEW_RESUMED)).setFragmentName(object.getClass().getCanonicalName()).build();
        fragmentEvent.report();

    }

    public static void trackFragmentSetUserVisibleHint(Object object, boolean flag) {
        Log.i(TGA, "trackFragmentSetUserVisibleHint object--->" + object.getClass().getCanonicalName());
    }

    public static void trackOnHiddenChanged(Object object, boolean onHiddenChanged) {
        Log.i(TGA, "trackOnHiddenChanged object--->" + object.getClass().getCanonicalName());
    }

    public static void trackOnFragmentViewCreated(Object object, View view, Bundle bundle) {
        Log.i(TGA, "trackOnFragmentViewCreated object--->" + object.getClass().getCanonicalName());
        FragmentEvent fragmentEvent = new FragmentEvent.Builder().setTag((FragmentEvent.NAME_ON_VIEW_CREATED)).setActivityName(view.getContext().getClass().getCanonicalName()).setFragmentName(object.getClass().getCanonicalName()).build();
        fragmentEvent.report();
    }

    public static void trackOnFragmentViewDestroyed(Object object) {
        Log.i(TGA, "trackOnFragmentViewDestroy object--->" + object.getClass().getCanonicalName());
        FragmentEvent fragmentEvent = new FragmentEvent.Builder().setTag((FragmentEvent.NAME_ON_VIEW_DESTROYED)).setFragmentName(object.getClass().getCanonicalName()).build();
        fragmentEvent.report();
    }

    public static void trackOnFragmentDetached(Object object) {
        Log.i(TGA, "trackOnFragmentDetached object--->" + object.getClass().getCanonicalName());
    }

    public static void trackOnFragmentAttached(Object object) {
        Log.i(TGA, "trackOnFragmentAttached object--->" + object.getClass().getCanonicalName());
    }

    public static void trackOnFragmentDestroyed(Object object) {
        Log.i(TGA, "trackOnFragmentDestroyed object--->" + object.getClass().getCanonicalName());
    }

    public static void trackOnFragmentCreated(Object object, Bundle bundle) {
        Log.i(TGA, "trackOnFragmentCreated object--->" + object.getClass().getCanonicalName());
    }


    public static void trackOnActivityCreated(Activity activity, Bundle bundle) {
        ReportCenterAPI.sharedInstance().getActivityIntoAssit().handleActivityCreated(activity);
    }

    public static void trackOnActivityStart(Activity activity) {
        ReportCenterAPI.sharedInstance().getActivityIntoAssit().handleActivityStart(activity);
    }

    public static void trackOnActivityStopped(Activity activity) {
        ReportCenterAPI.sharedInstance().getActivityIntoAssit().handleActivityStop(activity);
    }

    public static void trackOnActivityResumed(Activity activity) {
        ReportCenterAPI.sharedInstance().getActivityIntoAssit().handleActivityResume(activity);
    }

    public static void trackOnActivityDestroyed(Activity activity) {
        ReportCenterAPI.sharedInstance().getActivityIntoAssit().handleActivityDestroyed(activity);
    }

}
