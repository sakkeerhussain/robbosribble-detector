package com.anonymous.balldetector.opencv;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.anonymous.balldetector.models.Ball;

import org.jetbrains.annotations.NotNull;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.List;

/**
 * Created by sakkeer on 13/12/17.
 */

public class OpenCVUtils {
    public static final String ERROR_INVALID_BALL_COUNT = "Invalid balls found. Only one ball should be in board to calibrate";
    public static final int DISPLAY_NORMAL = 0;
    public static final int DISPLAY_BALLS_IN_RANGE = 1;
    public static final int DISPLAY_REFERENCE_IN_RANGE = 2;

    public static List<Ball> getBalls(@NotNull Mat rgbaFrame) {
        return OpenCVManager.get().detectCircles(rgbaFrame, Const.YELLOW_SCALAR_MIN, Const.YELLOW_SCALAR_MAX,
                Const.BALL_RADIUS_MIN, Const.BALL_RADIUS_MAX);
    }

    public static String updateRefPoints(int pointNo){
        List<Ball> balls = getBalls(OpenCVManager.get().getRGBFrame());
        if (balls.size() != 1){
            return ERROR_INVALID_BALL_COUNT;
        }
        Ball ball = balls.get(0);
        switch (pointNo){
            case 1:
                OpenCVManager.get().setRefPoint1(ball.getCenterPoint());
                return "Success";
            case 2:
                OpenCVManager.get().setRefPoint2(ball.getCenterPoint());
                return com.anonymous.balldetector.server.Const.SUCCESS;
            case 3:
                OpenCVManager.get().setRefPoint3(ball.getCenterPoint());
                return com.anonymous.balldetector.server.Const.SUCCESS;
            case 4:
                OpenCVManager.get().setRefPoint4(ball.getCenterPoint());
                return com.anonymous.balldetector.server.Const.SUCCESS;
        }
        return "Failed";
    }

    public static void drawFrameToImageView(final ImageView imageView, Activity activity, boolean withBall, int displayType) {
        Mat frame = OpenCVManager.get().getRGBFrame();
        drawFrameToImageView(frame, imageView, activity, withBall, displayType);
    }

    public static void drawFrameToImageView(Mat frame, final ImageView imageView, Activity activity, boolean withBall, int displayType) {
        if (frame == null) {
            return;
        }
        if (withBall) {
            drawBallsToFrame(frame);
        }

        updateDisplayType(displayType, frame);

        Mat frameRes = new Mat();
        Core.flip(frame.t(), frameRes, 1);
        if (frameRes.empty()) {
            return;
        }
//        Imgproc.resize(frameRes, frameRes, new Size(imageView.getWidth(), imageView.getHeight()));
        if (frameRes.cols() > 0 && frameRes.rows() > 0) {
            final Bitmap bm = Bitmap.createBitmap(frameRes.cols(), frameRes.rows(), Bitmap.Config.ARGB_8888);
            org.opencv.android.Utils.matToBitmap(frameRes, bm);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageBitmap(bm);
                }
            });
        }
    }

    public static void drawBallsToFrame(Mat frame) {
        List<Ball> balls = OpenCVUtils.getBalls(frame);
        for (Ball ball : balls) {
            Imgproc.circle(frame, ball.getCenterPoint(), 15, new Scalar(0, 255, 0), 3, 8, 0);
        }
    }

    public static void updateDisplayType(int displayType, Mat frame) {
        switch (displayType){
            case DISPLAY_BALLS_IN_RANGE:
                findInRangeFrame(frame, Const.YELLOW_SCALAR_MIN, Const.YELLOW_SCALAR_MAX);
                break;
            case DISPLAY_REFERENCE_IN_RANGE:
                findInRangeFrame(frame, Const.REFERENCE_SCALAR_MIN, Const.REFERENCE_SCALAR_MAX);
                break;
        }
    }

    private static void findInRangeFrame(Mat frame, Scalar minRange, Scalar maxRange) {
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
