package com.bqteam.appforlearn.function.boardcast;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.bqteam.appforlearn.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author charles
 */
public class BroadcastActivity extends AppCompatActivity {
    public static final String DOWNLOAD_FILTER = "downloadFilter";
    public static final String DOWNLOAD_PROGRESS = "downloadProgress";

    private ProgressDialog progressDialog;

    private DownloadBroadcast downloadBroadcast;
    private BroadcastService.MyBinder myBinder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (BroadcastService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();
    }

    @OnClick({R.id.register_btn, R.id.unregister_btn, R.id.start_btn, R.id.stop_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register_btn:
                registerBroadcast();
                break;
            case R.id.unregister_btn:
                unregisterBroadcast();
                break;
            case R.id.start_btn:
                startDownload();
                break;
            case R.id.stop_btn:
                stopDownload();
                break;
            default:
                break;
        }
    }

    private void showProgress(int progress) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("正在下载，请稍候");
            progressDialog.show();
        }
        String message = "正在下载" + progress + "%";
        progressDialog.setMessage(message);
    }

    private void registerBroadcast() {
        downloadBroadcast = new DownloadBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DOWNLOAD_FILTER);
        registerReceiver(downloadBroadcast, intentFilter);

        Intent bindIntent = new Intent(BroadcastActivity.this, BroadcastService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    private void unregisterBroadcast() {
        unregisterReceiver(downloadBroadcast);
    }

    private void startDownload() {
        myBinder.startDownload();
    }

    private void stopDownload() {
        myBinder.stopDownload();
        try {
            unbindService(serviceConnection);
        } catch (IllegalArgumentException ex) {
            Log.d("IllegalArgument", ex.toString());
        }
    }

    class DownloadBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int progress = intent.getIntExtra(DOWNLOAD_PROGRESS, 0);

            if (progress < 100) {
                showProgress(progress);
            } else {
                String message = "下载完毕";
                progressDialog.setMessage(message);
            }
        }
    }
}
