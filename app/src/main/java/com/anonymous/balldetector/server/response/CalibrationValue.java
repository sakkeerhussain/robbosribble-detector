package com.anonymous.balldetector.server.response;

import com.anonymous.balldetector.models.ReferencePoint;
import com.anonymous.balldetector.opencv.OpenCVManager;

import org.opencv.core.Point;

/**
 * Created by sakkeer on 07/12/17.
 */

public class CalibrationValue extends RespSuccess {

    public CalibrationValueData data;

    public CalibrationValue(int point){
        super("Calibration value");
        ReferencePoint value = null;
        switch (point){
            case 1:
                value = OpenCVManager.get().getRefPoint1();
                break;
            case 2:
                value = OpenCVManager.get().getRefPoint2();
                break;
            case 3:
                value = OpenCVManager.get().getRefPoint3();
                break;
            case 4:
                value = OpenCVManager.get().getRefPoint4();
                break;
        }
        data = new CalibrationValueData(value);
    }

    private class CalibrationValueData {
        ReferencePoint value;

        CalibrationValueData(ReferencePoint value){
            this.value = value;
        }
    }
}
