package com.anonymous.balldetector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.anonymous.balldetector.server.MyServer;
import com.anonymous.balldetector.opencv.OpenCVManager;
import com.anonymous.balldetector.server.ServerManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.OpenCVLoader;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, getApplicationContext(), new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                super.onManagerConnected(status);
                OpenCVManager.get().initOpenCV();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ServerManager.get().startServer();
    }

    @Override
    public void onPause() {
        super.onPause();
        ServerManager.get().stopServer();
    }
}
