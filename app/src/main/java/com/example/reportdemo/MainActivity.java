package com.example.reportdemo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.MessageQueue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.lib.FloatingAssitView;
import com.example.lib.PathInfoActivity;

import static android.content.Context.WINDOW_SERVICE;
import static android.os.Looper.getMainLooper;

public class MainActivity extends AppCompatActivity {

    private FragmentA fragmentA;
    private FragmentB fragmentB;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.to_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.to_third).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });
        fragmentA = new FragmentA();
        fragmentB = new FragmentB();
        getSupportFragmentManager().beginTransaction().add(R.id.content, fragmentA).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.content2, fragmentB).commit();

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = getSupportFragmentManager().getFragments().size();
                if (size == 2) {
                    return;
                } else if (size == 1) {
                    boolean added = fragmentA.isAdded();
                    if (added) {
                        getSupportFragmentManager().beginTransaction().show(fragmentA);
                    } else {
                        getSupportFragmentManager().beginTransaction().add(R.id.content, fragmentA).commit();
                    }
                } else {
                    boolean added = fragmentB.isAdded();
                    if (added) {
                        getSupportFragmentManager().beginTransaction().show(fragmentB);
                    } else {
                        getSupportFragmentManager().beginTransaction().add(R.id.content2, fragmentB).commit();

                    }
                }
            }
        });

        findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = getSupportFragmentManager().getFragments().size();
                if (size == 2) {
                    getSupportFragmentManager().beginTransaction().remove(fragmentA).commit();
                } else if (size == 1) {
                    getSupportFragmentManager().beginTransaction().remove(fragmentB).commit();
                }
            }
        });

        intent = new Intent(this, FloatingService.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        MessageQueue.IdleHandler idleHandler = new MessageQueue.IdleHandler() {

            @Override
            public boolean queueIdle() {
                return false;
            }
        };
        getMainLooper().getQueue().addIdleHandler(idleHandler);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                showFloatingView();
//            }
//        }, 1000);
    }


}
