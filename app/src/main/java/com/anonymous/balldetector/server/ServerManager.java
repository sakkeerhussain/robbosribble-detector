package com.anonymous.balldetector.server;

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

    private ServerManager(){
        try {
            server = new MyServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ServerManager get(){
        if (instance == null){
            instance = new ServerManager();
        }
        return instance;
    }

    //fields
    private MyServer server;

    //methods
    public void startServer(){
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stopServer(){
        if(server != null) {
            server.stop();
        }
    }

    String processExtRequest(NanoHTTPD.IHTTPSession session) {
        String uri = session.getUri();
        NanoHTTPD.Method method = session.getMethod();
        Map<String, List<String>> params = session.getParameters();
        String body = session.getQueryParameterString();
        if (uri.startsWith(Const.Calibrate.URI)){
            return processCalibrate(uri, method, params, body).toString();
        }
        return new RespError(Const.Error.INVALID_URI).toString();
    }

    private RespBase processCalibrate(String uri, NanoHTTPD.Method method, Map<String, List<String>> params, String body) {
        return new RespSuccess("Success");
    }
}
