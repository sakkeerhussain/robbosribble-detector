package com.anonymous.balldetector.opencv;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

import com.anonymous.balldetector.models.Ball;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
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

public class OpenCVManager implements Camera.PreviewCallback {
    private static final String TAG = "OpenCVManager";

    private static OpenCVManager instance;
    private Camera mCamera;
    private byte[] mBuffer;
    private Mat mYUVFrame;
    private SurfaceTexture mSurfaceTexture;

    private Point refPoint1;
    private Point refPoint2;
    private Point refPoint3;
    private Point refPoint4;

    private OpenCVManager() {
    }

    public static OpenCVManager get() {
        if (instance == null) {
            instance = new OpenCVManager();
        }
        return instance;
    }


    //methods
    public void initOpenCV(Context context) {

        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, context, new BaseLoaderCallback(context) {
            @Override
            public void onManagerConnected(int status) {

                try {
                    mCamera = Camera.open();
                } catch (Exception e) {
                    Log.e(TAG, "Camera is not available (in use or does not exist): " + e.getLocalizedMessage());
                }
                if (mCamera == null)
                    return;

                try {
                    Camera.Parameters params = mCamera.getParameters();
                    Log.d(TAG, "getSupportedPreviewSizes()");
                    Camera.Size size = params.getSupportedPreviewSizes().get(0);

                    if (size != null) {
                        params.setPreviewFormat(ImageFormat.NV21);
                        params.setPreviewSize(size.width, size.height);

                        List<String> focusModes = params.getSupportedFocusModes();
                        if (focusModes != null && focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                        }
                        mCamera.setParameters(params);
                        params = mCamera.getParameters();

                        int frameHeight = params.getPreviewSize().height;
                        int frameWidth = params.getPreviewSize().width;
                        int imageSize = frameWidth * frameHeight;
                        imageSize = imageSize * ImageFormat.getBitsPerPixel(params.getPreviewFormat()) / 8;
                        mBuffer = new byte[imageSize];

                        mCamera.addCallbackBuffer(mBuffer);
                        mCamera.setPreviewCallbackWithBuffer(OpenCVManager.this);

                        mYUVFrame = new Mat(frameHeight + (frameHeight / 2), frameWidth, CvType.CV_8UC1);

                        mSurfaceTexture = new SurfaceTexture(10);
                        mCamera.setPreviewTexture(mSurfaceTexture);
                        start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void start() {
        Log.d(TAG, "startPreview");
        mCamera.startPreview();
    }

    public void pause() {
        try {
            Log.d(TAG, "stopPreview");
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
                mCamera.release();
            }

            mYUVFrame.release();

            mSurfaceTexture.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Mat getRGBFrame() {
        if (mYUVFrame == null) {
            return null;
        }
        Mat rgbaFrame = new Mat();
        Imgproc.cvtColor(mYUVFrame, rgbaFrame, Imgproc.COLOR_YUV2RGBA_NV21, 4);
        return rgbaFrame;
    }

    public void setRefPoint1(Point point) {
        this.refPoint1 = point;
    }

    public void setRefPoint2(Point point) {
        this.refPoint2 = point;
    }

    public void setRefPoint3(Point point) {
        this.refPoint3 = point;
    }

    public void setRefPoint4(Point point) {
        this.refPoint4 = point;
    }

    public List<Ball> detectCircles(Mat frame, Scalar minRange, Scalar maxRange, int minRadius, int maxRadius) {
        ArrayList<Ball> balls = new ArrayList<>();
        try {
            Mat frameProc = new Mat();
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
                    Log.d(TAG, "Ball detected with radius: " + radius + ", and center at " + center);
                    // TODO: 13/12/17 Calculate circle point according to calib points
                    balls.add(new Ball(circle[0], circle[1]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return balls;
    }

    @Override
    public void onPreviewFrame(byte[] frame, Camera camera) {
//        Log.d(TAG, "Raw frame updated");
        mYUVFrame.put(0, 0, frame);

        if (mCamera != null)
            mCamera.addCallbackBuffer(mBuffer);
    }
}
