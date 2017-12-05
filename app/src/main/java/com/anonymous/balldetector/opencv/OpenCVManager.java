package com.anonymous.balldetector.opencv;

import android.content.Context;
import android.view.SurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 * Created by sakkeer on 04/12/17.
 */

public class OpenCVManager implements CameraBridgeViewBase.CvCameraViewListener2 {
//    private static VideoCapture camera;

    private static OpenCVManager instance;

    private OpenCVManager(){
    }

    public static OpenCVManager get(){
        if (instance == null){
            instance = new OpenCVManager();
        }
        return instance;
    }


    //methods
    public void initOpenCV(final Context context, final CameraBridgeViewBase cameraBridgeViewBase){

        cameraBridgeViewBase.setCameraIndex(0);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, context, new BaseLoaderCallback(context) {
            @Override
            public void onManagerConnected(int status) {
                if (status == LoaderCallbackInterface.SUCCESS) {
                    start(cameraBridgeViewBase);
                } else {
                    super.onManagerConnected(status);
                }
            }
        });
    }

    private void start(CameraBridgeViewBase cameraBridgeViewBase) {
        cameraBridgeViewBase.enableView();
    }

    public void pause(CameraBridgeViewBase cameraBridgeViewBase) {
        cameraBridgeViewBase.disableView();
    }

//    public Mat grabFrame() {
//        if (camera.isOpened()) {
//            try {
//                Mat frame = new Mat();
//                camera.read(frame);
//                return frame;
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//        return null;
//    }

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
        return mRgbaT;
    }
}
