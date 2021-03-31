package com.example.lib;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * @Author jacky.peng
 * @Date 2021/3/18 3:35 PM
 * @Version 1.0
 */
public class BaseReportFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentTracker.getInstance().dispatchFragmentCreated(this, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentTracker.getInstance().dispatchFragmentResumed(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        FragmentTracker.getInstance().dispatchFragmentPaused(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        FragmentTracker.getInstance().dispatchFragmentAttached(context, this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        FragmentTracker.getInstance().dispatchFragmentVisibleHint(this,isVisibleToUser);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        FragmentTracker.getInstance().dispatchFragmentDetached(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FragmentTracker.getInstance().dispatchFragmentDestroy(this);
    }
}
