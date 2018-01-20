package com.bqteam.appforlearn.function.main;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.bqteam.appforlearn.R;
import com.bqteam.appforlearn.function.boardcast.BroadcastActivity;
import com.bqteam.appforlearn.function.canvas.CanvasActivity;
import com.bqteam.appforlearn.function.mode.DesignModeActivity;
import com.bqteam.appforlearn.function.panorama.PanoramaActivity;
import com.bqteam.appforlearn.function.refresh_loadmore.RefreshLoadmoreActivity;
import com.bqteam.appforlearn.function.service.ServiceActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author charles
 */
public class MainActivity extends AppCompatActivity {
    private String[] entranceList;
    private List<Class> activityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.main_recyclerView);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);

        EntranceAdapter adapter = new EntranceAdapter(this, entranceList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> jumpToActivity(position));
    }

    private void initData() {
        entranceList = new String[]{"上拉加载与下拉刷新", "设计模式", "Canvas", "Service", "Broadcast", "全景测试"};
        activityList.add(RefreshLoadmoreActivity.class);
        activityList.add(DesignModeActivity.class);
        activityList.add(CanvasActivity.class);
        activityList.add(ServiceActivity.class);
        activityList.add(BroadcastActivity.class);
        activityList.add(PanoramaActivity.class);
    }

    private void jumpToActivity(int position) {
        startActivity(new Intent(this, activityList.get(position)));
    }
}

