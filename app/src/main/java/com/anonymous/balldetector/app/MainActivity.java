package com.anonymous.balldetector.app;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.anonymous.balldetector.R;
import com.anonymous.balldetector.models.Ball;
import com.anonymous.balldetector.opencv.OpenCVManager;
import com.anonymous.balldetector.opencv.OpenCVUtils;
import com.anonymous.balldetector.server.ServerManager;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
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
                    drawImage();
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


    private void drawImage() {
        Log.d(TAG, "drawImage() called");
        Mat frame = OpenCVManager.get().getRGBFrame();
        if (frame == null) {
            return;
        }
        List<Ball> balls = OpenCVUtils.getBalls(frame);
        for (Ball ball : balls) {
            Imgproc.circle(frame, ball.getCenterPoint(), 4, new Scalar(0, 255, 0), -1, 8, 0);
        }

        Mat frameRes = new Mat();
        Core.flip(frame.t(), frameRes, 1);
//        Imgproc.resize(frameRes, frameRes, new Size(mImageView.getWidth(), mImageView.getHeight()));
        final Bitmap bm = Bitmap.createBitmap(frameRes.cols(), frameRes.rows(), Bitmap.Config.ARGB_8888);
        org.opencv.android.Utils.matToBitmap(frameRes, bm);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mImageView.setImageBitmap(bm);
            }
        });
    }
}
