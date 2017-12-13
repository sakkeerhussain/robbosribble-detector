package com.anonymous.balldetector.opencv;

import com.anonymous.balldetector.models.Ball;

import org.jetbrains.annotations.NotNull;
import org.opencv.core.Mat;

import java.util.List;

/**
 * Created by sakkeer on 13/12/17.
 */

public class OpenCVUtils {

    public static List<Ball> getBalls(@NotNull Mat rgbaFrame) {
        return OpenCVManager.get().detectCircles(rgbaFrame, Const.YELLOW_SCALAR_MIN, Const.YELLOW_SCALAR_MAX,
                Const.BALL_RADIUS_MIN, Const.BALL_RADIUS_MAX);
    }
}
