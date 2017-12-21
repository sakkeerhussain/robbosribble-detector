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
        } catch (IOException e) {
            e.printStackTrace();
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
        Log.d(TAG, "HTTP "+method.name()+" "+uri);
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

    private NanoHTTPD.Response processStream(String uri, NanoHTTPD.Method method, Map<String, List<String>> params, String body) {
        if (uri.length() <= 1) {
            return NanoHTTPD.newFixedLengthResponse(Const.Stream.HTML.replace("{{type}}", "raw"));
        }else if (uri.startsWith(Const.Stream.URI_RAW)) {
            return getRawStream();
        }
        return NanoHTTPD.newFixedLengthResponse(new RespError(Const.Error.INVALID_URI).toString());
    }

    private RespBase processCalibrate(String uri, NanoHTTPD.Method method, Map<String, List<String>> params, String body) {
        if (uri.startsWith(Const.Calibrate.URI_REF_POINT)) {
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

    private NanoHTTPD.Response getRawStream() {
        Mat frame = OpenCVManager.get().getRGBFrame();
        final Bitmap bm = Bitmap.createBitmap(frame.cols(), frame.rows(), Bitmap.Config.ARGB_8888);
        org.opencv.android.Utils.matToBitmap(frame, bm);
        return getImageResponse(bm);
    }

    private NanoHTTPD.Response getImageResponse(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); //use the compression format of your need
        InputStream is = new ByteArrayInputStream(stream.toByteArray());
        return NanoHTTPD.newChunkedResponse(NanoHTTPD.Response.Status.OK, "image/jpeg", is);
    }
}
