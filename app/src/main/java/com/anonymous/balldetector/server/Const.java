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

    public class Stream {
        public static final String URI = "/stream";
        public static final String URI_RAW = "/raw";

        public static final String HTML = "<html>\n" +
                "<head>\n" +
                "<script>\n" +
                "setInterval(function() {\n" +
                "    var myImageElement = document.getElementById('image');\n" +
                "    myImageElement.src = '{{type}}/?rand=' + Math.random();\n" +
                "}, 1000);\n" +
                "</script>\n" +
                "</head>\n" +
                "<body>\n" +
                "<img id=\"image\">\n" +
                "</body>\n" +
                "</html>";
    }

    public class Error {
        public static final String KEY = "err";
        public static final String INVALID_URI = "Invalid URI provided";
    }
    public static final String FAV_ICON = "/favicon.ico";
}
