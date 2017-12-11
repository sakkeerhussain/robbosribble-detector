package com.anonymous.balldetector;

import android.app.Application;

/**
 * Created by sakkeer on 11/12/17.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        System.loadLibrary("opencv_java3");
    }
}
