package com.bqteam.appforlearn.function.panorama;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.bqteam.appforlearn.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author charles
 */
public class PanoramaActivity extends AppCompatActivity {
    @BindView(R.id.panoramaView)
    MyGlPanoramaView panoramaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panorama);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);

        panoramaView.setPanoramaImage(R.drawable.panorama_test);
        panoramaView.setTarget(1500, 1100);
    }
}
