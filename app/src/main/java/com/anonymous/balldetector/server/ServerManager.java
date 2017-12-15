package com.anonymous.balldetector.server;

import com.anonymous.balldetector.opencv.OpenCVUtils;
import com.anonymous.balldetector.server.response.RespBase;
import com.anonymous.balldetector.server.response.RespError;
import com.anonymous.balldetector.server.response.RespSuccess;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by sakkeer on 04/12/17.
 */

public class ServerManager {

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
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stopServer() {
        if (server != null) {
            server.stop();
        }
    }

    String processExtRequest(NanoHTTPD.IHTTPSession session) {
        String uri = session.getUri();
        NanoHTTPD.Method method = session.getMethod();
        Map<String, List<String>> params = session.getParameters();
        String body = session.getQueryParameterString();
        if (uri.startsWith(Const.Calibrate.URI)) {
            return processCalibrate(uri.substring(Const.Calibrate.URI.length()), method, params, body).toString();
        }
        return new RespError(Const.Error.INVALID_URI).toString();
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
}
