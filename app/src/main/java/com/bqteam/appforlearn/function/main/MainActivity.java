package com.bqteam.appforlearn.function.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.bqteam.appforlearn.R;
import com.bqteam.appforlearn.function.boardcast.BroadcastActivity;
import com.bqteam.appforlearn.function.canvas.CanvasActivity;
import com.bqteam.appforlearn.function.mode.DesignModeActivity;
import com.bqteam.appforlearn.function.refresh_loadmore.RefreshLoadmoreActivity;
import com.bqteam.appforlearn.function.service.ServiceActivity;

import java.util.List;

/**
 * @author charles
 */
public class MainActivity extends AppCompatActivity {
    private String[] entranceList = {"上拉加载与下拉刷新", "设计模式", "Canvas", "Service", "Broadcast"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    private void jumpToActivity(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, RefreshLoadmoreActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, DesignModeActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, CanvasActivity.class));
                break;
            case 3:
                startActivity(new Intent(this, ServiceActivity.class));
                break;
            case 4:
                startActivity(new Intent(this, BroadcastActivity.class));
                break;
            default:
                break;
        }
    }
}
