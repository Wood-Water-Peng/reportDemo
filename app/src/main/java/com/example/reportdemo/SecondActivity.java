package com.example.reportdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lib.BaseReportActivity;

public class SecondActivity extends BaseReportActivity {

    private FragmentA fragmentA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViewById(R.id.to_third).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });

        fragmentA = new FragmentA();
        getSupportFragmentManager().beginTransaction().add(R.id.content, fragmentA).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
