package com.anonymous.balldetector.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.anonymous.balldetector.R;
import com.anonymous.balldetector.opencv.OpenCVManager;
import com.anonymous.balldetector.server.ServerManager;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;

public class CaliberationActivity extends AppCompatActivity {
    private static final String TAG = "CaliberationActivity";

    private JavaCameraView mCameraBridgeViewBase;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caliberation);

        initViews();

        Utils.requestPermissions(this);
    }

    private void initViews() {
        mCameraBridgeViewBase = findViewById(R.id.camera_preview);
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
            OpenCVManager.get().initOpenCV(this, mCameraBridgeViewBase);
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
        OpenCVManager.get().pause(mCameraBridgeViewBase);
    }
}
