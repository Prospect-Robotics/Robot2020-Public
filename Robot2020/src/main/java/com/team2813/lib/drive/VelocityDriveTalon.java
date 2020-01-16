package com.team2813.lib.drive;

import com.team2813.lib.config.PIDControllerConfig;
import com.team2813.lib.config.TalonConfig;
import com.team2813.lib.ctre.CTREException;
import com.team2813.lib.ctre.PIDProfile;
import com.team2813.lib.ctre.TalonWrapper;

public class VelocityDriveTalon {
    private TalonWrapper talon;
    private double maxVelocity;

    public VelocityDriveTalon(double maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    /**
     * Configure a TalonWrapper with configuration for velocity drive.
     * @param talon
     * @param config
     * @throws CTREException
     */
    public void configureMotor(TalonWrapper talon, TalonConfig config) throws CTREException {
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
