package com.anonymous.balldetector;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.anonymous.balldetector.opencv.OpenCVManager;
import com.anonymous.balldetector.server.ServerManager;

import org.opencv.android.CameraBridgeViewBase;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private CameraBridgeViewBase mCameraBridgeViewBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        requestPermissions();
    }

    private void requestPermissions() {
        if (!hasPermissions()){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    private boolean hasPermissions() {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
    }

    private void initViews() {
        mCameraBridgeViewBase = findViewById(R.id.camera_preview);
    }

    @Override
    public void onResume() {
        super.onResume();
        ServerManager.get().startServer();
        initOpenCV();
    }

    private void initOpenCV() {
        if (hasPermissions()) {
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


    private void drawImage() {
//        Mat frame = OpenCVManager.get().grabFrame();
//        Mat frameRes = new Mat();
//        Imgproc.resize(frame, frameRes, new Size(700, 500));
//        Bitmap bm = Bitmap.createBitmap(frameRes.cols(), frameRes.rows(), Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(frameRes, bm);
//        imageView.setImageBitmap(bm);
    }
}
