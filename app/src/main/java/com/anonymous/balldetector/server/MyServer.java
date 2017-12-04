package com.anonymous.balldetector.server;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by sakkeer on 04/12/17.
 */

public class MyServer extends NanoHTTPD {
    private final static int PORT = 8080;

    public MyServer() throws IOException {
        super(PORT);
        start();
        System.out.println( "\nRunning! Point your browers to http://localhost:8080/ \n" );
    }

    @Override
    public Response serve(IHTTPSession session) {
        String msg = "<html><body><h1>Hello server</h1>\n";
        msg += "<p>We serve " + session.getUri() + " !</p>";
        return newFixedLengthResponse( msg + "</body></html>\n" );
    }
}
