package com.example.lib;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.lib.event.PageOnCreateEvent;
import com.example.lib.event.PageOnDestroyEvent;
import com.example.lib.node.ActivityNode;
import com.example.lib.node.FragmentNode;

import java.util.List;

/**
 * @Author jacky.peng
 * @Date 2021/3/18 10:56 AM
 * @Version 1.0
 */
public class ActivityLifecycleTracker {
    Application application;

    public ActivityLifecycleTracker(Application application) {
        assert application != null;
        this.application = application;

    }


    private String getActivityName(Activity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("activity can not be null");
        }
        return activity.getClass().getCanonicalName();
    }


    public void startTrack() {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
                String localClassName = getActivityName(activity);
                ActivityNode pageNode = new ActivityNode(localClassName);
                pageNode.createTime = SystemClock.uptimeMillis();
                new PageOnCreateEvent(localClassName, "" + SystemClock.currentThreadTimeMillis()).report();
                PageNodeManager.getInstance().addActivityNode(pageNode);
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                //上报页面信息
                //生成一个PageLifeCycle事件

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                String localClassName = getActivityName(activity);
                new PageOnDestroyEvent(localClassName, "" + SystemClock.currentThreadTimeMillis()).report();

                PageNodeManager.getInstance().onActivityPaused(activity.getClass().getCanonicalName());
                PageNodeManager.getInstance().removeActivityNode(localClassName);
            }
        });

        FragmentTracker.getInstance().registerFragmentLifecycleCallbacks(new FragmentTracker.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentCreated(Fragment fragment, Bundle savedInstanceState) {

            }

            @Override
            public void onFragmentResumed(Fragment fragment) {
                String localClassName = getFragmentIdentifier(fragment);
                FragmentNode fragmentNode = PageNodeManager.getInstance().findFragmentNode(localClassName, getActivityName(fragment.getActivity()));
                PageNodeManager.getInstance().updateHeadPoint(fragmentNode);
            }

            @Override
            public void onFragmentPaused(Fragment fragment) {

            }

            @Override
            public void onFragmentAttached(Context context, Fragment fragment) {
                String localClassName = getFragmentIdentifier(fragment);
                FragmentNode pageNode = new FragmentNode(localClassName);
                pageNode.createTime = SystemClock.uptimeMillis();
                String activityName = getActivityName(fragment.getActivity());
                String parentName = null;
                if (fragment.getParentFragment() != null) {
                    parentName = getFragmentIdentifier(fragment.getParentFragment());
                }

                PageNodeManager.getInstance().addFragmentNode(pageNode, parentName, activityName);

            }

            @Override
            public void onFragmentDetached(Fragment fragment) {

            }

            @Override
            public void onFragmentVisibleHint(Fragment fragment, boolean isVisibleToUser) {
                if (!isVisibleToUser) return;
                if (fragment.getActivity() == null) return;
                String fragmentIdentifier = getFragmentIdentifier(fragment);
                FragmentNode fragmentNode = PageNodeManager.getInstance().findFragmentNode(fragmentIdentifier, getActivityName(fragment.getActivity()));
                if (fragmentNode == null) {
                    throw new IllegalStateException("can not find node->" + fragmentIdentifier);
                }
                List<Fragment> fragments = fragment.getChildFragmentManager().getFragments();
                if (fragments != null && !fragments.isEmpty()) {
                    for (int i = 0; i < fragments.size(); i++) {
                        fragments.get(i).setUserVisibleHint(isVisibleToUser);
                    }
                } else {
                    fragmentNode.isVisible = true;
                    PageNodeManager.getInstance().updateHeadPoint(fragmentNode);
                }
            }

            @Override
            public void onFragmentDestroy(Fragment fragment) {
                FragmentActivity activity = fragment.getActivity();
                if (!activity.isDestroyed()) {
                    PageNodeManager.getInstance().onFragmentPaused(getFragmentIdentifier(fragment), fragment.getActivity().getClass().getCanonicalName());
                    PageNodeManager.getInstance().removeFragmentNode(getFragmentIdentifier(fragment), (getActivityName(fragment.getActivity())));
                }
            }
        });
    }


    public String getFragmentIdentifier(Fragment fragment) {
        if (fragment == null) return "";


        FragmentActivity activity = fragment.getActivity();
        String identifier = getActivityName(activity) + "::";
        Fragment parentFragment = fragment.getParentFragment();
        while (parentFragment != null) {
            identifier += getFragmentName(parentFragment) + "::";
            parentFragment = parentFragment.getParentFragment();
        }
        identifier += getFragmentName(fragment);
        return identifier;
    }

    private String getFragmentName(Fragment fragment) {
        return fragment.getClass().getCanonicalName();
    }
}
