package com.team2813.lib.motors;

import com.ctre.phoenix.ErrorCode;
import com.team2813.lib.motors.interfaces.ControlMode;
import com.team2813.lib.motors.interfaces.LimitDirection;

@SuppressWarnings("UnusedReturnValue")
public interface Motor<ErrorType> {
    // motor control
    public ErrorType set(ControlMode controlMode, double demand);
    public ErrorType set(ControlMode controlMode, double demand, double feedForward);

    // encoder position
    public ErrorType setEncoderPosition(double position);
    public double getEncoderPosition();

    // config
    public ErrorType setFactoryDefaults();
    public ErrorType setPeakCurrentLimit(double amps);
    public void enableVoltageCompensation();
    public ErrorType setSoftLimit(LimitDirection direction, double limit, boolean enable);
    public ErrorType setSoftLimit(LimitDirection direction, double limit);

    // pid config
    public void setPIDF(int slot, double p, double i, double d, double f);
    public void setPID(int slot, double p, double i, double d);
    public void setPIDF(double p, double i, double d, double f);
    public void setPID(double p, double i, double d);
    public ErrorType setMotionMagicVelocity(double velocity);
    public ErrorType setMotionMagicAcceleration(double acceleration);

    public String getSubsystemName();
}