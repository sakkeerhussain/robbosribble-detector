package com.anonymous.balldetector.app;

import android.os.Bundle;
import android.widget.ImageView;

import com.anonymous.balldetector.R;
import com.anonymous.balldetector.opencv.OpenCVManager;
import com.anonymous.balldetector.opencv.OpenCVUtils;
import com.anonymous.balldetector.server.ServerManager;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    private ImageView mImageView;
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        Utils.requestPermissions(this);
    }

    private void initViews() {
        mImageView = findViewById(R.id.image_view);
    }

    @Override
    public void onResume() {
        super.onResume();
        ServerManager.get().startServer();
        initOpenCV();
    }

    private void initOpenCV() {
        if (Utils.hasPermissions(this)) {
            OpenCVManager.get().initOpenCV(this);

            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    OpenCVUtils.drawCurrentFrameToImageView(mImageView, MainActivity.this,
                            true, OpenCVUtils.DISPLAY_NORMAL);
                }
            }, 0, 500);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        initOpenCV();
    }

    @Override
    public void onPause() {
        super.onPause();
        ServerManager.get().stopServer();
        OpenCVManager.get().pause();

        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
        }
    }
}
