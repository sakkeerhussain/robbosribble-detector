package com.anonymous.balldetector.server.response;

import com.anonymous.balldetector.models.ReferencePoint;
import com.anonymous.balldetector.opencv.OpenCVManager;

import org.opencv.core.Point;

/**
 * Created by sakkeer on 07/12/17.
 */

public class CalibrationValue extends RespSuccess {

    public ReferencePoint data;

    public CalibrationValue(int point){
        super("Calibration value");
        switch (point){
            case 1:
                data = OpenCVManager.get().getRefPoint1();
                break;
            case 2:
                data = OpenCVManager.get().getRefPoint2();
                break;
            case 3:
                data = OpenCVManager.get().getRefPoint3();
                break;
            case 4:
                data = OpenCVManager.get().getRefPoint4();
                break;
        }
    }
}
