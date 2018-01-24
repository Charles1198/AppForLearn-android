package com.bqteam.appforlearn.function.maxst;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bqteam.appforlearn.R;
import com.bqteam.appforlearn.function.maxst.util.SampleUtil;
import com.bqteam.appforlearn.util.LogUtil;
import com.maxst.ar.CameraDevice;
import com.maxst.ar.MaxstAR;
import com.maxst.ar.ResultCode;
import com.maxst.ar.TrackerManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author charles
 */
public class MaxstActivity extends ARActivity {

    @BindView(R.id.ar_surfaceView)
    GLSurfaceView arSurfaceView;
    @BindView(R.id.ar_tv)
    TextView arTv;

    private ImageTrackerRenderer imageTargetRenderer;
    private GLSurfaceView glSurfaceView;
    private int preferCameraResolution = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maxst);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);

        imageTargetRenderer = new ImageTrackerRenderer();
        glSurfaceView = findViewById(R.id.ar_surfaceView);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(imageTargetRenderer);
        imageTargetRenderer.setOnRecognizeListener(recognized -> {
            if (recognized && arTv.getVisibility() == View.GONE) {
                arTv.setVisibility(View.VISIBLE);
            }
            if (!recognized && arTv.getVisibility() == View.VISIBLE) {
                arTv.setVisibility(View.GONE);
            }
        });

        TrackerManager.getInstance().addTrackerData("ImageTarget/zhonghang.2dmap", true);
        TrackerManager.getInstance().addTrackerData("ImageTarget/zhonghang1.2dmap", true);
        TrackerManager.getInstance().addTrackerData("ImageTarget/zhonghang2.2dmap", true);
        TrackerManager.getInstance().loadTrackerData();

        preferCameraResolution = getSharedPreferences(SampleUtil.PREF_NAME,
                Activity.MODE_PRIVATE).getInt(SampleUtil.PREF_KEY_CAM_RESOLUTION, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        glSurfaceView.onResume();
        TrackerManager.getInstance().startTracker(TrackerManager.TRACKER_TYPE_IMAGE);

        ResultCode resultCode = ResultCode.Success;
        switch (preferCameraResolution) {
            case 0:
                resultCode = CameraDevice.getInstance().start(0, 640, 480);
                break;
            case 1:
                resultCode = CameraDevice.getInstance().start(0, 1280, 720);
                break;
            default:
                break;
        }

        if (resultCode != ResultCode.Success) {
            Toast.makeText(this, R.string.camera_open_fail, Toast.LENGTH_SHORT).show();
            finish();
        }

        MaxstAR.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        glSurfaceView.onPause();

        TrackerManager.getInstance().stopTracker();
        CameraDevice.getInstance().stop();
        MaxstAR.onPause();
    }
}
