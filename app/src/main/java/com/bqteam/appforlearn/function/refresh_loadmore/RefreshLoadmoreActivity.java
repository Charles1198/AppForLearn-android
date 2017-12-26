package com.bqteam.appforlearn.function.refresh_loadmore;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bqteam.appforlearn.R;

/**
 * @author charles
 */
public class RefreshLoadmoreActivity extends AppCompatActivity {
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private LinearLayout linTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_loadmore);

        linTest = findViewById(R.id.test);
        refreshLoadmoreLayout = findViewById(R.id.refreshLoadmoreLayout);
        refreshLoadmoreLayout.setOnRefreshListener(new RefreshLoadmoreLayout.OnRefreshListener() {
            @Override
            public void refresh() {
                toRefresh();
            }
        });
        refreshLoadmoreLayout.setOnLoadmoreListener(new RefreshLoadmoreLayout.OnLoadmoreListener() {
            @Override
            public void loadmore() {
                toLoadmore();
            }
        });
    }

    private void toRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLoadmoreLayout.refreshEnd();
            }
        }, 3000);
    }

    private void toLoadmore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLoadmoreLayout.loadmoreEnd(false);
                linTest.setVisibility(View.VISIBLE);
            }
        }, 3000);
    }
}
