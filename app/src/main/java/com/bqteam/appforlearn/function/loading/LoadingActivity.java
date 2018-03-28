package com.bqteam.appforlearn.function.loading;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bqteam.appforlearn.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author charles
 */
public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        LinkedList linkedList = new LinkedList();
        List list = new ArrayList();
    }
}
