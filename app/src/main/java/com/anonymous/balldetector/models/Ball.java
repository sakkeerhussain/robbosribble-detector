package com.anonymous.balldetector.models;

import org.opencv.core.Point;

/**
 * Created by sakkeer on 13/12/17.
 */

public class Ball {
    private double x;
    private double y;

    public Ball(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point getCenterPoint() {
        return new Point(x, y);
    }
}
