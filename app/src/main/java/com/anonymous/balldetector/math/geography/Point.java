package com.anonymous.balldetector.math.geography;
/*
 * Created by sakkeerhussain on 06/01/18.
 */

import org.opencv.core.Mat;

public class Point {
    float x;
    float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public org.opencv.core.Point cvPoint() {
        return new org.opencv.core.Point(x, y);
    }

    void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point getAngledPoint(double angleInRad, double distance, boolean highest) {
        float xDelta = (float) (distance * Math.cos(angleInRad));
        float yDelta = (float) (distance * Math.sin(angleInRad));
        if (highest)
            return new Point(this.x + xDelta, this.y + yDelta);
        else
            return new Point(this.x - xDelta, this.y - yDelta);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + x +',' + y + ')';
    }
}