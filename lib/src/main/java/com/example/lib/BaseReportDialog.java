package com.example.lib;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 用于埋点统一的dialog
 */
public abstract class BaseReportDialog extends Dialog {
    public BaseReportDialog(@NonNull Context context) {
        super(context);
    }

    public BaseReportDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseReportDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
