package com.anonymous.balldetector.app;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.anonymous.balldetector.R;
import com.anonymous.balldetector.models.Ball;
import com.anonymous.balldetector.opencv.OpenCVManager;
import com.anonymous.balldetector.server.ServerManager;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.FutureTask;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private JavaCameraView mCameraBridgeViewBase;
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

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                drawImage();
            }
        }, 0, 1000);
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

        mTimer.cancel();
        mTimer.purge();
    }


    private void drawImage() {
        List<Ball> balls = OpenCVManager.get().getBalls();
        Mat frame = OpenCVManager.get().getFrame();
        if (frame == null){
            return;
        }
        frame = frame.clone();
        Imgproc.resize(frame, frame, new Size(mImageView.getWidth(), mImageView.getHeight()));
        Bitmap bm = Bitmap.createBitmap(frame.cols(), frame.rows(), Bitmap.Config.ARGB_8888);
        org.opencv.android.Utils.matToBitmap(frame, bm);
        for (Ball ball:balls){
            Imgproc.circle(frame, ball.getCenterPoint(), 4, new Scalar(0, 255, 0), -1, 8, 0);
        }
        mImageView.setImageBitmap(bm);
    }
}
