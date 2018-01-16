package com.anonymous.balldetector.server;

/**
 * Created by Sakkeer Hussain on 07/12/17.
 */

public class Const {
    public static final String SUCCESS = "ok";

    class Balls {
        static final String URI = "/balls";
    }

    class Bot {
        static final String URI = "/bot";
    }

    class Calibrate {
        static final String URI = "/calibrate";
        static final String URI_REF_POINT = "/ref_point";
        static final String URI_1 = "/1";
        static final String URI_2 = "/2";
        static final String URI_3 = "/3";
        static final String URI_4 = "/4";
        static final String URI_VALUE = "/value";
    }

    class Stream {
        static final String URI = "/stream";
        static final String URI_RAW = "/raw";
        static final String URI_DETECTION = "/detection";
        static final String URI_BALLS = "/balls";
        static final String URI_BOARD = "/board";
        static final String URI_BOT_DETECTION = "/bot-location";
        static final String URI_BALL_COLOUR = "/ball-color";
        static final String URI_REF_COLOUR = "/ref-color";
        static final String URI_BOT_FRONT_COLOUR = "/bot-front";
        static final String URI_BOT_BACK_COLOUR = "/bot-back";
        static final String URI_IMAGE = "/image";

        static final String HTML = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=windows-1252\">\n" +
                "\t\n" +
                "\t<style type=\"text/css\">\n" +
                "\t\t.control{\n" +
                "\t\t\tfloat: right;\n" +
                "\t\t}\n" +
                "\t</style>\n" +
                "</head>\n" +
                "<body cz-shortcut-listen=\"true\">\n" +
                "<img src=\"/image/\" width=\"760\" height=\"570\" id=\"image\">\n" +
                "<div class=\"control\">\n" +
                "\tFrame Number: <span id=\"frame_seq\">0</span>\n" +
                "\t<ul>\n" +
                "\t\t<li><a href=\"/raw/\">Raw</a></li>\n" +
                "\t\t<li><a href=\"/detection/\">Detection</a></li>\n" +
                "\t\t<li><a href=\"/ball-color/\">Ball color</a></li>\n" +
                "\t\t<li><a href=\"/ref-color/\">Reference color</a></li>\n" +
                "\t\t<li><a href=\"/bot-location/\">BOT location</a></li>\n" +
                "\t\t<li><a href=\"/bot-front/\">BOT front color</a></li>\n" +
                "\t\t<li><a href=\"/bot-back/\">BOT back color</a></li>\n" +
                "\t\t<li><a href=\"/balls/\">Balls</a></li>\n" +
                "\t\t<li><a href=\"/board/\">Board</a></li>\n" +
                "\t</ul>\n" +
                "</div>\n" +
                "<script>\n" +
                "var myImageElement = document.getElementById('image');\n" +
                "var frameSeqElement = document.getElementById('frame_seq');\n" +
                "var frame_seq = 0;\n" +
                "function imageLoaded() {\n" +
                "\tframe_seq++; \n" +
                "\tframeSeqElement.innerText = frame_seq\n" +
                "    myImageElement.src = '/image/?frame_seq=' + frame_seq;\n" +
                "} \n" +
                "myImageElement.addEventListener('load', imageLoaded)\n" +
                "</script>\n" +
                "\n" +
                "</body></html>";
    }

    public class Error {
        public static final String KEY = "err";
        static final String INVALID_URI = "Invalid URI provided";
    }
    public static final String FAV_ICON = "/favicon.ico";
}
