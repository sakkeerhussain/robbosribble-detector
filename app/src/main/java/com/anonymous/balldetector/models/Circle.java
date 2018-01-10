package com.anonymous.balldetector.models;

import com.anonymous.balldetector.math.geography.Point;

/**
 * Created by sakkeer on 13/12/17.
 */

public class Circle {
    private Point center;
    private int radius;

    public Circle(Point point, int radius) {
        this.center = point;
        this.radius = radius;
    }

    public Point getCenter() {
        return center;
    }

    public int getRadius() {
        return radius;
    }

    public void setCenterPoint(Point point) {
        this.center = point;
    }
}
