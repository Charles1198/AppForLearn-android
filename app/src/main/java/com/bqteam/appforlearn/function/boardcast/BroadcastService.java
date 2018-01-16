package com.bqteam.appforlearn.function.boardcast;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author charles
 * @date 2018/1/16
 */

public class BroadcastService extends Service {
    private MyBinder myBinder = new MyBinder();
    private Timer timer = new Timer();
    private int progress = 0;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public void sendBroadcast(int progress) {
        Intent intent = new Intent(BroadcastActivity.DOWNLOAD_FILTER);
        intent.putExtra(BroadcastActivity.DOWNLOAD_PROGRESS, progress);
        sendBroadcast(intent);
    }

    class MyBinder extends Binder {
        public void startDownload() {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (progress < 100) {
                        progress++;
                        sendBroadcast(progress);
                    } else {
                        timer.cancel();
                    }
                    Log.d("download", progress + "%");
                }
            }, 100, 100);
        }

        public void stopDownload() {
            progress = -1;
            sendBroadcast(progress);
            timer.cancel();
            Log.d("download", "stop");
        }
    }
}
