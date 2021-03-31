package com.example.lib;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lib.node.PageNode;

import java.util.List;

public class PathInfoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    NodePath nodePath = NodePath.getInstance();
    PageNode[] pageNodes = new PageNode[nodePath.nodeQueue.size()];
    private List<PageNode> pageNodePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_info);
        pageNodePath = PageNodeManager.getInstance().getPageNodePath();
        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                TextView textView = new TextView(PathInfoActivity.this);
                textView.setGravity(Gravity.CENTER);
                RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
                textView.setBackgroundColor(Color.GRAY);
                layoutParams.topMargin = 20;
                textView.setLayoutParams(layoutParams);
                return new RecyclerView.ViewHolder(textView) {
                };
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                TextView tv = (TextView) holder.itemView;
                tv.setText(pageNodePath.get(position).name);
            }

            @Override
            public int getItemCount() {
                return pageNodePath.size();
            }
        });
    }
}
