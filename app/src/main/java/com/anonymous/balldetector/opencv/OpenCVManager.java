package com.anonymous.balldetector.opencv;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.SurfaceView;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Created by sakkeer on 04/12/17.
 */

public class OpenCVManager implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static OpenCVManager instance;
    private CameraBridgeViewBase mCameraBridgeViewBase;
    private ImageView mImageView;

    private OpenCVManager(Context context, int cameraId){
        mCameraBridgeViewBase = new CameraBridgeViewBase(context, cameraId) {
            @Override
            protected boolean connectCamera(int width, int height) {
                return true;
            }

            @Override
            protected void disconnectCamera() {

            }
        };
    }

    public static OpenCVManager get(Context context){
        if (instance == null) {
            instance = new OpenCVManager(context, 0);
        }
        return instance;
    }


    //methods
    public void initOpenCV(final Context context){
        mCameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        mCameraBridgeViewBase.setCvCameraViewListener(this);
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, context, new BaseLoaderCallback(context) {
            @Override
            public void onManagerConnected(int status) {
                if (status == LoaderCallbackInterface.SUCCESS) {
                    start();
                } else {
                    super.onManagerConnected(status);
                }
            }
        });
    }

    private void start() {
        mCameraBridgeViewBase.enableView();
    }

    public void pause() {
        mCameraBridgeViewBase.disableView();
    }

    public void setImageView(ImageView imageView) {
        this.mImageView = imageView;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
    }

    @Override
    public void onCameraViewStopped() {
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat mRgba = inputFrame.rgba();
        Mat mRgbaT = mRgba.t();
        Core.flip(mRgba.t(), mRgbaT, 1);
        Imgproc.resize(mRgbaT, mRgbaT, mRgba.size());
        drawImage(mRgbaT);
        return null;
    }

    private void drawImage(Mat frame) {
        Mat frameRes = new Mat();
        Imgproc.resize(frame, frameRes, new Size(700, 500));
        Bitmap bm = Bitmap.createBitmap(frameRes.cols(), frameRes.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(frameRes, bm);
        if (mImageView != null) {
            mImageView.setImageBitmap(bm);
        }
    }
}
