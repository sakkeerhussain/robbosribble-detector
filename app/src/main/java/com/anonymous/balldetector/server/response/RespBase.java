package com.anonymous.balldetector.server.response;

import com.anonymous.balldetector.server.Const;
import com.google.gson.GsonBuilder;

/**
 * Created by sakkeer on 07/12/17.
 */

public class RespBase {
    String status;
    String message;

    RespBase(String status, String message){
        this.status = status;
        this.message = message;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
