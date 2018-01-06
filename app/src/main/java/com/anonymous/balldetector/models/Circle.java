package com.anonymous.balldetector.models;

import com.anonymous.balldetector.math.geography.Point;

/**
 * Created by sakkeer on 13/12/17.
 */

public class Circle {
    private float x;
    private float y;

    public Circle(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point getCenterPoint() {
        return new Point(x, y);
    }

    public void setCenterPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setCenterPoint(Point point) {
        this.x = point.getX();
        this.y = point.getY();
    }
}
