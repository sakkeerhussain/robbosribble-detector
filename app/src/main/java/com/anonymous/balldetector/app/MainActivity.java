package com.anonymous.balldetector.app;

import android.os.Bundle;

import com.anonymous.balldetector.R;
import com.anonymous.balldetector.opencv.OpenCVManager;
import com.anonymous.balldetector.server.ServerManager;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.requestPermissions(this);

        initOpenCV();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ServerManager.get().startServer();
            }
        }, 0, 10000);
    }

    private void initOpenCV() {
        if (Utils.hasPermissions(this)) {
            OpenCVManager.get().initOpenCV(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        initOpenCV();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServerManager.get().stopServer();
        OpenCVManager.get().pause();
    }
}
