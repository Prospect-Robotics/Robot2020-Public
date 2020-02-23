package com.team2813.frc2020.util;

import com.team2813.lib.util.LimelightValues;

public class Limelight {
    private LimelightValues values = new LimelightValues();
    private final double kP = .6;
    private static final double CORRECTION_MAX_STEER_SPEED = 1;
    private static final double MIN_CORRECTION_STEER = 0.08;
    private static final double MOUNT_ANGLE = 15; // in degrees
    private static final double MOUNT_HEIGHT = 35; // in inches
    private static final double TARGET_HEIGHT = 98.25; // in inches

    private static double angleAccum = -1;
    private static double times = 0;

    private Limelight() {}

    private static Limelight instance = new Limelight();

    public static Limelight getInstance() {
        return instance;
    }

    public double getSteer() {
        if (Math.abs(values.getTx()) > 0.35) {
            double sign = Math.abs(values.getTx()) / values.getTx();
            return (values.getTx() / 27) * kP * CORRECTION_MAX_STEER_SPEED + (sign * MIN_CORRECTION_STEER);
        }
        return (values.getTx() / 27) * kP * CORRECTION_MAX_STEER_SPEED;
    }

    public void resetDistance() {
        angleAccum = -1;
        times = 0;
    }

    public double getDistance() {
        double limelightAngle = values.getTy().getDouble(0); // filters noise by averaging out numbers
        if (angleAccum == -1) // first time calling
            angleAccum = limelightAngle;
        else angleAccum = ((angleAccum * times) + limelightAngle) / (times + 1);
        times++;
        System.out.println(angleAccum);
        return (TARGET_HEIGHT - MOUNT_HEIGHT) / Math.tan(MOUNT_ANGLE + angleAccum);
    }

    public void setLights(boolean enable) {
        values.getLedMode().setNumber(enable ? 0 : 1);
    }

    public LimelightValues getValues() {
        return values;
    }
}
