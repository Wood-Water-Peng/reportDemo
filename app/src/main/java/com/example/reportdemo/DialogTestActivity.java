package com.example.reportdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lib.BaseReportActivity;
import com.example.reportdemo.dialogs.TestDialog;

public class DialogTestActivity extends BaseReportActivity {

    private FragmentA fragmentA;
    private TestDialog testDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_test);
        findViewById(R.id.show_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testDialog = new TestDialog(DialogTestActivity.this);
                testDialog.setOnShowListener(new MyShowListener());
                testDialog.setOnDismissListener(new MyDismissListener());
                testDialog.show();
            }
        });

        findViewById(R.id.alert_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(DialogTestActivity.this).create();
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "positive", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
            }
        });

    }


    class MyShowListener implements DialogInterface.OnShowListener {

        @Override
        public void onShow(DialogInterface dialog) {
        }
    }

    class MyDismissListener implements DialogInterface.OnDismissListener {


        @Override
        public void onDismiss(DialogInterface dialog) {

        }
    }
}
