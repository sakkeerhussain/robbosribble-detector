//package com.anonymous.balldetector.math.geography
//
///*
// * Created by sakkeerhussain on 06/01/18.
// */
//class Point(var x: Float, var y: Float) {
//
//        fun openCV(): org.opencv.core.Point {
//        return org.opencv.core.Point(x.toDouble(), y.toDouble())
//        }
//
//        fun set(x: Float, y: Float) {
//        this.x = x
//        this.y = y
//        }
//
//        fun getAngledPoint(angleInRad: Double, distance: Double): Point {
//        val xDelta = (distance * Math.cos(angleInRad)).toFloat()
//        val yDelta = (distance * Math.sin(angleInRad)).toFloat()
//        return Point(this.x + xDelta, this.y + yDelta)
//        }
//        }