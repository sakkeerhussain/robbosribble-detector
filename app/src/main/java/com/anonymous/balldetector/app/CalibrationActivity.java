package com.anonymous.balldetector.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class CalibrationActivity extends BaseActivity {
    private static final String TAG = "CalibrationActivity";

    private ImageView mImageView;
    private Timer mTimer;
    private int mDisplayType = OpenCVUtils.DISPLAY_NORMAL;

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
                mDisplayType = OpenCVUtils.DISPLAY_NORMAL;
                return true;
            case R.id.display_in_range:
                mDisplayType = OpenCVUtils.DISPLAY_IN_RANGE;
                return true;
            case R.id.open_center_color:
                startActivity(new Intent(this, CenterColorCalibrationActivity.class));
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
        OpenCVUtils.drawCurrentFrameToImageView(mImageView, this, false, mDisplayType);
    }
}
