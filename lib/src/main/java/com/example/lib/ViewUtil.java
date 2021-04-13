package com.example.lib;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * @Author jacky.peng
 * @Date 2021/4/13 2:12 PM
 * @Version 1.0
 */
public class ViewUtil {
    /**
     * @param view
     * @return view在视图中的唯一id
     * DecorView[0]/LinearLayout[1]/FrameLayout[0]#2131230850/ActionBarOverlayLayout[0]#16908290/ContentFrameLayout[0]/ConstraintLayout[0]/LinearLayout[5]#2131231023/AppCompatButton
     */
    @SuppressLint("ResourceType")
    public static String getViewPath(View view) {
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
