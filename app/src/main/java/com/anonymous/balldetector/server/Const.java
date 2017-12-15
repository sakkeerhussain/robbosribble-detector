package com.anonymous.balldetector.server;

/**
 * Created by sakkeer on 07/12/17.
 */

public class Const {
    public static final String SUCCESS = "ok";

    public class Calibrate {
        public static final String URI = "/calibrate";
        public static final String URI_REF_POINT = "/ref_point";
        public static final String URI_1 = "/1";
        public static final String URI_2 = "/2";
        public static final String URI_3 = "/3";
        public static final String URI_4 = "/4";
    }

    public class Error {
        public static final String KEY = "err";
        public static final String INVALID_URI = "Invalid URI provided";
    }
}
