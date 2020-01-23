package com.team2813.frc2020.subsystems;

import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Button;
import com.team2813.lib.motors.SparkMaxWrapper;
import com.team2813.lib.motors.interfaces.ControlMode;

public class Mag extends Subsystem {

    private final SparkMaxWrapper MOTOR;
    private final Button START_STOP_BUTTON = SubsystemControlsConfig.getMagButton();
    private final Button REVERSE_BUTTON = SubsystemControlsConfig.getMagReverse();
    private Demand demand;

    Mag() {
        MOTOR = MotorConfigs.sparks.get("mag");
        demand = Demand.OFF;
    }

    @Override
    public void outputTelemetry() {
    }

    @Override
    public void teleopControls() {
        START_STOP_BUTTON.whenPressed(() -> {
            demand = demand == Demand.ON ? Demand.OFF : Demand.ON;
        });
        REVERSE_BUTTON.whenPressedReleased(() -> demand = Demand.REV, () -> demand = Demand.OFF);
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

    @Override
    protected void writePeriodicOutputs() {
        MOTOR.set(ControlMode.DUTY_CYCLE, demand.percent);
    }

    enum Demand {
        ON(0.5), OFF(0.0), REV(-0.3);

        double percent;

        Demand(double percent) {
            this.percent = percent;
        }
    }
}