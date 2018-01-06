package com.anonymous.balldetector.math.geography;
/*
 * Created by sakkeerhussain on 06/01/18.
 */

public class Point {
    float x;
    float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public org.opencv.core.Point openCV() {
        return new org.opencv.core.Point(x, y);
    }

    void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point getAngledPoint(double angleInRad, double distance) {
        float xDelta = (float) (distance * Math.cos(angleInRad));
        float yDelta = (float) (distance * Math.sin(angleInRad));
        return new Point(this.x + xDelta, this.y + yDelta);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}