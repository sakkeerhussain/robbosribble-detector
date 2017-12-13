package com.anonymous.balldetector.opencv;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceView;

import com.anonymous.balldetector.models.Ball;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sakkeer on 04/12/17.
 */

public class OpenCVManager implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static final String TAG = "OpenCVManager";
//    private static VideoCapture camera;

    private static OpenCVManager instance;
    private Mat mRgbaFrame;

    private OpenCVManager(){
    }

    public static OpenCVManager get(){
        if (instance == null){
            instance = new OpenCVManager();
        }
        return instance;
    }


    //methods
    public void initOpenCV(final Context context, final JavaCameraView cameraBridgeViewBase){

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
        cameraBridgeViewBase.setMaxFrameSize();
        cameraBridgeViewBase.enableView();
    }

    public void pause(CameraBridgeViewBase cameraBridgeViewBase) {
        cameraBridgeViewBase.disableView();
    }

    public List<Ball> getBalls() {
        if (mRgbaFrame == null){
            return null;
        }
        return detectCircles(mRgbaFrame, Const.YELLOW_SCALAR_MIN, Const.YELLOW_SCALAR_MAX,
                Const.BALL_RADIUS_MIN, Const.BALL_RADIUS_MAX);
    }

    public Mat getFrame() {
        return mRgbaFrame;
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
    }

    @Override
    public void onCameraViewStopped() {
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgbaFrame = inputFrame.rgba();
        return null;
    }


    private List<Ball> detectCircles(Mat frame, Scalar minRange, Scalar maxRange, int minRadius, int maxRadius){
        ArrayList<Ball> balls = new ArrayList<>();
        Mat frameProc =new Mat();
        Imgproc.medianBlur(frame, frameProc, 3);
        Imgproc.cvtColor(frameProc, frameProc, Imgproc.COLOR_RGB2HSV);
        Core.inRange(frameProc, minRange, maxRange, frameProc);
        Imgproc.GaussianBlur(frameProc, frameProc, new Size(9, 9), 2, 2);
        Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(24, 24));
        Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(12, 12));
        Imgproc.erode(frameProc, frameProc, erodeElement);
        Imgproc.dilate(frameProc, frameProc, dilateElement);

        Mat circles = new Mat();
        Imgproc.HoughCircles(frameProc, circles, Imgproc.CV_HOUGH_GRADIENT,
                1, 30, 20, 20, minRadius, maxRadius);

        for (int i = 0; i < circles.cols(); i++) {
            double[] circle = circles.get(0, i);
            if (frame.get((int) circle[1], (int) circle[0])[0] > 140) {
                Point center = new Point((int) circle[0], (int) circle[1]);
                int radius = (int) circle[2];
                Log.d(TAG, "Ball detected with radius: "+radius+", and center at "+center);
//                 circle center
//                Imgproc.circle(frame, center, 2, new Scalar(0, 255, 0), -1, 8, 0);
//                 circle outline
//                Imgproc.circle(frame, center, radius, new Scalar(0, 0, 255), 3, 8, 0);

                // TODO: 13/12/17 Calculate circle point according to calib points
                balls.add(new Ball(circle[0], circle[1]));
            }
        }
        return balls;
    }
}
