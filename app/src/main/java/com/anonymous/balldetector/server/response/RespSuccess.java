package com.anonymous.balldetector.server.response;

import com.anonymous.balldetector.server.Const;

/**
 * Created by sakkeer on 07/12/17.
 */

public class RespSuccess extends RespBase {

    public RespSuccess(String message){
        super(Const.SUCCESS, message);
    }
}
