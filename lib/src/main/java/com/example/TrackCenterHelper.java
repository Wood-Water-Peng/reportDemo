package com.example;

import android.view.View;

import com.example.lib.core.ReportHandler;
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
}
