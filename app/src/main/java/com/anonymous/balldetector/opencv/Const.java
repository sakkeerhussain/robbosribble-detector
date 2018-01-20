package com.anonymous.balldetector.opencv;

import org.opencv.core.Scalar;

/**
 * Created by sakkeer on 12/12/17.
 */

public class Const {

    static final int BALL_RADIUS_MIN = 10;
    static final int BALL_RADIUS_MAX = 26;
    static final int BALL_MIN_DISTANCE = 13;
    static Scalar BALL_SCALAR_MIN = new Scalar(20,100,100);
    static Scalar BALL_SCALAR_MAX = new Scalar(35,255,255);

    static final int REFERENCE_RADIUS_MIN = 10;
    static final int REFERENCE_RADIUS_MAX = 30;
    static final int REFERENCE_MIN_DISTANCE = 100;
    static Scalar REFERENCE_SCALAR_MIN = new Scalar(12,72,108);
    static Scalar REFERENCE_SCALAR_MAX = new Scalar(17,146,180);

    static final int BOT_FRONT_RADIUS_MIN = 26;
    static final int BOT_FRONT_RADIUS_MAX = 100;
    static final int BOT_FRONT_MIN_DISTANCE = 500;
    static Scalar BOT_FRONT_SCALAR_MIN = new Scalar(134.0, 149.0, 95.0);
    static Scalar BOT_FRONT_SCALAR_MAX = new Scalar(143.0, 255.0, 255.0);

    static final int BOT_BACK_RADIUS_MIN = 26;
    static final int BOT_BACK_RADIUS_MAX = 100;
    static final int BOT_BACK_MIN_DISTANCE = 500;
    static Scalar BOT_BACK_SCALAR_MIN = new Scalar(63.0, 96.0, 136.0);
    static Scalar BOT_BACK_SCALAR_MAX = new Scalar(73.0, 183.0, 255.0);

    private static final int BOT_LOCATOR_DISTANCE_IN_BETWEEN = 15;
    private static final double BOT_LOCATOR_DISTANCE_TO_CORNER = 10.61;
    public static final double BOT_LOCATOR_DISTANCE_RATIO = BOT_LOCATOR_DISTANCE_TO_CORNER/BOT_LOCATOR_DISTANCE_IN_BETWEEN;
    public static final double BOT_LOCATOR_ANGLE_45 = 0.785398;
    public static final double BOT_LOCATOR_ANGLE_135 = 2.35619;
}
