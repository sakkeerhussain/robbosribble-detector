package com.anonymous.balldetector.opencv;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

import com.anonymous.balldetector.math.geography.Point;
import com.anonymous.balldetector.models.Circle;
import com.anonymous.balldetector.models.ReferencePoint;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
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

    private ReferencePoint refPoint1;
    private ReferencePoint refPoint2;
    private ReferencePoint refPoint3;
    private ReferencePoint refPoint4;

    private OpenCVManager() {
        refPoint1 = new ReferencePoint(0, 0, -10, -10);
        refPoint2 = new ReferencePoint(1245, 25, 290, -10);
        refPoint3 = new ReferencePoint(0, 865, -10, 190);
        refPoint4 = new ReferencePoint(1245, 840, 290, 190);
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

    public void setRefPoint(float xImage, float yImage, float xBord, float yBord, int point) {
        ReferencePoint refPoint = new ReferencePoint(xImage, yImage, xBord, yBord);
        switch (point) {
            case 1:
                OpenCVManager.get().setRefPoint1(refPoint);
                break;
            case 2:
                OpenCVManager.get().setRefPoint2(refPoint);
                break;
            case 3:
                OpenCVManager.get().setRefPoint3(refPoint);
                break;
            case 4:
                OpenCVManager.get().setRefPoint4(refPoint);
                break;
        }
    }

    void setRefPoint1(ReferencePoint point) {
        this.refPoint1 = point;
    }

    void setRefPoint2(ReferencePoint point) {
        this.refPoint2 = point;
    }

    void setRefPoint3(ReferencePoint point) {
        this.refPoint3 = point;
    }

    void setRefPoint4(ReferencePoint point) {
        this.refPoint4 = point;
    }

    public ReferencePoint getRefPoint1() {
        return this.refPoint1;
    }

    public ReferencePoint getRefPoint2() {
        return this.refPoint2;
    }

    public ReferencePoint getRefPoint3() {
        return this.refPoint3;
    }

    public ReferencePoint getRefPoint4() {
        return this.refPoint4;
    }

    List<Circle> detectCircles(Mat frame, Scalar minRange, Scalar maxRange, int minRadius, int maxRadius, int minDistance) {
        ArrayList<Circle> circles = new ArrayList<>();
        try {
            Mat frameProc = clipFrame(frame);
            Imgproc.medianBlur(frameProc, frameProc, 3);
            Imgproc.cvtColor(frameProc, frameProc, Imgproc.COLOR_RGB2HSV);
            Core.inRange(frameProc, minRange, maxRange, frameProc);
            Imgproc.GaussianBlur(frameProc, frameProc, new Size(9, 9), 2, 2);
            Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(24, 24));
            Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(12, 12));
            Imgproc.erode(frameProc, frameProc, erodeElement);
            Imgproc.dilate(frameProc, frameProc, dilateElement);

            Mat circlesMat = new Mat();
            Imgproc.HoughCircles(frameProc, circlesMat, Imgproc.CV_HOUGH_GRADIENT,
                    1, minDistance, 20, 20, minRadius, maxRadius);

            for (int i = 0; i < circlesMat.cols(); i++) {
                double[] circle = circlesMat.get(0, i);
                Point center = new Point((int) circle[0], (int) circle[1]);
                int radius = (int) circle[2];
                Log.d(TAG, "Ball detected with radius: " + radius + ", and center at " + center);
                circles.add(new Circle(center, radius));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return circles;
    }

    Mat clipFrame(Mat frame) {
        Mat frameRes = new Mat();
        MatOfPoint refPoints = OpenCVManager.get().getRefPoints();
        if (refPoints != null) {
            Mat mask = Mat.zeros(frame.rows(), frame.cols(), CvType.CV_8UC4);
            Imgproc.fillConvexPoly(mask, refPoints, new Scalar(255, 255, 255));
            frame.copyTo(frameRes, mask);
        }
        return frameRes;
    }

    @Override
    public void onPreviewFrame(byte[] frame, Camera camera) {
//        Log.d(TAG, "Raw frame updated");
        mYUVFrame.put(0, 0, frame);

        if (mCamera != null)
            mCamera.addCallbackBuffer(mBuffer);
    }

    private MatOfPoint getRefPoints() {
        if (refPoint1 == null || refPoint2 == null || refPoint3 == null || refPoint4 == null) {
            return null;
        }
        org.opencv.core.Point[] points = new org.opencv.core.Point[]{refPoint1.getPointImage().cvPoint(), refPoint2.getPointImage().cvPoint(),
                refPoint4.getPointImage().cvPoint(), refPoint3.getPointImage().cvPoint()};
        MatOfPoint matOfPoint = new MatOfPoint();
        matOfPoint.fromArray(points);
        return matOfPoint;
    }
}
