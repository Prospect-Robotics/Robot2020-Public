package com.team2813.frc2020.subsystems;

import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Button;
import com.team2813.lib.motors.SparkMaxWrapper;
import com.team2813.lib.motors.VictorWrapper;

public class Intake extends Subsystem {

    private static SparkMaxWrapper MOTOR;
    private static final Button INTAKE_BUTTON = SubsystemControlsConfig.getIntakeButton();
    private static Demand demand;

    Intake(SparkMaxWrapper MOTOR) {
         Intake.MOTOR = MOTOR;
    }


    @Override
    public void outputTelemetry() {

    }

    @Override
    public void teleopControls() {
        INTAKE_BUTTON.whenPressedReleased(() -> demand = Demand.ON, () -> demand = Demand.OFF);
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
    public void onDisabledStop(double timestamp) {

    }

    @Override
    public void writePeriodicOutputs(){
        MOTOR.set(demand.percent);
    }

    enum Demand {
        ON(0.7), OFF(0.0);

        double percent;

        Demand(double percent) {
            this.percent = percent;
        }
    }
}
