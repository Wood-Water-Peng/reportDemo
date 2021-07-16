package com.example.reportdemo.dialogs;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.lib.BaseReportDialog;
import com.example.reportdemo.R;

public class TestDialog extends BaseReportDialog {
    public TestDialog(@NonNull Context context) {
        this(context,0);
    }

    public TestDialog(@NonNull Context context, int themeResId) {
        super(context,themeResId);
        setContentView(com.example.reportdemo.R.layout.dialog_test);
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
