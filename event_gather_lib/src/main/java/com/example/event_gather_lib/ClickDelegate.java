package com.example.event_gather_lib;

import android.annotation.SuppressLint;
import android.print.PrinterId;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityEvent;

import com.example.lib.EventConstants;
import com.example.lib.ReportLog;
import com.example.lib.event.ViewClickEvent;

import java.security.acl.Group;

/**
 * @Author jacky.peng
 * @Date 2021/4/12 2:33 PM
 * @Version 1.0
 * <p>
 * 点击事件的辅助类
 */
public class ClickDelegate extends View.AccessibilityDelegate {
    final View rootView;

    public ClickDelegate(View view) {
        this.rootView = view;
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setDelegate(rootView);
            }
        });
    }

    @Override
    public void sendAccessibilityEvent(View host, int eventType) {
        super.sendAccessibilityEvent(host, eventType);
        if (eventType == AccessibilityEvent.TYPE_VIEW_CLICKED) {

            String viewPath = getViewPath(host);
            ViewClickEvent event = new ViewClickEvent.Builder().setActivityName(host.getContext().getClass().getCanonicalName()).setViewPath(viewPath).build();
            ReportLog.logD(EventConstants.VIEW_ON_CLICK_EVENT + "-->" + viewPath);
        }
    }

    private void setDelegate(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            if (view instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) view;
                for (int i = 0; i < group.getChildCount(); i++) {
                    setDelegate(group.getChildAt(i));
                }
            } else {
                if (view.isClickable()) {
                    view.setAccessibilityDelegate(this);
                }
            }
        }
    }


    /**
     * @param view
     * @return view在视图中的唯一id
     * DecorView[0]/LinearLayout[1]/FrameLayout[0]#2131230850/ActionBarOverlayLayout[0]#16908290/ContentFrameLayout[0]/ConstraintLayout[0]/LinearLayout[5]#2131231023/AppCompatButton
     */
    @SuppressLint("ResourceType")
    private String getViewPath(View view) {
        ViewParent parent = view.getParent();
        StringBuilder sb = new StringBuilder();
        View child = view;
        while (parent != null && parent instanceof ViewGroup) {
            StringBuilder tmp = new StringBuilder();
            if (child.getParent() instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) child.getParent();
                tmp.append(group.getClass().getSimpleName());
                tmp.append("[" + group.indexOfChild(child) + "]");
                if (child.getId() > 0) {
                    tmp.append("#");
                    tmp.append(child.getId());
                }
                tmp.append("/");
            }
            sb.insert(0, tmp.toString());
            child = (View) parent;
            parent = child.getParent();
        }
        sb.append(view.getClass().getSimpleName());
        if (child.getId() > 0) {
            sb.append("#");
            sb.append(child.getId());
        }
        return sb.toString();
    }
}
