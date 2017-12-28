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
        public static final String URI_VALUE = "/value";
    }

    public class Stream {
        public static final String URI = "/stream";
        public static final String URI_RAW = "/raw";
        public static final String URI_DETECTION = "/detection";
        public static final String URI_BALL_COLOUR = "/ball-color";
        public static final String URI_REF_COLOUR = "/ref-color";
        public static final String URI_IMAGE = "/image";

        public static final String HTML = "<html>\n" +
                "<head>\n" +
                "\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=windows-1252\">\n" +
                "\t<style type=\"text/css\">\n" +
                "\t\t.control{\n" +
                "\t\t\tfloat: right;\n" +
                "\t\t}\n" +
                "\t</style>\n" +
                "</head>\n" +
                "<body cz-shortcut-listen=\"true\">\n" +
                "<img src=\"image/\" width=\"760\" height=\"570\" id=\"image\">\n" +
                "<div class=\"control\">\n" +
                "\t<ul>\n" +
                "\t\t<li><a href=\"../raw/\">Raw</a></li>\n" +
                "\t\t<li><a href=\"../detection/\">Detection</a></li>\n" +
                "\t\t<li><a href=\"../ball-color/\">Ball color</a></li>\n" +
                "\t\t<li><a href=\"../ref-color/\">Reference color</a></li>\n" +
                "\t</ul>\n" +
                "</div>\n" +
                "<script>\n" +
                "var myImageElement = document.getElementById('image');\n" +
                "function imageLoaded() {\n" +
                "    myImageElement.src = 'image/?rand=' + Math.random();\n" +
                "} \n" +
                "myImageElement.addEventListener('load', imageLoaded)\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>";
    }

    public class Error {
        public static final String KEY = "err";
        public static final String INVALID_URI = "Invalid URI provided";
    }
    public static final String FAV_ICON = "/favicon.ico";
}
