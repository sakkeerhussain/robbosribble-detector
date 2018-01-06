package com.anonymous.balldetector.server.response;

import com.anonymous.balldetector.math.geography.Point;

/**
 * Created by sakkeer on 07/12/17.
 */

public class RespBot extends RespSuccess {

    public BotData data;

    public RespBot(Point frontLeft, Point frontRight, Point backLeft, Point backRight){
        super("Bot location");
        this.data = new BotData(frontLeft, frontRight, backLeft, backRight);
    }

    public class BotData {
        public Point frontLeft;
        public Point frontRight;
        public Point backLeft;
        public Point backRight;

        BotData(Point frontLeft, Point frontRight, Point backLeft, Point backRight) {
            this.frontLeft = frontLeft;
            this.frontRight = frontRight;
            this.backLeft = backLeft;
            this.backRight = backRight;
        }
    }
}
