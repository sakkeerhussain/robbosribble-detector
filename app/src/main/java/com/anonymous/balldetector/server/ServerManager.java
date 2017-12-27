package com.anonymous.balldetector.server;

import android.graphics.Bitmap;
import android.util.Log;

import com.anonymous.balldetector.opencv.OpenCVManager;
import com.anonymous.balldetector.opencv.OpenCVUtils;
import com.anonymous.balldetector.server.response.RespBase;
import com.anonymous.balldetector.server.response.RespError;
import com.anonymous.balldetector.server.response.RespSuccess;

import org.opencv.core.Mat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;


/**
 * Created by sakkeer on 04/12/17.
 */

public class ServerManager {
    private static final String TAG = "ServerManager";

    private static ServerManager instance;

    private ServerManager() {
        try {
            server = new MyServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ServerManager get() {
        if (instance == null) {
            instance = new ServerManager();
        }
        return instance;
    }

    //fields
    private MyServer server;

    //methods
    public void startServer() {
        Log.d(TAG, "Starting server");
        try {
            server.start();
        } catch (IOException ignored) {
        }

    }

    public void stopServer() {
        Log.d(TAG, "Stopping server");
        if (server != null) {
            server.stop();
        }
    }

    NanoHTTPD.Response processExtRequest(NanoHTTPD.IHTTPSession session) {
        String uri = session.getUri();
        NanoHTTPD.Method method = session.getMethod();
        Log.d(TAG, "HTTP "+method.name()+" ("+session.getRemoteIpAddress()+") "+uri);
        Map<String, List<String>> params = session.getParameters();
        String body = session.getQueryParameterString();
        String response;
        if (uri.startsWith(Const.Calibrate.URI)) {
            response = processCalibrate(uri.substring(Const.Calibrate.URI.length()), method, params, body).toString();
        }else if (uri.startsWith(Const.Stream.URI)) {
            return processStream(uri.substring(Const.Stream.URI.length()), method, params, body);
        }else {
            response = new RespError(Const.Error.INVALID_URI).toString();
        }
        return NanoHTTPD.newFixedLengthResponse( response );
    }

    //Calibration methods
    private RespBase processCalibrate(String uri, NanoHTTPD.Method method, Map<String, List<String>> params, String body) {
        if (uri.startsWith(Const.Calibrate.URI_REF_POINT)) {
            uri = uri.substring(Const.Calibrate.URI_REF_POINT.length());
            String response = null;
            if (uri.startsWith(Const.Calibrate.URI_1)) {
                response = OpenCVUtils.updateRefPoints(1);
            } else if (uri.startsWith(Const.Calibrate.URI_2)) {
                response = OpenCVUtils.updateRefPoints(2);
            } else if (uri.startsWith(Const.Calibrate.URI_3)) {
                response = OpenCVUtils.updateRefPoints(3);
            } else if (uri.startsWith(Const.Calibrate.URI_4)) {
                response = OpenCVUtils.updateRefPoints(4);
            }

            if (response != null) {
                if (response.equals(Const.SUCCESS)) {
                    return new RespSuccess("Configured reference pint.");
                } else {
                    return new RespError(response);
                }
            }
        }
        return new RespError(Const.Error.INVALID_URI);
    }

    //Stream methods
    private NanoHTTPD.Response processStream(String uri, NanoHTTPD.Method method, Map<String, List<String>> params, String body) {
        if (uri.startsWith(Const.Stream.URI_RAW)) {
            uri = uri.substring(Const.Stream.URI_RAW.length());
            if (uri.length() == 0 || uri.equals("/")) {
                return NanoHTTPD.newFixedLengthResponse(Const.Stream.HTML);
            } else if (uri.startsWith(Const.Stream.URI_IMAGE)){
                return getRawStream();
            }
        }else if (uri.startsWith(Const.Stream.URI_DETECTION)) {
            uri = uri.substring(Const.Stream.URI_DETECTION.length());
            if (uri.length() == 0 || uri.equals("/")) {
                return NanoHTTPD.newFixedLengthResponse(Const.Stream.HTML);
            } else if (uri.startsWith(Const.Stream.URI_IMAGE)){
                return getDetectionStream();
            }
        }else if (uri.startsWith(Const.Stream.URI_BALL_COLOUR)) {
            uri = uri.substring(Const.Stream.URI_BALL_COLOUR.length());
            if (uri.length() == 0 || uri.equals("/")) {
                return NanoHTTPD.newFixedLengthResponse(Const.Stream.HTML);
            } else if (uri.startsWith(Const.Stream.URI_IMAGE)){
                return getBallColourStream();
            }
        }else if (uri.startsWith(Const.Stream.URI_REF_COLOUR)) {
            uri = uri.substring(Const.Stream.URI_REF_COLOUR.length());
            if (uri.length() == 0 || uri.equals("/")) {
                return NanoHTTPD.newFixedLengthResponse(Const.Stream.HTML);
            } else if (uri.startsWith(Const.Stream.URI_IMAGE)){
                return getReferenceColourStream();
            }
        }
        return NanoHTTPD.newFixedLengthResponse(new RespError(Const.Error.INVALID_URI).toString());
    }

    private NanoHTTPD.Response getRawStream() {
        Mat frame = OpenCVManager.get().getRGBFrame();
        return getFrameAsStream(frame);
    }

    private NanoHTTPD.Response getDetectionStream() {
        Mat frame = OpenCVManager.get().getRGBFrame();
        OpenCVUtils.drawBallsToFrame(frame);
        OpenCVUtils.drawRefPointsToFrame(frame);
        OpenCVUtils.drawBordToFrame(frame);
        return getFrameAsStream(frame);
    }

    private NanoHTTPD.Response getFrameAsStream(Mat frame) {
        final Bitmap bm = Bitmap.createBitmap(frame.cols(), frame.rows(), Bitmap.Config.ARGB_8888);
        org.opencv.android.Utils.matToBitmap(frame, bm);
        return getImageResponse(bm);
    }

    private NanoHTTPD.Response getBallColourStream() {
        Mat frame = OpenCVManager.get().getRGBFrame();
        OpenCVUtils.updateDisplayType(OpenCVUtils.DISPLAY_BALLS_IN_RANGE, frame);
        return getFrameAsStream(frame);
    }

    private NanoHTTPD.Response getReferenceColourStream() {
        Mat frame = OpenCVManager.get().getRGBFrame();
        OpenCVUtils.updateDisplayType(OpenCVUtils.DISPLAY_REFERENCE_IN_RANGE, frame);
        return getFrameAsStream(frame);
    }

    private NanoHTTPD.Response getImageResponse(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); //use the compression format of your need
        InputStream is = new ByteArrayInputStream(stream.toByteArray());
        return NanoHTTPD.newChunkedResponse(NanoHTTPD.Response.Status.OK, "image/jpeg", is);
    }
}
