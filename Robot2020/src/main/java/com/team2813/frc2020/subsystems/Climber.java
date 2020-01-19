package com.team2813.frc2020.subsystems;

import com.team2813.lib.ctre.CTREException;
import com.team2813.lib.sparkMax.CANSparkMaxWrapper;
import com.team2813.lib.sparkMax.SparkMaxException;
import com.team2813.lib.config.MotorConfig; 
public class Climber extends Subsystem1d{

    Climber(CANSparkMaxWrapper motor) {
        super(motor);
    }

    @Override
    void setNextPosition(boolean clockwise) {

    }

    @Override
    void setNextPosition(Position position) {

    }

    @Override
    protected boolean checkSystem_() throws CTREException, SparkMaxException {
        return false;
    }

    @Override
    protected void outputTelemetry_() throws CTREException, SparkMaxException {

    }

    @Override
    protected void teleopControls_() throws CTREException, SparkMaxException {

    }

    @Override
    protected void onEnabledStart_(double timestamp) throws CTREException, SparkMaxException {

    }

    @Override
    protected void onEnabledLoop_(double timestamp) throws CTREException, SparkMaxException {

    }

    @Override
    protected void onEnabledStop_(double timestamp) throws CTREException, SparkMaxException {

    }
}
