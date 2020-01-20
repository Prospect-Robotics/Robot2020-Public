package com.team2813.lib.motors;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.team2813.lib.ctre.CTREException;
import com.team2813.lib.ctre.TimeoutMode;
import com.team2813.lib.motors.interfaces.ControlMode;
import com.team2813.lib.motors.interfaces.LimitDirection;

public class TalonWrapper extends TalonFX implements Motor {
    private TimeoutMode timeoutMode = TimeoutMode.CONSTRUCTING;
    public String subsystemName;

    /**
     * Constructor for TalonSRX object
     *
     * @param deviceNumber CAN Device ID of Device
     */
    public TalonWrapper(int deviceNumber, String subsystem) {
        super(deviceNumber);
        this.subsystemName = subsystem;
        System.out.println("Initializing Talon with ID " + deviceNumber);
    }

    @Override
    public Object set(ControlMode controlMode, double demand) {
        set(controlMode.getTalonMode(), demand);
        return null;
    }

    @Override
    public Object set(ControlMode controlMode, double demand, double feedForward) {
        set(controlMode.getTalonMode(), demand, DemandType.ArbitraryFeedForward, feedForward);
        return null;
    }

    @Override
    public ErrorCode setPosition(double position) {
        return setSelectedSensorPosition(0);
    }

    @Override
    public double getPosition() {
        return getSelectedSensorPosition();
    }

    @Override
    public ErrorCode setFactoryDefaults() {
        return configFactoryDefault();
    }

    @Override
    public ErrorCode setPeakCurrentLimit(double amps) {
//        return configPeakCurrentLimit((int) amps);
        return null;
    }

    @Override
    public void enableVoltageCompensation() {
        enableVoltageCompensation(true);
    }

    @Override
    public ErrorCode setStatusFramePeriod(StatusFrame statusFrame, int period) {
        return super.setStatusFramePeriod(statusFrame, period);
    }

    @Override
    public ErrorCode setSoftLimit(LimitDirection direction, double limit, boolean enable) {
        try {
            if (direction == LimitDirection.FORWARD) {
                CTREException.throwIfNotOk(configForwardSoftLimitThreshold((int) limit));
                CTREException.throwIfNotOk(configForwardSoftLimitEnable(enable));
            } else {
                CTREException.throwIfNotOk(configReverseSoftLimitThreshold((int) limit));
                CTREException.throwIfNotOk(configReverseSoftLimitEnable(enable));
            }
            return ErrorCode.OK;
        } catch (CTREException e) {
            return e.getErrorCode();
        }
    }

    @Override
    public ErrorCode setSoftLimit(LimitDirection direction, double limit) {
        return setSoftLimit(direction, limit, true);
    }

    @Override
    public void setPIDF(int slot, double p, double i, double d, double f) {
        config_kP(slot, p);
        config_kP(slot, i);
        config_kP(slot, d);
        config_kF(slot, f);
    }

    @Override
    public void setPID(int slot, double p, double i, double d) {
        setPIDF(slot, p, i, d, 0);
    }

    @Override
    public void setPIDF(double p, double i, double d, double f) {
        setPIDF(0, p, i, d, f);
    }

    @Override
    public void setPID(double p, double i, double d) {
        setPID(0, p, i, d);
    }

    @Override
    public ErrorCode setMotionMagicVelocity(double velocity) {
        return configMotionCruiseVelocity((int) velocity);
    }

    @Override
    public ErrorCode setMotionMagicAcceleration(double acceleration) {
        return configMotionAcceleration((int) acceleration);
    }

    @Override
    public String getSubsystemName() {
        return subsystemName;
    }

    public Object setContinuousCurrentLimit(int limitAmps) {
//        return super.configContinuousCurrentLimit(limitAmps);
        return false;
    }

    public void enableLimitSwitches() {
        overrideLimitSwitchesEnable(true);
    }

    public Object setClosedLoopRamp(double rate) {
        return super.configClosedloopRamp(rate);
    }

    /**
     * Reset selected feedback sensor (encoder) on a limit
     *
     * @param direction
     * @param clearOnLimit
     */
    public void setClearPositionOnLimit(LimitDirection direction, boolean clearOnLimit) {
        if (direction == LimitDirection.FORWARD) setClearPositionOnLimitF(clearOnLimit);
        else if (direction == LimitDirection.REVERSE) setClearPositionOnLimitR(clearOnLimit);
    }

    public void setClearPositionOnLimitF(boolean enable) {
        configClearPositionOnLimitF(enable, timeoutMode.valueMs);
    }

    public void setClearPositionOnLimitR(boolean enable) {
        configClearPositionOnLimitF(enable, timeoutMode.valueMs);
    }

    public void setLimitSwitchSource(LimitDirection direction, LimitSwitchSource type, LimitSwitchNormal normalOpenOrClose) {
        if (direction == LimitDirection.FORWARD) {
            configForwardLimitSwitchSource(type, normalOpenOrClose);
        } else if (direction == LimitDirection.REVERSE) {
            configReverseLimitSwitchSource(type, normalOpenOrClose);
        }
    }

    public enum PIDProfile {
        PRIMARY(0),
        SECONDARY(1);

        public final int id;

        private PIDProfile(int id) {
            this.id = id;
        }
    }
}
