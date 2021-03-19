package com.team2813.lib.motors;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANError;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.team2813.lib.config.SparkConfig;
import com.team2813.lib.ctre.TimeoutMode;
import com.team2813.lib.motors.interfaces.ControlMode;
import com.team2813.lib.motors.interfaces.LimitDirection;
import com.team2813.lib.sparkMax.SparkMaxException;

@SuppressWarnings("UnusedReturnValue")
public class SparkMaxWrapper extends CANSparkMax implements Motor {
    public String subsystemName;
    private SparkConfig config;
    private CANEncoder encoder = getEncoder();
    private CANPIDController pid = getPIDController();

    /**
     * Create a new SPARK MAX Controller
     *
     * @param deviceID The device ID.
     * @param type     The motor type connected to the controller. Brushless motors
     *                 must be connected to their matching color and the hall sensor
     *                 plugged in. Brushed motors must be connected to the Red and
     */
    public SparkMaxWrapper(int deviceID, String subsystem, MotorType type) {
        super(deviceID, type);
        this.subsystemName = subsystem;
    }

    public SparkMaxWrapper(int deviceID, MotorType type, SparkMaxWrapper master, boolean invert) {
        super(deviceID, type);
        this.subsystemName = master.subsystemName;
        this.follow(master, invert);
    }

    @Override
    public CANError set(ControlMode controlMode, double demand) {
        return pid.setReference(demand, controlMode.getSparkMode());
    }

    @Override
    public CANError set(ControlMode controlMode, double demand, double feedForward) {
        return pid.setReference(demand, controlMode.getSparkMode(), 0, feedForward);
    }

    @Override
    public CANError setEncoderPosition(double position) {
        return encoder.setPosition(position);
    }

    @Override
    public double getEncoderPosition() {
        return encoder.getPosition();
    }

    @Override
    public double getVelocity() {
        return encoder.getVelocity();
    }

    @Override
    public CANError setFactoryDefaults() {
        return restoreFactoryDefaults();
    }

    @Override
    public CANError setPeakCurrentLimit(double amps) {
        return super.setSmartCurrentLimit((int) amps);
    }

    @Override
    public void enableVoltageCompensation() {
        enableVoltageCompensation(0);
    }

    @Override
    public CANError setSoftLimit(LimitDirection direction, double limit, boolean enable) {
        try {
            SparkMaxException.throwIfNotOk(enableSoftLimit(direction.getSparkMode(), enable));
            SparkMaxException.throwIfNotOk(setSoftLimit(direction, limit));
        } catch (SparkMaxException e) {
            return e.getError();
        }
        return CANError.kOk;
    }

    @Override
    public CANError setSoftLimit(LimitDirection direction, double limit) {
        return super.setSoftLimit(direction.getSparkMode(), (float) limit);
    }

    public double getSoftLimit(LimitDirection direction) {
        return super.getSoftLimit(direction.getSparkMode());
    }

    @Override
    public void setPIDF(int slot, double p, double i, double d, double f) {
        getPIDController().setP(p, slot);
        getPIDController().setI(i, slot);
        getPIDController().setD(d, slot);
        getPIDController().setFF(f, slot);
    }

    @Override
    public void setPID(int slot, double p, double i, double d) {
        setPIDF(slot, p, i, d, 0);
    }

    @Override
    public void setPIDF(double p, double i, double d, double f) {
        setPIDF(0, p, i, d, 0);
    }

    @Override
    public void setPID(double p, double i, double d) {
        setPIDF(p, i, d, 0);
    }

    public CANError setMotionMagicVelocity(int slot, double velocity) {
        return getPIDController().setSmartMotionMaxVelocity(velocity, slot);
    }

    @Override
    public CANError setMotionMagicVelocity(double velocity) {
        return setMotionMagicVelocity(0, velocity);
    }

    public CANError setMotionMagicAcceleration(int slot, double acceleration) {
        return getPIDController().setSmartMotionMaxVelocity(acceleration, slot);
    }

    @Override
    public CANError setMotionMagicAcceleration(double acceleration) {
        return setMotionMagicAcceleration(0, acceleration);
    }

    @Override
    public String getSubsystemName() {
        return subsystemName;
    }

    public CANError setAllowableClosedLoopError(int slot, double error) {
        return getPIDController().setSmartMotionAllowedClosedLoopError(error, slot);
    }

    public CANError setAllowableClosedLoopError(double error) {
        return setAllowableClosedLoopError(0, error);
    }

    public CANError setMotionMagicMinOutputVelocity(int slot, double minOuput) {
        return getPIDController().setSmartMotionMinOutputVelocity(minOuput, slot);
    }

    /**
     * Set the rate of transmission for periodic frames from the SPARK MAX
     *
     * Each motor controller sends back three status frames with different data at
     * set rates. Use this function to change the default rates.
     *
     * Defaults: Status0 - 10ms Status1 - 20ms Status2 - 50ms
     *
     * This value is not stored in the FLASH after calling burnFlash() and is reset
     * on powerup.
     *
     * Refer to the SPARK MAX reference manual on details for how and when to
     * configure this parameter.
     *
     * @param frameID  The frame ID can be one of PeriodicFrame type
     * @param periodMs The rate the controller sends the frame to the controller.
     *
     */
    public void setPeriodicFrame(PeriodicFrame frameID, int periodMs) {
        setPeriodicFramePeriod(frameID, periodMs);
    }

    public void setPeriodicFrame(PeriodicFrame frameID) {
        setPeriodicFrame(frameID, TimeoutMode.RUNNING.valueMs);
    }

    public CANError setNeutralMode(IdleMode mode) {
        return setIdleMode(mode);
    }

    public void setConfig(SparkConfig config) {
        this.config = config;
    }

    public SparkConfig getConfig() {
        return config;
    }

    public enum InvertType {
        NORMAL(false), FOLLOW_LEADER(false), OPPOSE_LEADER(true), INVERTED(true);

        public boolean inverted;

        InvertType(boolean inverted) {
            this.inverted = inverted;
        }
    }
}
