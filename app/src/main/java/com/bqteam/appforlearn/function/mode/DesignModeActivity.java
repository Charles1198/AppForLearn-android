package com.bqteam.appforlearn.function.mode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bqteam.appforlearn.R;
import com.bqteam.appforlearn.function.main.EntranceAdapter;
import com.bqteam.appforlearn.function.mode.observer.ModeObserverActivity;

/**
 * @author charles
 */
public class DesignModeActivity extends AppCompatActivity {
    private String[] entranceList = {"观察者模式"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_mode);

        initView();
    }

    private void initView() {
        RecyclerView recyclerView = findViewById(R.id.designMode_recyclerView);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);

        EntranceAdapter adapter = new EntranceAdapter(this, entranceList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> jumpToActivity(position));
    }

    private void jumpToActivity(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(DesignModeActivity.this, ModeObserverActivity.class));
                break;
            default:
                break;
        }
    }
}
