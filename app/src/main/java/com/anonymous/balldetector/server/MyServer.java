package com.anonymous.balldetector.server;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by sakkeer on 04/12/17.
 */

public class MyServer extends NanoHTTPD {
    private final static int PORT = 8080;

    MyServer() throws IOException {
        super(PORT);
        start();
    }

    @Override
    public Response serve(IHTTPSession session) {
        return ServerManager.get().processExtRequest(session);
    }
}
