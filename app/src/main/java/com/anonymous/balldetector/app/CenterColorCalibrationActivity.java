package com.anonymous.balldetector.app;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;

import com.anonymous.balldetector.R;
import com.anonymous.balldetector.models.Ball;
import com.anonymous.balldetector.opencv.Const;
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

public class CenterColorCalibrationActivity extends BaseActivity {
    private static final String TAG = "CenterColorActivity";
    private static final int DISPLAY_NORMAL = 0;
    private static final int DISPLAY_IN_RANGE = 1;

    private ImageView mImageView;
    private Timer mTimer;
    private int mDisplayType = DISPLAY_NORMAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caliberation);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_caliberation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.display_normal:
                mDisplayType = DISPLAY_NORMAL;
                return true;
            case R.id.display_in_range:
                mDisplayType = DISPLAY_IN_RANGE;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            Imgproc.circle(frame, ball.getCenterPoint(), 30, new Scalar(255, 0, 0), 3, 8, 0);
        }

        switch (mDisplayType){
            case DISPLAY_IN_RANGE:
                findInRangeFrame(frame, Const.YELLOW_SCALAR_MIN, Const.YELLOW_SCALAR_MAX);
                break;
        }

        Mat frameRes = new Mat();
        Core.flip(frame.t(), frameRes, 1);
        if (frameRes.cols() > 0 && frameRes.rows() > 0) {
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

    private void findInRangeFrame(Mat frame, Scalar minRange, Scalar maxRange) {
        Imgproc.medianBlur(frame, frame, 3);
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2HSV);
        Core.inRange(frame, minRange, maxRange, frame);
        Imgproc.GaussianBlur(frame, frame, new Size(9, 9), 2, 2);
        Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(24, 24));
        Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(12, 12));
        Imgproc.erode(frame, frame, erodeElement);
        Imgproc.dilate(frame, frame, dilateElement);
    }
}
