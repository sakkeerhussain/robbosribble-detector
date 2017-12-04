package com.anonymous.balldetector.server;

import java.io.IOException;

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
}
