package com.team2813.lib.drive;

import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.util.Units;

public class DriveDemand {
    public static double circumference = 1; // default should be set on init
    private double left;
    private double right;

    public DriveDemand(DifferentialDriveWheelSpeeds wheelSpeeds) { // to rpm
        left = Units.metersToInches(wheelSpeeds.leftMetersPerSecond) * 60 / circumference / (9.0 / 60);
        right = Units.metersToInches(wheelSpeeds.rightMetersPerSecond) * 60 / circumference / (9.0 / 60);
    }

    public DriveDemand(double left, double right) {
        this.left = left;
        this.right = right;
    }

    public DriveDemand reverse() {
        double temp = left;
        left = -right;
        right = -temp;
        return this;
    }

    public double getLeft() {
        return left;
    }

    public double getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "("+ left + ", " + right + ")";
    }
}