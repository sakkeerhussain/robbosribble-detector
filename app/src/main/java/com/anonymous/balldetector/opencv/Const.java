package com.anonymous.balldetector.opencv;

import org.opencv.core.Scalar;

/**
 * Created by sakkeer on 12/12/17.
 */

public class Const {

    public static final int BALL_RADIUS_MIN = 5;
    public static final int BALL_RADIUS_MAX = 30;
    public static Scalar YELLOW_SCALAR_MIN = new Scalar(20,100,100);
    public static Scalar YELLOW_SCALAR_MAX = new Scalar(35,255,255);
    public static Scalar REFERENCE_SCALAR_MIN = new Scalar(12,72,108);
    public static Scalar REFERENCE_SCALAR_MAX = new Scalar(17,146,180);
}
