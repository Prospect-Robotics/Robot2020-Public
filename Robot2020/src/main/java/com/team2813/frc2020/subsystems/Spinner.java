package com.team2813.frc2020.subsystems;

import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Button;
import com.team2813.lib.motors.SparkMaxWrapper;
import com.team2813.lib.motors.interfaces.ControlMode;

public class Spinner extends Subsystem {


    private final SparkMaxWrapper MOTOR;
    private final Button FORWARD_BUTTON = SubsystemControlsConfig.getSpinnerButton();
    private final Button REVERSE_BUTTON = SubsystemControlsConfig.getSpinnerReverse();
    private Demand demand;

    public Spinner() {
        MOTOR = MotorConfigs.sparks.get("spinner");
    }

    @Override
    public void outputTelemetry() {

    }

    @Override
    public void teleopControls() {
        FORWARD_BUTTON.whenPressedReleased(() -> demand = Demand.FWD, () -> demand = Demand.OFF);
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
    public void writePeriodicOutputs() {
        MOTOR.set(ControlMode.DUTY_CYCLE, demand.percent);
    }

    enum Demand {
        FWD(0.5), OFF(0.0), REV(-0.5);

        double percent;

        Demand(double percent) {
            this.percent = percent;
        }
    }

}
