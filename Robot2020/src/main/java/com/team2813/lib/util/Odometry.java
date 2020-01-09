package com.team2813.lib.util;

import java.awt.geom.Point2D;

/**
 * @author Sid Banerjee
 **/

public class Odometry {
    public int leftOdometry;
    public int rightOdometry;
    public int startingLeftOdometry;
    public int startingRightOdometry;
    public Point2D.Double location = new Point2D.Double();

    public Odometry(int leftOdometry, int rightOdometry, int startingLeftOdometry, int startingRightOdometry){
        this.leftOdometry = leftOdometry;
        this.rightOdometry = rightOdometry;
        this.startingLeftOdometry = startingLeftOdometry;
        this.startingRightOdometry = startingRightOdometry;
    }

    public int getXLocation(){
        int distance = (Math.abs(leftOdometry-startingLeftOdometry)+Math.abs(rightOdometry-startingRightOdometry))/2;
        location.x += distance * Math.cos();
    }
    public int getYLocation(){
        int distance = (Math.abs(leftOdometry-startingLeftOdometry)+Math.abs(rightOdometry-startingRightOdometry))/2;
        location.y += distance
    }
}
