package com.anonymous.balldetector.server.response;

import com.anonymous.balldetector.server.Const;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by sakkeer on 07/12/17.
 */

public class RespError extends RespBase {

    public RespError(String message){
        super(Const.Error.KEY, message);
    }
}
