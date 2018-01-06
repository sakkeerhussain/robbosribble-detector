package com.anonymous.balldetector.math.geography;
/*
 * Created by sakkeerhussain on 06/01/18.
 */

public class Line {
    private Point point1;
    private Point point2;

    public Line(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;
    }

    public double length() {
        float dx = this.point1.x - this.point2.x;
        float dy = this.point1.y - this.point2.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double getAngle() {
        if (this.point1.x == this.point2.x)
            return 90.0;
        else
            return Math.atan((this.point2.y - this.point1.y) / (this.point2.x - this.point1.x));
    }
}
