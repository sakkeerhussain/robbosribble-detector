package com.anonymous.balldetector.opencv;

/**
 * Created by sakkeer on 04/12/17.
 */

public class OpenCVManager {

    private static OpenCVManager instance;

    private OpenCVManager(){
    }

    public static OpenCVManager get(){
        if (instance == null){
            instance = new OpenCVManager();
        }
        return instance;
    }


    //methods
    public void initOpenCV(){

    }
}
