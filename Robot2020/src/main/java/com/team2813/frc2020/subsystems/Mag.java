package com.team2813.frc2020.subsystems;

import com.team2813.lib.config.MotorConfig;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.motors.SparkMaxWrapper;

public class Mag extends Subsystem1d {
    Mag() {
        super(MotorConfigs.sparks.get("mag"));
    }

    @Override
    void setNextPosition(boolean clockwise) {

    }

    @Override
    void setNextPosition(Position position) {

    }

    @Override
    public void outputTelemetry() {

    }

    @Override
    public void teleopControls() {

    }

    @Override
    public void onEnabledStart(double timestamp) {

    }

    @Override
    public void onEnabledLoop(double timestamp) {

    }

    @Override
    public void onEnabledStop(double timestamp) {

    }
}
