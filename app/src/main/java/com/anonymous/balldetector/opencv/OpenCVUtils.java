package com.anonymous.balldetector.opencv;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.anonymous.balldetector.math.geography.Line;
import com.anonymous.balldetector.math.geography.Point;
import com.anonymous.balldetector.models.Circle;
import com.anonymous.balldetector.models.ReferencePoint;
import com.anonymous.balldetector.server.response.RespBase;
import com.anonymous.balldetector.server.response.RespBot;
import com.anonymous.balldetector.server.response.RespError;

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
    private static final String ERROR_INVALID_REF_POINT_COUNT = "Invalid reference points found. Only one reference point should be in board to calibrate";
    public static final int DISPLAY_NORMAL = 0;
    public static final int DISPLAY_BALLS_IN_RANGE = 1;
    public static final int DISPLAY_REFERENCE_IN_RANGE = 2;
    public static final int DISPLAY_BOT_FRONT_IN_RANGE = 3;
    public static final int DISPLAY_BOT_BACK_IN_RANGE = 4;

    private static List<Circle> getBalls(@NotNull Mat rgbaFrame) {
        return OpenCVManager.get().detectCircles(rgbaFrame, Const.BALL_SCALAR_MIN, Const.BALL_SCALAR_MAX,
                Const.BALL_RADIUS_MIN, Const.BALL_RADIUS_MAX, Const.BALL_MIN_DISTANCE);
    }

    private static List<Circle> getBotFront(@NotNull Mat rgbaFrame) {
        return OpenCVManager.get().detectCircles(rgbaFrame, Const.BOT_FRONT_SCALAR_MIN, Const.BOT_FRONT_SCALAR_MAX,
                Const.BOT_FRONT_RADIUS_MIN, Const.BOT_FRONT_RADIUS_MAX, Const.BOT_FRONT_MIN_DISTANCE);
    }

    private static List<Circle> getBotBack(@NotNull Mat rgbaFrame) {
        return OpenCVManager.get().detectCircles(rgbaFrame, Const.BOT_BACK_SCALAR_MIN, Const.BOT_BACK_SCALAR_MAX,
                Const.BOT_BACK_RADIUS_MIN, Const.BOT_BACK_RADIUS_MAX, Const.BOT_BACK_MIN_DISTANCE);
    }

    private static List<Circle> getRefPoint(@NotNull Mat rgbaFrame) {
        return OpenCVManager.get().detectCircles(rgbaFrame, Const.REFERENCE_SCALAR_MIN, Const.REFERENCE_SCALAR_MAX,
                Const.REFERENCE_RADIUS_MIN, Const.REFERENCE_RADIUS_MAX, Const.REFERENCE_MIN_DISTANCE);
    }

    public static String updateRefPoints(int pointNo) {
        List<Circle> referencePoints = getRefPoint(OpenCVManager.get().getRGBFrame());
        if (referencePoints.size() != 1) {
            return ERROR_INVALID_REF_POINT_COUNT;
        }
        Circle referencePoint = referencePoints.get(0);
        switch (pointNo) {
            case 1:
                OpenCVManager.get().setRefPoint1(new ReferencePoint(referencePoint.getCenterPoint(), new Point(0, 0)));
                return com.anonymous.balldetector.server.Const.SUCCESS;
            case 2:
                OpenCVManager.get().setRefPoint2(new ReferencePoint(referencePoint.getCenterPoint(), new Point(180, 0)));
                return com.anonymous.balldetector.server.Const.SUCCESS;
            case 3:
                OpenCVManager.get().setRefPoint3(new ReferencePoint(referencePoint.getCenterPoint(), new Point(0, 280)));
                return com.anonymous.balldetector.server.Const.SUCCESS;
            case 4:
                OpenCVManager.get().setRefPoint4(new ReferencePoint(referencePoint.getCenterPoint(), new Point(180, 280)));
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
        List<Circle> balls = OpenCVUtils.getBalls(frame);
        for (Circle ball : balls) {
            Imgproc.circle(frame, ball.getCenterPoint().cvPoint(), ball.getRadius(), new Scalar(0, 255, 0), 3, 8, 0);
        }
    }

    public static void drawRefPointsToFrame(Mat frame) {
        List<Circle> refPoints = OpenCVUtils.getRefPoint(frame);
        for (Circle refPoint : refPoints) {
            Imgproc.circle(frame, refPoint.getCenterPoint().cvPoint(), refPoint.getRadius(), new Scalar(0, 0, 255), 3, 8, 0);
        }
    }

    public static void drawBordToFrame(Mat frame) {
        ReferencePoint fullRefPoint1 = OpenCVManager.get().getRefPoint1();
        org.opencv.core.Point refPoint1 = null;
        if (fullRefPoint1 != null)
            refPoint1 = OpenCVManager.get().getRefPoint1().getPointImage().cvPoint();
        ReferencePoint fullRefPoint2 = OpenCVManager.get().getRefPoint2();
        org.opencv.core.Point refPoint2 = null;
        if (fullRefPoint2 != null)
            refPoint2 = OpenCVManager.get().getRefPoint2().getPointImage().cvPoint();
        ReferencePoint fullRefPoint3 = OpenCVManager.get().getRefPoint3();
        org.opencv.core.Point refPoint3 = null;
        if (fullRefPoint3 != null)
            refPoint3 = OpenCVManager.get().getRefPoint3().getPointImage().cvPoint();
        ReferencePoint fullRefPoint4 = OpenCVManager.get().getRefPoint4();
        org.opencv.core.Point refPoint4 = null;
        if (fullRefPoint4 != null)
            refPoint4 = OpenCVManager.get().getRefPoint4().getPointImage().cvPoint();

        if (refPoint1 != null && refPoint2 != null) {
            Imgproc.line(frame, refPoint1, refPoint2, new Scalar(225, 0, 0), 3);
        }
        if (refPoint1 != null && refPoint3 != null) {
            Imgproc.line(frame, refPoint1, refPoint3, new Scalar(225, 0, 0), 3);
        }
        if (refPoint4 != null && refPoint2 != null) {
            Imgproc.line(frame, refPoint4, refPoint2, new Scalar(225, 0, 0), 3);
        }
        if (refPoint3 != null && refPoint4 != null) {
            Imgproc.line(frame, refPoint3, refPoint4, new Scalar(225, 0, 0), 3);
        }

        if (refPoint1 != null) {
            Imgproc.putText(frame, "1", refPoint1, Core.FONT_HERSHEY_PLAIN, 4, new Scalar(225, 10, 10), 3);
        }
        if (refPoint2 != null) {
            Imgproc.putText(frame, "2", refPoint2, Core.FONT_HERSHEY_PLAIN, 4, new Scalar(225, 10, 10), 3);
        }
        if (refPoint3 != null) {
            Imgproc.putText(frame, "3", refPoint3, Core.FONT_HERSHEY_PLAIN, 4, new Scalar(225, 10, 10), 3);
        }
        if (refPoint4 != null) {
            Imgproc.putText(frame, "4", refPoint4, Core.FONT_HERSHEY_PLAIN, 4, new Scalar(225, 10, 10), 3);
        }
    }

    public static void updateDisplayType(int displayType, Mat frame) {
        switch (displayType) {
            case DISPLAY_BALLS_IN_RANGE:
                findInRangeFrame(frame, Const.BALL_SCALAR_MIN, Const.BALL_SCALAR_MAX);
                break;
            case DISPLAY_REFERENCE_IN_RANGE:
                findInRangeFrame(frame, Const.REFERENCE_SCALAR_MIN, Const.REFERENCE_SCALAR_MAX);
                break;
            case DISPLAY_BOT_FRONT_IN_RANGE:
                findInRangeFrame(frame, Const.BOT_FRONT_SCALAR_MIN, Const.BOT_FRONT_SCALAR_MAX);
                break;
            case DISPLAY_BOT_BACK_IN_RANGE:
                findInRangeFrame(frame, Const.BOT_BACK_SCALAR_MIN, Const.BOT_BACK_SCALAR_MAX);
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

    public static List<Circle> getBallsInBoard() {
        Mat frame = OpenCVManager.get().getRGBFrame();
        List<Circle> balls = OpenCVUtils.getBalls(frame);
        for (Circle ball : balls) {
            Point boardPoint = convertPointOnBoard(ball.getCenterPoint());
            ball.setCenterPoint(boardPoint);
        }
        return balls;
    }

    private static Point convertPointOnBoard(Point centerPoint) {
        ReferencePoint point1 = OpenCVManager.get().getRefPoint1();
        ReferencePoint point2 = OpenCVManager.get().getRefPoint2();
        ReferencePoint point3 = OpenCVManager.get().getRefPoint3();
        //Assumption #1
        float imageXd = centerPoint.getX() - point1.getPointImage().getX();
        float imageYd = centerPoint.getY() - point1.getPointImage().getY();

        float imageXD = point2.getPointImage().getX() - point1.getPointImage().getX();
        float imageYD = point3.getPointImage().getY() - point1.getPointImage().getY();

        float boardXD = point2.getPointBord().getX() - point1.getPointBord().getX();
        float boardYD = point3.getPointBord().getY() - point1.getPointBord().getY();

        float x = imageXd * boardXD / imageXD;
        float y = imageYd * boardYD / imageYD;
        return new Point(x, y);

        //Assumption #2
    }

    public static RespBase getBotLocation() {
        Mat frame = OpenCVManager.get().getRGBFrame();
        List<Circle> frontCircles = OpenCVUtils.getBotFront(frame);
        List<Circle> backCircles = OpenCVUtils.getBotBack(frame);
        if (frontCircles.size() < 1 || backCircles.size() < 1)
            return new RespError("Bot not found");
        Point frontCenter = frontCircles.get(0).getCenterPoint();
        Point backCenter = backCircles.get(0).getCenterPoint();
        Line centerLine = new Line(frontCircles.get(0).getCenterPoint(), backCircles.get(0).getCenterPoint());
        double centerLineAngle = centerLine.getAngle();
        double centerLength = centerLine.length() * Const.BOT_LOCATOR_DISTANCE_RATIO;
        Point frontLeft = frontCenter.getAngledPoint(centerLineAngle + Const.BOT_LOCATOR_ANGLE_45, centerLength);
        Point frontRight = frontCenter.getAngledPoint(centerLineAngle - Const.BOT_LOCATOR_ANGLE_45, centerLength);
        Point backLeft = backCenter.getAngledPoint(centerLineAngle + Const.BOT_LOCATOR_ANGLE_135, centerLength);
        Point backRight = backCenter.getAngledPoint(centerLineAngle - Const.BOT_LOCATOR_ANGLE_135, centerLength);
        return new RespBot(frontLeft, frontRight, backLeft, backRight, centerLineAngle);
    }

    public static void drawBotToFrame(Mat frame) {
        RespBase location = getBotLocation();
        List<Circle> frontCircles = OpenCVUtils.getBotFront(frame);
        for (Circle circle : frontCircles) {
            Imgproc.circle(frame, circle.getCenterPoint().cvPoint(), circle.getRadius(), new Scalar(221, 21, 246), 3, 8, 0);
        }

        List<Circle> backCircles = OpenCVUtils.getBotBack(frame);
        for (Circle circle : backCircles) {
            Imgproc.circle(frame, circle.getCenterPoint().cvPoint(), circle.getRadius(), new Scalar(223, 246, 21), 3, 8, 0);
        }

        Scalar scalar = new Scalar(10, 10, 255);
        Imgproc.putText(frame, "BOT NOT FOUND", new org.opencv.core.Point(0, 0), Core.FONT_HERSHEY_PLAIN, 4, scalar, 3);

        if (location instanceof RespBot) {
            RespBot botLocation = (RespBot) location;
            Imgproc.line(frame, botLocation.data.frontLeft.cvPoint(), botLocation.data.frontRight.cvPoint(), scalar, 3);
            Imgproc.line(frame, botLocation.data.frontLeft.cvPoint(), botLocation.data.backLeft.cvPoint(), scalar, 3);
            Imgproc.line(frame, botLocation.data.backRight.cvPoint(), botLocation.data.backLeft.cvPoint(), scalar, 3);
            Imgproc.line(frame, botLocation.data.backRight.cvPoint(), botLocation.data.frontRight.cvPoint(), scalar, 3);

            Imgproc.putText(frame, "FL", botLocation.data.frontLeft.cvPoint(), Core.FONT_HERSHEY_PLAIN, 4, scalar, 3);
            Imgproc.putText(frame, "FR", botLocation.data.frontRight.cvPoint(), Core.FONT_HERSHEY_PLAIN, 4, scalar, 3);
            Imgproc.putText(frame, "BL", botLocation.data.backLeft.cvPoint(), Core.FONT_HERSHEY_PLAIN, 4, scalar, 3);
            Imgproc.putText(frame, "BR", botLocation.data.backRight.cvPoint(), Core.FONT_HERSHEY_PLAIN, 4, scalar, 3);
        }
    }
}
