package com.team2813.frc2020.subsystems;

import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Button;
import com.team2813.lib.motors.SparkMaxWrapper;
import com.team2813.lib.motors.VictorWrapper;

public class Intake extends Subsystem {

    private static final SparkMaxWrapper MOTOR = MotorConfigs.sparks.get("intake");
    private static final double ROLLER_SPEED = .8; // # from OffSeasonBot2019 -- is this the right value?
    private static final Button INTAKE_BUTTON = SubsystemControlsConfig.getIntakeButton();
    private static double demand;


    @Override
    public void outputTelemetry() {

    }

    @Override
    public void teleopControls() {
        INTAKE_BUTTON.whenPressedReleased(() -> demand = ROLLER_SPEED, () -> demand = 0.0);
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
}
