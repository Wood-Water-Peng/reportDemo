package com.example.lib;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

/**
 * @Author jacky.peng
 * @Date 2021/3/18 3:43 PM
 * @Version 1.0
 */
public class FragmentTracker {
    FragmentLifecycleCallbacks callbacks;

    public static FragmentTracker getInstance() {
        return FragmentTrackerHolder.INSTANCE;
    }

    private static class FragmentTrackerHolder {
        static final FragmentTracker INSTANCE = new FragmentTracker();
    }

    public void registerFragmentLifecycleCallbacks(FragmentTracker.FragmentLifecycleCallbacks callback) {
        this.callbacks = callback;
    }

    public interface FragmentLifecycleCallbacks {
        void onFragmentCreated(Fragment fragment, Bundle savedInstanceState);

        void onFragmentDestroy(Fragment fragment);

        void onFragmentResumed(Fragment fragment);

        void onFragmentPaused(Fragment fragment);

        void onFragmentAttached(Context context, Fragment fragment);

        void onFragmentDetached(Fragment fragment);

        void onFragmentVisibleHint(Fragment fragment, boolean isVisibleToUser);
    }

    /* package */ void dispatchFragmentCreated(Fragment fragment, Bundle savedInstanceState) {
        if (callbacks != null) {
            callbacks.onFragmentCreated(fragment, savedInstanceState);
        }
    }

    /* package */ void dispatchFragmentResumed(Fragment fragment) {
        if (callbacks != null) {
            callbacks.onFragmentResumed(fragment);
        }
    }

    /* package */ void dispatchFragmentPaused(Fragment fragment) {
        if (callbacks != null) {
            callbacks.onFragmentPaused(fragment);
        }
    }

    /* package */ void dispatchFragmentDestroy(Fragment fragment) {
        if (callbacks != null) {
            callbacks.onFragmentDestroy(fragment);
        }
    }

    /* package */ void dispatchFragmentDetached(Fragment fragment) {
        if (callbacks != null) {
            callbacks.onFragmentDetached(fragment);
        }
    }

    /* package */ void dispatchFragmentAttached(Context context, Fragment fragment) {
        if (callbacks != null) {
            callbacks.onFragmentAttached(context, fragment);
        }
    }

    /* package */
    void dispatchFragmentVisibleHint(Fragment fragment, boolean isVisibleToUser) {
        if (callbacks != null) {
            callbacks.onFragmentVisibleHint(fragment, isVisibleToUser);
        }
    }
}
