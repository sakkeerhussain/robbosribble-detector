package com.anonymous.balldetector;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.anonymous.balldetector.opencv.OpenCVManager;
import com.anonymous.balldetector.server.ServerManager;
import com.anonymous.opencv.core.CvType;
import com.anonymous.opencv.core.Mat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.loadLibrary("opencv_java3");
        Mat mat = new Mat(3, 3, CvType.CV_8SC1);
        System.out.print(mat);


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
        OpenCVManager.get().setImageView((ImageView) findViewById(R.id.camera_preview));
    }

    @Override
    public void onResume() {
        super.onResume();
        ServerManager.get().startServer();
        initOpenCV();
    }

    private void initOpenCV() {
        if (hasPermissions()) {
            OpenCVManager.get().initOpenCV(this);
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
    }
}
