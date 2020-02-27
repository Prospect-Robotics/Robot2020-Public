package com.team2813.frc2020.util;

import com.team2813.lib.util.LimelightValues;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Limelight {
    private LimelightValues values = new LimelightValues();
    private final double kP = 0.5;//was 0.56
    private static final double CORRECTION_MAX_STEER_SPEED = 1;
    private static final double MIN_CORRECTION_STEER = 0.05;
    private static final double MOUNT_ANGLE = 15; // in degrees
    private static final double MOUNT_HEIGHT = 35; // in inches
    private static final double TARGET_HEIGHT = 98.25; // in inches

    private Limelight() {}

    private static Limelight instance = new Limelight();

    public static Limelight getInstance() {
        return instance;
    }

    public double getSteer() {
        if (Math.abs(values.getTx()) > 0.35) {
            double sign = Math.abs(values.getTx()) / values.getTx();
            return (((values.getTx()) / 27) * kP * CORRECTION_MAX_STEER_SPEED + (sign * MIN_CORRECTION_STEER));
        }
        return 0;
    }

    public double getVertAngle() {
        return values.getTy();
    }

    public boolean targetFound() {
        return values.getTv().getNumber(0).intValue() == 1;
    }

    public void setLights(boolean enable) {
        values.getLedMode().setNumber(enable ? 0 : 1);
    }

    public LimelightValues getValues() {
        return values;
    }
}
