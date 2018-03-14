package com.bqteam.appforlearn.function.sort;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bqteam.appforlearn.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author charles
 */
public class SortActivity extends AppCompatActivity {

    @BindView(R.id.sortView)
    SortView sortView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        ButterKnife.bind(this);

        toolbar.setOnMenuItemClickListener(item -> {
            performSort();
            return false;
        });
    }

    private void performSort() {

    }
}
