package com.bqteam.appforlearn.function.service;

import android.content.ComponentName;
import android.content.Intent;
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
public class ServiceActivity extends AppCompatActivity {
    private TestService.MyBinder myBinder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 将 TestService 与 ServiceActivity 关联起来后，就可以在 ServiceActivity 中随意调用 TestService.MyBinder 里面的方法
            myBinder = (TestService.MyBinder) service;
            myBinder.startDownload();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.start_btn, R.id.stop_btn, R.id.bind_btn, R.id.unbind_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.start_btn:
                Intent startIntent = new Intent(ServiceActivity.this, TestService.class);
                startService(startIntent);
                break;
            case R.id.stop_btn:
                Intent stopIntent = new Intent(ServiceActivity.this, TestService.class);
                stopService(stopIntent);
                break;
            case R.id.bind_btn:
                Intent bindIntent = new Intent(ServiceActivity.this, TestService.class);
                bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
                break;
            case R.id.unbind_btn:
                try {
                    unbindService(serviceConnection);
                } catch (IllegalArgumentException ex) {
                    Log.d("IllegalArgument", ex.toString());
                }
                break;
            default:
                break;
        }
    }
}
