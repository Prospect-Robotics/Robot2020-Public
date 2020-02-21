package com.team2813.frc2020.util;

import com.team2813.lib.util.LimelightValues;

public class Limelight {
    private LimelightValues values = new LimelightValues();
    private final double kP = .56;
    private static final double CORRECTION_MAX_STEER_SPEED = 1;
    private static final double MIN_CORRECTION_STEER = 0.08;

    public double getSteer() {
        if (Math.abs(values.getTx()) > 0.35) {
            double sign = Math.abs(values.getTx()) / values.getTx();
            return (values.getTx() / 27) * kP * CORRECTION_MAX_STEER_SPEED + (sign * MIN_CORRECTION_STEER);
        }
        return 0;
    }

    public void setLights(boolean enable) {
        values.getLedMode().setNumber(enable ? 0 : 1);
    }

    public LimelightValues getValues() {
        return values;
    }
}
