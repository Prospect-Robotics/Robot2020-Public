package com.team2813.lib.util;

import java.awt.geom.Point2D;

/**
 * @author Sid Banerjee
 **/

public class Odometry {
    private int previousLeftEncoder;
    private int previousRightEncoder;

    public Point2D.Double getLocation() {
        return location;
    }

    private Point2D.Double location;

    public Odometry(int currentLeft, int currentRight){
        previousLeftEncoder = currentLeft;
        previousRightEncoder = currentRight;
        location = new Point2D.Double();
    }

    public void updateLocation(int currLeft, int currRight, double currAngle){
        int distance = (Math.abs(currLeft-previousLeftEncoder)+Math.abs(currRight-previousRightEncoder))/2;
        location.x += distance * Math.cos(currAngle);
        location.y += distance * Math.cos(currAngle);
        previousLeftEncoder = currLeft;
        previousRightEncoder = currRight;
    }

}
