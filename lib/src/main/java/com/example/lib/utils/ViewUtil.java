/*
 * Created by zhangxiangwei on 2019/12/09.
 * Copyright 2015－2021 Sensors Data Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.lib.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.LruCache;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.ToggleButton;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class ViewUtil {

    private static boolean sHaveCustomRecyclerView = false;
    private static boolean sHaveRecyclerView = haveRecyclerView();
    private static Method sRecyclerViewGetChildAdapterPositionMethod;
    private static Class sRecyclerViewClass;
    private static LruCache<Class, String> sClassNameCache;
    private static SparseArray sViewCache;

    /**
     * 获取 class name
     */
    private static String getCanonicalName(Class clazz) {
        String name = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            if (sClassNameCache == null) {
                sClassNameCache = new LruCache<Class, String>(100);
            }
            name = sClassNameCache.get(clazz);
        }
        if (TextUtils.isEmpty(name)) {
            name = clazz.getCanonicalName();
            if (TextUtils.isEmpty(name)) {
                name = "Anonymous";
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                synchronized (ViewUtil.class) {
                    sClassNameCache.put(clazz, name);
                }
            }
            checkCustomRecyclerView(clazz, name);
        }
        return name;
    }

    private static boolean instanceOfSupportViewPager(Object view) {
        Class clazz;
        try {
            clazz = Class.forName("android.support.v4.view.ViewPager");
        } catch (ClassNotFoundException e) {
            return false;
        }
        return clazz.isInstance(view);
    }

    private static boolean instanceOfAndroidXViewPager(Object view) {
        Class clazz;
        try {
            clazz = Class.forName("androidx.viewpager.widget.ViewPager");
        } catch (ClassNotFoundException e2) {
            return false;
        }
        return clazz.isInstance(view);
    }

    public static boolean instanceOfRecyclerView(Object view) {
        Class clazz;
        try {
            clazz = Class.forName("android.support.v7.widget.RecyclerView");
        } catch (ClassNotFoundException th) {
            try {
                clazz = Class.forName("androidx.recyclerview.widget.RecyclerView");
            } catch (ClassNotFoundException e2) {
                return sHaveCustomRecyclerView
                        && view != null
                        && sRecyclerViewClass != null
                        && sRecyclerViewClass.isAssignableFrom(view.getClass());
            }
        }
        return clazz.isInstance(view);
    }

    private static boolean instanceOfSupportSwipeRefreshLayout(Object view) {
        Class clazz;
        try {
            clazz = Class.forName("android.support.v4.widget.SwipeRefreshLayout");
        } catch (ClassNotFoundException th) {
            try {
                clazz = Class.forName("androidx.swiperefreshlayout.widget.SwipeRefreshLayout");
            } catch (ClassNotFoundException e2) {
                return false;
            }
        }
        return clazz.isInstance(view);
    }

    static boolean instanceOfSupportListMenuItemView(Object view) {
        Class clazz = null;
        try {
            clazz = Class.forName("android.support.v7.view.menu.ListMenuItemView");
            return clazz.isInstance(view);
        } catch (ClassNotFoundException th) {
            //ignored
        }
        return false;
    }

    static boolean instanceOfAndroidXListMenuItemView(Object view) {
        Class clazz = null;
        try {
            clazz = Class.forName("androidx.appcompat.view.menu.ListMenuItemView");
            return clazz.isInstance(view);
        } catch (ClassNotFoundException th) {
            //ignored
        }
        return false;
    }

    static boolean instanceOfBottomNavigationItemView(Object view) {
        Class clazz = null;
        try {
            clazz = Class.forName("com.google.android.material.bottomnavigation.BottomNavigationItemView");
            return clazz.isInstance(view);
        } catch (ClassNotFoundException e) {
            //ignored
        }
        return false;
    }

    static boolean instanceOfActionMenuItem(Object view) {
        Class clazz = null;
        try {
            clazz = Class.forName("androidx.appcompat.view.menu.ActionMenuItem");
            return clazz.isInstance(view);
        } catch (ClassNotFoundException e) {
            //ignored
        }
        return false;
    }

    static boolean instanceOfToolbar(Object view) {
        Class clazz = null;
        try {
            clazz = Class.forName("androidx.appcompat.widget.Toolbar");
        } catch (ClassNotFoundException e) {
            try {
                clazz = Class.forName("android.support.v7.widget.Toolbar");
            } catch (ClassNotFoundException e2) {
                try {
                    clazz = Class.forName("android.widget.Toolbar");
                } catch (ClassNotFoundException e3) {
                    return false;
                }
            }
        }
        return clazz.isInstance(view);
    }

    private static boolean instanceOfNavigationView(Object view) {
        Class clazz = null;
        try {
            clazz = Class.forName("android.support.design.widget.NavigationView");
        } catch (ClassNotFoundException th) {
            try {
                clazz = Class.forName("com.google.android.material.navigation.NavigationView");
            } catch (ClassNotFoundException e2) {
                return false;
            }
        }
        return clazz.isInstance(view);
    }


    public static boolean instanceOfWebView(Object view) {
        return view instanceof WebView || instanceOfX5WebView(view) || instanceOfUCWebView(view);
    }

    private static boolean instanceOfX5WebView(Object view) {
        try {
            Class<?> clazz = Class.forName("com.tencent.smtt.sdk.WebView");
            return clazz.isInstance(view);
        } catch (ClassNotFoundException th) {
            //ignored
        }
        return false;
    }

    private static boolean instanceOfUCWebView(Object view) {
        try {
            Class<?> clazz = Class.forName("com.alipay.mobile.nebulauc.impl.UCWebView$WebViewEx");
            return clazz.isInstance(view);
        } catch (ClassNotFoundException th) {
            //ignored
        }
        return false;
    }

    /**
     * position RecyclerView item
     */
    private static int getChildAdapterPositionInRecyclerView(View childView, ViewGroup parentView) {
        if (instanceOfRecyclerView(parentView)) {
            try {
                sRecyclerViewGetChildAdapterPositionMethod = parentView.getClass().getDeclaredMethod("getChildAdapterPosition", new Class[]{View.class});
            } catch (NoSuchMethodException e) {
                //ignored
            }
            if (sRecyclerViewGetChildAdapterPositionMethod == null) {
                try {
                    sRecyclerViewGetChildAdapterPositionMethod = parentView.getClass().getDeclaredMethod("getChildPosition", new Class[]{View.class});
                } catch (NoSuchMethodException e2) {
                    //ignored
                }
            }
            try {
                if (sRecyclerViewGetChildAdapterPositionMethod != null) {
                    Object object = sRecyclerViewGetChildAdapterPositionMethod.invoke(parentView, childView);
                    if (object != null) {
                        return (Integer) object;
                    }
                }
            } catch (IllegalAccessException e) {
                //ignored
            } catch (InvocationTargetException e) {
                //ignored
            }
        } else if (sHaveCustomRecyclerView) {
            return invokeCRVGetChildAdapterPositionMethod(parentView, childView);
        }
        return -1;
    }

    private static int getCurrentItem(View view) {
        try {
            Method method = view.getClass().getMethod("getCurrentItem");
            Object object = method.invoke(view);
            if (object != null) {
                return (Integer) object;
            }
        } catch (IllegalAccessException e) {
            //ignored
        } catch (InvocationTargetException e2) {
            //ignored
        } catch (NoSuchMethodException e) {
            //ignored
        }
        return -1;
    }

    static Object getItemData(View view) {
        try {
            Method method = view.getClass().getMethod("getItemData");
            return method.invoke(view);
        } catch (IllegalAccessException e) {
            //ignored
        } catch (InvocationTargetException e2) {
            //ignored
        } catch (NoSuchMethodException e) {
            //ignored
        }
        return null;
    }

    private static boolean haveRecyclerView() {
        try {
            Class.forName("android.support.v7.widget.RecyclerView");
            return true;
        } catch (ClassNotFoundException th) {
            try {
                Class.forName("androidx.recyclerview.widget.RecyclerView");
                return true;
            } catch (ClassNotFoundException e2) {
                return false;
            }
        }
    }

    @TargetApi(12)
    private static void checkCustomRecyclerView(Class<?> viewClass, String viewName) {
        if (!sHaveRecyclerView && !sHaveCustomRecyclerView && viewName != null && viewName.contains("RecyclerView")) {
            try {
                if (findRecyclerInSuper(viewClass) != null && sRecyclerViewGetChildAdapterPositionMethod != null) {
                    sRecyclerViewClass = viewClass;
                    sHaveCustomRecyclerView = true;
                }
            } catch (Exception e) {
                //ignored
            }
        }
    }

    private static Class<?> findRecyclerInSuper(Class<?> viewClass) {
        while (viewClass != null && !viewClass.equals(ViewGroup.class)) {
            try {
                sRecyclerViewGetChildAdapterPositionMethod = viewClass.getDeclaredMethod("getChildAdapterPosition", new Class[]{View.class});
            } catch (NoSuchMethodException e) {
                //ignored
            }
            if (sRecyclerViewGetChildAdapterPositionMethod == null) {
                try {
                    sRecyclerViewGetChildAdapterPositionMethod = viewClass.getDeclaredMethod("getChildPosition", new Class[]{View.class});
                } catch (NoSuchMethodException e2) {
                    //ignored
                }
            }
            if (sRecyclerViewGetChildAdapterPositionMethod != null) {
                return viewClass;
            }
            viewClass = viewClass.getSuperclass();
        }
        return null;
    }

    private static int invokeCRVGetChildAdapterPositionMethod(View customRecyclerView, View childView) {
        try {
            if (customRecyclerView.getClass() == sRecyclerViewClass) {
                return (Integer) sRecyclerViewGetChildAdapterPositionMethod.invoke(customRecyclerView, new Object[]{childView});
            }
        } catch (IllegalAccessException e) {
            //ignored
        } catch (InvocationTargetException e2) {
            //ignored
        }
        return -1;
    }

    private static boolean isListView(View view) {
        return (view instanceof AdapterView) || ViewUtil.instanceOfRecyclerView(view) || ViewUtil.instanceOfAndroidXViewPager(view) || ViewUtil.instanceOfSupportViewPager(view);
    }

    @SuppressLint("NewApi")
    public static boolean isViewSelfVisible(View view) {
        if (view == null || view.getWindowVisibility() == View.GONE) {
            return false;
        }

        if (view.getWidth() <= 0 || view.getHeight() <= 0 || view.getAlpha() <= 0.0f || !view.getLocalVisibleRect(new Rect())) {
            return false;
        }
        if ((view.getVisibility() == View.VISIBLE || view.getAnimation() == null || !view.getAnimation().getFillAfter()) && view.getVisibility() != View.VISIBLE) {
            return false;
        }
        return true;
    }

    private static boolean viewVisibilityInParents(View view) {
        if (view == null) {
            return false;
        }
        if (!ViewUtil.isViewSelfVisible(view)) {
            return false;
        }
        ViewParent viewParent = view.getParent();
        while (viewParent instanceof View) {
            if (!ViewUtil.isViewSelfVisible((View) viewParent)) {
                return false;
            }
            viewParent = viewParent.getParent();
            if (viewParent == null) {
                return false;
            }
        }
        return true;
    }

    @SuppressLint("NewApi")
    public static void invalidateLayerTypeView(View[] views) {
        for (View view : views) {
            if (ViewUtil.viewVisibilityInParents(view) && view.isHardwareAccelerated()) {
                checkAndInvalidate(view);
                if (view instanceof ViewGroup) {
                    invalidateViewGroup((ViewGroup) view);
                }
            }
        }
    }

    @SuppressLint("NewApi")
    private static void checkAndInvalidate(View view) {
        if (view.getLayerType() != 0) {
            view.invalidate();
        }
    }

    private static void invalidateViewGroup(ViewGroup viewGroup) {
        for (int index = 0; index < viewGroup.getChildCount(); index++) {
            View child = viewGroup.getChildAt(index);
            if (ViewUtil.isViewSelfVisible(child)) {
                checkAndInvalidate(child);
                if (child instanceof ViewGroup) {
                    invalidateViewGroup((ViewGroup) child);
                }
            }
        }
    }


    private static int getViewPosition(View view, int viewIndex) {
        int idx = viewIndex;
        if (view.getParent() != null && (view.getParent() instanceof ViewGroup)) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (ViewUtil.instanceOfAndroidXViewPager(parent) || ViewUtil.instanceOfSupportViewPager(parent)) {
                idx = ViewUtil.getCurrentItem(parent);
            } else if (parent instanceof AdapterView) {
                idx += ((AdapterView) parent).getFirstVisiblePosition();
            } else if (ViewUtil.instanceOfRecyclerView(parent)) {
                int adapterPosition = ViewUtil.getChildAdapterPositionInRecyclerView(view, parent);
                if (adapterPosition >= 0) {
                    idx = adapterPosition;
                }
            }
        }
        return idx;
    }


    public static void clear() {
        if (sViewCache != null) {
            sViewCache.clear();
        }
    }

    static boolean isTrackEvent(View view, boolean isFromUser) {
        if (view instanceof CheckBox) {
            if (!isFromUser) {
                return false;
            }
        } else if (view instanceof RadioButton) {
            if (!isFromUser) {
                return false;
            }
        } else if (view instanceof ToggleButton) {
            if (!isFromUser) {
                return false;
            }
        } else if (view instanceof CompoundButton) {
            if (!isFromUser) {
                return false;
            }
        }
        if (view instanceof RatingBar) {
            if (!isFromUser) {
                return false;
            }
        }
        return true;
    }
}