package com.anonymous.balldetector.opencv;

import org.opencv.core.Scalar;

/**
 * Created by sakkeer on 12/12/17.
 */

class Const {

    static final int BALL_RADIUS_MIN = 5;
    static final int BALL_RADIUS_MAX = 30;
    static final int BALL_MIN_DISTANCE = 30;
    static Scalar BALL_SCALAR_MIN = new Scalar(20,100,100);
    static Scalar BALL_SCALAR_MAX = new Scalar(35,255,255);

    static final int REFERENCE_RADIUS_MIN = 10;
    static final int REFERENCE_RADIUS_MAX = 30;
    static final int REFERENCE_MIN_DISTANCE = 30;
    static Scalar REFERENCE_SCALAR_MIN = new Scalar(12,72,108);
    static Scalar REFERENCE_SCALAR_MAX = new Scalar(17,146,180);

    static final int BOT_FRONT_RADIUS_MIN = 40;
    static final int BOT_FRONT_RADIUS_MAX = 200;
    static final int BOT_FRONT_MIN_DISTANCE = 500;
    static Scalar BOT_FRONT_SCALAR_MIN = new Scalar(142.0, 40.0, 93.0);
    static Scalar BOT_FRONT_SCALAR_MAX = new Scalar(170.0, 141, 255.0);

    static final int BOT_BACK_RADIUS_MIN = 40;
    static final int BOT_BACK_RADIUS_MAX = 200;
    static final int BOT_BACK_MIN_DISTANCE = 500;
    static Scalar BOT_BACK_SCALAR_MIN = new Scalar(92.0, 33.0, 80.0);
    static Scalar BOT_BACK_SCALAR_MAX = new Scalar(125.0, 101.0, 246.0);

    private static final int BOT_LOCATOR_DISTANCE_IN_BETWEEN = 15;
    private static final double BOT_LOCATOR_DISTANCE_TO_CORNER = 10.61;
    static final double BOT_LOCATOR_DISTANCE_RATIO = BOT_LOCATOR_DISTANCE_TO_CORNER/BOT_LOCATOR_DISTANCE_IN_BETWEEN;
    static final double BOT_LOCATOR_ANGLE_45 = 0.785398;
    static final double BOT_LOCATOR_ANGLE_135 = 2.35619;
}
