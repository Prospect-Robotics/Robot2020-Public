package com.team2813.frc2020.subsystems;

import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Button;
import com.team2813.lib.motors.SparkMaxWrapper;
import com.team2813.lib.solenoid.PistonSolenoid;

public class Intake extends Subsystem {

    private SparkMaxWrapper INTAKE_MOTOR;
    private final Button INTAKE_BUTTON = SubsystemControlsConfig.getIntakeButton();
    private final Button PISTONS_BUTTON = SubsystemControlsConfig.getIntakePistons();
    private final Button INTAKE_IN_BUTTON = SubsystemControlsConfig.getIntakeIn();
    private final Button INTAKE_OUT_BUTTON = SubsystemControlsConfig.getIntakeOut();
    private Demand demand;

    Intake() {
        INTAKE_MOTOR = MotorConfigs.sparks.get("intake");
    }

    private static PistonSolenoid LEFT_PISTON = new PistonSolenoid(0);
    private static PistonSolenoid RIGHT_PISTON = new PistonSolenoid(1);

    private boolean deployed = false;

    @Override
    public void outputTelemetry() {

    }

    @Override
    public void teleopControls() {
        // driver
        INTAKE_BUTTON.whenPressedReleased(() -> {
            setIntake(Demand.IN);
            setDeployed(true);
        }, () -> {
            setIntake(Demand.OFF);
            setDeployed(false);
        });

        // operator
        PISTONS_BUTTON.whenPressed(() -> setDeployed(!deployed));
        INTAKE_IN_BUTTON.whenPressedReleased(() -> setIntake(Demand.IN), () -> setIntake(Demand.OFF));
        INTAKE_OUT_BUTTON.whenPressedReleased(() -> setIntake(Demand.OUT), () -> setIntake(Demand.OFF));
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
    public void writePeriodicOutputs() {
        INTAKE_MOTOR.set(demand.percent);
    }

    public enum Demand {
        IN(0.7), OFF(0.0), OUT(-0.7);

        double percent;

        Demand(double percent) {
            this.percent = percent;
        }
    }

    public boolean getDeployed() {
        return deployed;
    }

    public void setDeployed(boolean deployed) {
        this.deployed = deployed;
        if (deployed) {
            LEFT_PISTON.set(PistonSolenoid.PistonState.EXTENDED);
            RIGHT_PISTON.set(PistonSolenoid.PistonState.EXTENDED);
        } else {
            LEFT_PISTON.set(PistonSolenoid.PistonState.RETRACTED);
            RIGHT_PISTON.set(PistonSolenoid.PistonState.RETRACTED);
        }
    }

    public void setIntake(Demand demand) {
        this.demand = demand;
    }
}
