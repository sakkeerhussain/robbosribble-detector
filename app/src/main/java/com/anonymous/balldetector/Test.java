package com.anonymous.balldetector;

import com.anonymous.balldetector.math.geography.Line;
import com.anonymous.balldetector.math.geography.Point;
import com.anonymous.balldetector.opencv.Const;

/**
 * Created by sakkeer on 20/1/18.
 */

public class Test {

    public static void main(String[] args) {
        System.out.println("Running...");

        Point frontCenter = new Point(3f, 64f);
        Point backCenter = new Point(72f, 70f);
        Line centerLine = new Line(frontCenter, backCenter);
        double centerLineAngle = centerLine.getAngle();
        double centerToCornerLength = centerLine.length() * Const.BOT_LOCATOR_DISTANCE_RATIO;
        Point frontLeft = frontCenter.getAngledPoint(centerLineAngle + Const.BOT_LOCATOR_ANGLE_45, centerToCornerLength);
        Point frontRight = frontCenter.getAngledPoint(centerLineAngle - Const.BOT_LOCATOR_ANGLE_45, centerToCornerLength);
        Point backLeft = backCenter.getAngledPoint(centerLineAngle + Const.BOT_LOCATOR_ANGLE_135, centerToCornerLength);
        Point backRight = backCenter.getAngledPoint(centerLineAngle - Const.BOT_LOCATOR_ANGLE_135, centerToCornerLength);

        System.out.println("FL: "+frontLeft);
    }

}
