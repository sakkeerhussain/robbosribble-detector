package com.anonymous.balldetector.opencv;

import android.content.Context;
import android.widget.ImageView;

import com.anonymous.opencv.core.Core;
import com.anonymous.opencv.core.Mat;
import com.anonymous.opencv.core.Size;
import com.anonymous.opencv.imgproc.Imgproc;
import com.anonymous.opencv.videoio.VideoCapture;

/**
 * Created by sakkeer on 04/12/17.
 */

public class OpenCVManager {

    private static OpenCVManager instance;
    private VideoCapture mCamera;

    private OpenCVManager(int cameraId){
        mCamera = new VideoCapture(cameraId);
    }

    public static OpenCVManager get(){
        if (instance == null) {
            instance = new OpenCVManager(0);
        }
        return instance;
    }


    //methods
    public void initOpenCV(final Context context){
//        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, context, new BaseLoaderCallback(context) {
//            @Override
//            public void onManagerConnected(int status) {
//                if (status == LoaderCallbackInterface.SUCCESS) {
//                    start();
//                } else {
//                    super.onManagerConnected(status);
//                }
//            }
//        });
    }

    private void start() {
//        mCamera.enableView();
    }

    public void pause() {
//        mCamera.disableView();
    }

    public void setImageView(ImageView imageView) {
//        this.mImageView = imageView;
    }

//    @Override
//    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
//        Mat mRgba = inputFrame.rgba();
//        Mat mRgbaT = mRgba.t();
//        Core.flip(mRgba.t(), mRgbaT, 1);
//        Imgproc.resize(mRgbaT, mRgbaT, mRgba.size());
//        drawImage(mRgbaT);
//        return null;
//    }

//    private void drawImage(Mat frame) {
//        Mat frameRes = new Mat();
//        Imgproc.resize(frame, frameRes, new Size(700, 500));
//        Bitmap bm = Bitmap.createBitmap(frameRes.cols(), frameRes.rows(), Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(frameRes, bm);
//        if (mImageView != null) {
//            mImageView.setImageBitmap(bm);
//        }
//    }
}
