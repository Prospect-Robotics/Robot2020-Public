package com.team2813.lib.drive;

import java.awt.geom.Point2D;

/**
 * @author Sid Banerjee
 * @author Abhineet Pal
 **/
public class Odometry {
    private int originalLeftEncoder;
    private int originalRightEncoder;
    private Point2D.Double location;

    public Point2D.Double getLocation() {
        return location;
    }

    public Odometry(int originalLeft, int originalRight){
        originalLeftEncoder = originalLeft;
        originalRightEncoder = originalRight;
        location = new Point2D.Double();
    }

    public void updateLocation(int currLeft, int currRight, double currAngle){
        int distance = ((currLeft-originalLeftEncoder) + (currRight-originalRightEncoder))/2;
        location.x += distance * Math.cos(currAngle);
        location.y += distance * Math.cos(currAngle);
    }

}
