package com.example.lib;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.lib.ReportLog;

/**
 * @Author jacky.peng
 * @Date 2021/4/12 5:49 PM
 * @Version 1.0
 * <p>
 * 监听RecyclerView的滑动事件，并在滑动停下时确定item的曝光
 */
public abstract class RecyclerViewExposureListener extends RecyclerView.OnScrollListener {
    Rect hitRect = new Rect();
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    LinearLayoutManager linearLayoutManager;
    boolean _hasInit = false;

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (!_hasInit) {
            _hasInit = true;
            calcExposureView(recyclerView);
        }
    }

    private void calcExposureView(RecyclerView recyclerView) {
        //确定RecyclerView的显示区域
        recyclerView.getHitRect(hitRect);
        //确定显示的child的范围
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            linearLayoutManager = (LinearLayoutManager) layoutManager;
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
            //获取范围内的所有view，并计算出他们的曝光比例
            for (int i = firstVisibleItemPosition; i < lastVisibleItemPosition; i++) {
                handleLinearLayoutExposure(i);
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] range = getStaggerViewRange();
            for (int i = 0; i < range.length; i++) {
                handleStaggerLayoutExposure(range[i]);
            }
        }
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        ReportLog.logD("onScrollStateChanged newState->" + newState);
        if (newState != 0) return;
        calcExposureView(recyclerView);
    }

    /**
     * 返回可见的view集合
     *
     * @return
     */
    private int[] getStaggerViewRange() {
        int[] startSpan = new int[staggeredGridLayoutManager.getSpanCount()];
        int[] endSpan = new int[staggeredGridLayoutManager.getSpanCount()];

        staggeredGridLayoutManager.findFirstVisibleItemPositions(startSpan);
        staggeredGridLayoutManager.findLastVisibleItemPositions(endSpan);

        int min = startSpan[0];
        for (int i = 1; i < startSpan.length; i++) {
            min = Math.min(min, startSpan[i]);
        }
        int max = endSpan[endSpan.length - 1];
        for (int i = 0; i < endSpan.length - 1; i++) {
            max = Math.max(max, endSpan[i]);
        }
        int[] result = new int[max - min + 1];
        for (int i = 0; i < result.length; i++) {
            result[i] = min + i;
        }
        return result;
    }


    //处理瀑布流布局中的曝光事件
    private void handleStaggerLayoutExposure(int index) {
        View viewByPosition = staggeredGridLayoutManager.findViewByPosition(index);
        if (viewByPosition == null || !viewByPosition.getLocalVisibleRect(hitRect)) {
            return;
        }
        if (getVerticalExposurePercentage(viewByPosition) > 50) {
//            ReportLog.logD("exposureView index->" + index);
            exposureView(viewByPosition, index);
        }

    }


    //处理线性布局中的曝光事件
    private void handleLinearLayoutExposure(int index) {
        View viewByPosition = linearLayoutManager.findViewByPosition(index);
        if (viewByPosition == null || !viewByPosition.getLocalVisibleRect(hitRect)) {
            return;
        }
        int orientation = linearLayoutManager.getOrientation();
        if (orientation == LinearLayoutManager.VERTICAL) {
            if (getVerticalExposurePercentage(viewByPosition) > 50) {
                ReportLog.logD("exposureView index->" + index);
                exposureView(viewByPosition, index);
            }
        }

    }

    public abstract void exposureView(View view, int index);


    private int getVerticalExposurePercentage(View view) {
        Rect rect = new Rect();
        view.getLocalVisibleRect(rect);
        int height = view.getHeight();
        int percents;
        if (rect.top == 0 && rect.bottom == height) {
            percents = 100;
        } else if (rect.top > 0) {
            percents = (height - rect.top) * 100 / height;
        } else {
            percents = rect.bottom * 100 / height;
        }
        return percents;
    }
}
