package com.example.reportdemo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lib.RecyclerViewExposureListener;
import com.example.lib.ViewUtil;
import com.example.lib.event.ViewClickEvent;
import com.example.lib.event.ViewExposureEvent;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    private FragmentA fragmentA;
    private FragmentB fragmentB;
    private Intent intent;

    AtomicInteger reportNum = new AtomicInteger(1000);
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.to_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);

                ViewClickEvent.Builder builder = new ViewClickEvent.Builder();
                builder.setViewPath(ViewUtil.getViewPath(v));

                FragmentActivity activity = (FragmentActivity) v.getContext();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                List<Fragment> fragments = fragmentManager.getFragments();
                for (int i = 0; i < fragments.size(); i++) {
                    Fragment fragment = fragments.get(i);
                    View view = fragment.getView();
                    Rect rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                    if (v.getLocalVisibleRect(rect)) {
                        builder.setFragmentName(fragment.getClass().getCanonicalName());
                        break;
                    }
                }
                builder.build().report();
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
//        startService(intent);


        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
//                while (reportNum.get() > 0) {
//                    Random random = new Random(100);
//                    boolean aBoolean = random.nextBoolean();
//                    long timeMillis = System.currentTimeMillis();
//                    if (aBoolean) {
//                        new PageLifeCycleEvent("test", "" + timeMillis).report();
//                    } else {
//                        new PageOnCreateEvent("test", "" + timeMillis).report();
//                    }
//                }
            }
        });

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                TextView textView = new TextView(MainActivity.this);
                textView.setBackgroundColor(Color.YELLOW);
                RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 100);
                params.bottomMargin = 10;
                textView.setGravity(Gravity.CENTER);
                textView.setLayoutParams(params);
                RecyclerView.ViewHolder viewHolder = new RecyclerView.ViewHolder(textView) {
                };
                return viewHolder;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                TextView textView = (TextView) holder.itemView;
                textView.setText(position + "");
            }

            @Override
            public int getItemCount() {
                return 100;
            }
        });
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnScrollListener(new RecyclerViewExposureListener() {

            @Override
            public void exposureView(View view, int index) {
                String viewPath = ViewUtil.getViewPath(view);
                ViewExposureEvent event = new ViewExposureEvent.Builder().setActivityName(view.getContext().getClass().getCanonicalName()).setViewPath(viewPath).build();
                event.report();
            }
        });

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL) {

        });

        new Thread(){

            @Override
            public void run() {

                Looper.prepare();

                Looper.loop();

            }
        }.start();
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
