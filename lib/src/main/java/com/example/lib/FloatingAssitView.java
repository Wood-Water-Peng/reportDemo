package com.example.lib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Author jacky.peng
 * @Date 2021/3/18 2:00 PM
 * @Version 1.0
 */
public class FloatingAssitView extends FrameLayout {
    public FloatingAssitView(@NonNull Context context) {
        this(context,null);
    }

    public FloatingAssitView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FloatingAssitView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_floating_assitor, this, true);
    }
}
