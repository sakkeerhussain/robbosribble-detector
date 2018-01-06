package com.anonymous.balldetector.models;

import com.anonymous.balldetector.math.geography.Point;

/**
 * Created by Sakkeer Hussain on 29/12/17.
 */

public class ReferencePoint {
    private Point pointImage;
    private Point pointBord;

    public ReferencePoint(Point pointImage, Point pointBord) {
        this.pointImage = pointImage;
        this.pointBord = pointBord;
    }

    public ReferencePoint(float pointImageX, float pointImageY, float pointBordX, float pointBordY) {
        this(new Point(pointImageX, pointImageY), new Point(pointBordX, pointBordY));
    }

    public Point getPointImage() {
        return pointImage;
    }

    public Point getPointBord() {
        return pointBord;
    }
}
