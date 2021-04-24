package com.example;

import android.os.Bundle;
import android.view.View;

import com.example.lib.core.ReportHandler;
import com.example.lib.event.FragmentEvent;
import com.example.lib.event.ViewClickEvent;

///
///@author jacky.peng on
/// 收集事件的入口类
///
public class TrackCenterHelper {
    //切勿修改！！！该方法可能被修改后的class文件调用
    public static void onViewClicked(View view) {
        System.out.println("TrackCenterHelper onViewClicked---");
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
        System.out.println("trackFragmentResume object--->" + object.getClass().getCanonicalName());
        System.out.println("trackOnFragmentViewCreated object--->" + object.getClass().getCanonicalName());
        FragmentEvent fragmentEvent = new FragmentEvent.Builder().setTag((FragmentEvent.NAME_ON_VIEW_RESUMED)).setFragmentName(object.getClass().getCanonicalName()).build();
        fragmentEvent.report();

    }

    public static void trackFragmentSetUserVisibleHint(Object object, boolean flag) {
        System.out.println("trackFragmentSetUserVisibleHint object--->" + object.getClass().getCanonicalName());
    }

    public static void trackOnHiddenChanged(Object object, boolean onHiddenChanged) {
        System.out.println("trackOnHiddenChanged object--->" + object.getClass().getCanonicalName());
    }

    public static void trackOnFragmentViewCreated(Object object, View view, Bundle bundle) {
        System.out.println("trackOnFragmentViewCreated object--->" + object.getClass().getCanonicalName());
        FragmentEvent fragmentEvent = new FragmentEvent.Builder().setTag((FragmentEvent.NAME_ON_VIEW_CREATED)).setActivityName(view.getContext().getClass().getCanonicalName()).setFragmentName(object.getClass().getCanonicalName()).build();
        fragmentEvent.report();
    }


}
