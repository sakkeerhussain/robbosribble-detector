package com.anonymous.balldetector.server.response;

import com.anonymous.balldetector.models.Circle;

import java.util.List;

/**
 * Created by sakkeer on 07/12/17.
 */

public class RespBalls extends RespSuccess {

    public List<Circle> data;

    public RespBalls(List<Circle> balls){
        super("Balls list");
        this.data = balls;
    }
}
