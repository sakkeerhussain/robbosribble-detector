///*
//package com.anonymous.balldetector.math.geography
//
//*/
///**
// * Created by sakkeerhussain on 11/09/16.
// *//*
//
//class Line(var point1: Point, var point2: Point) {
//
//    //functions
//    val slop: Double
//        get() = if (point1.x == point2.x) {
//            java.lang.Double.POSITIVE_INFINITY
//        } else ((point2.y - point1.y) / (point2.x - point1.x)).toDouble()
//
//    internal val yIntersection: Double
//        get() {
//            val slop = this.slop
//            return if (slop == java.lang.Double.POSITIVE_INFINITY) {
//                java.lang.Double.POSITIVE_INFINITY
//            } else this.point1.y - (slop * this.point1.x)
//        }
//
//    val angle: Double
//        get() {
//            return if (this.point1.x == this.point2.x) {
//                90.0
//            } else Math.atan(((this.point2.y - this.point1.y) / (this.point2.x - this.point1.x)).toDouble())
//        }
//
//    val distanceInX: Double
//        get() = Math.abs(this.point1.x - this.point2.x).toDouble()
//
//    val distanceInY: Double
//        get() = Math.abs(this.point1.y - this.point2.y).toDouble()
//
//    internal fun getPointAtX(x: Double): Point {
//        val slop = this.slop
//        val y = slop * x + this.yIntersection
//        return Point(x.toFloat(), y.toFloat())
//    }
//
//    fun getIntersectingAngle(line: Line): Double {
//        return Math.abs(this.angle - line.angle)
//    }
//
//    fun length(): Double {
//        val dx = (this.point1.x - this.point2.x).toDouble()
//        val dy = (this.point1.y - this.point2.y).toDouble()
//        return Math.sqrt(dx * dx + dy * dy)
//    }
//
//    fun getDistanceFromPoint(point: Point): Double {
//        val A: Double
//        val B: Double
//        val C: Double
//        if (this.angle == 90.0) {
//            A = 1.0
//            B = 0.0
//            C = (-1 * point1.x).toDouble()
//        } else {
//            A = -1 * this.slop
//            B = 1.0
//            C = this.slop * point1.x - point1.y
//        }
//        return Math.abs(A * point.x + B * point.y + C) / Math.sqrt(A * A + B * B)
//    }
//
//    fun getIntersectingPoint(line1: Line, line2: Line): Point? {
//        // TODO: 11/09/16 consider infinite value case(no interception).
//        // TODO: 11/09/16 consider 0 value case.
//        if (line1.slop == java.lang.Double.POSITIVE_INFINITY && line2.slop == java.lang.Double.POSITIVE_INFINITY) {
//            return null
//        } else if (line1.slop == java.lang.Double.POSITIVE_INFINITY) {
//            return line2.getPointAtX(line1.point1.x.toDouble())
//        } else if (line2.slop == java.lang.Double.POSITIVE_INFINITY) {
//            return line1.getPointAtX(line2.point1.x.toDouble())
//        } else {
//            val x = (line1.yIntersection - line2.yIntersection) / (line2.slop - line1.slop)
//            val y = line1.slop * x + line1.yIntersection
//            return Point(x.toFloat(), y.toFloat())
//        }
//    }
//
//    fun getLine(angle: Double, point: Point, distance: Double): Line {
//        if (angle == 90.0) {
//            return Line(Point(point.x, (point.y + distance).toFloat()), Point(point.x, (point.y - distance).toFloat()))
//        }
//        val slope = Math.tan(Math.toRadians(angle))
//        val a = slope * slope + 1
//        val b = -2.0 * point.x.toDouble() * (slope * slope + 1)
//        val c = slope * slope * point.x.toDouble() * point.x.toDouble() + point.x * point.x - distance * distance
//
//        val temp1 = Math.sqrt(Math.abs(b * b - 4.0 * a * c))
//        val root1 = (-b + temp1) / (2 * a)
//        val point1y = slope * (root1 - point.x) + point.y
//        val point1 = Point(root1.toFloat(), point1y.toFloat())
//
//        val root2 = (-b - temp1) / (2 * a)
//        val point2y = slope * (root2 - point.x) + point.y
//        val point2 = Point(root2.toFloat(), point2y.toFloat())
//        return Line(point1, point2)
//    }
//}
//*/
