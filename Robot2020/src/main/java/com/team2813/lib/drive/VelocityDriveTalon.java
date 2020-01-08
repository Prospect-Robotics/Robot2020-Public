package com.team2813.lib.drive;

import com.team2813.lib.config.PIDControllerConfig;
import com.team2813.lib.config.SparkConfig;
import com.team2813.lib.ctre.CTREException;
import com.team2813.lib.sparkMax.CANSparkMaxWrapper;
import com.team2813.lib.sparkMax.SparkMaxException;
import com.team2813.lib.ctre.TalonWrapper;
import com.team2813.lib.ctre.PIDProfile;
public class VelocityDriveTalon {
    private TalonWrapper talon;
    private double maxVelocity;

    public VelocityDriveTalon(double maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    /**
     * Configure a CANSparkMaxWrapper with configuration for velocity drive.
     * @param talon
     * @param config
     * @throws SparkMaxException
     */
    public void configureMotor(TalonWrapper talon, SparkConfig config) throws SparkMaxException, CTREException {
        this.talon = talon;
        PIDControllerConfig pidConfig = config.getPidControllers().get(0);
        talon.setPIDF(PIDProfile.Profile.PRIMARY, pidConfig.getP(), pidConfig.getI(), pidConfig.getD(), pidConfig.getF());
        talon.setMotionMagicCruiseVelocity(0);
        talon.setMotionMagicAcceleration(0);
//        talon.getPIDController().setSmartMotionMinOutputVelocity(0, 0);
//        talon.getPIDController().setSmartMotionAllowedClosedLoopError(pidConfig.getAllowableClosedLoopError(), 0);
    }

    public void setMaxVelocity(int maxVelocity) throws CTREException {
        if (maxVelocity != this.maxVelocity)
            talon.setMotionMagicCruiseVelocity(0);
        this.maxVelocity = maxVelocity;
    }

    public double getVelocityFromDemand(double demand) {
        return maxVelocity * demand;
    }
}
