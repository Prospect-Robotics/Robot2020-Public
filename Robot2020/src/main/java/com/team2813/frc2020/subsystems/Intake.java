package com.team2813.frc2020.subsystems;

import com.team2813.lib.config.MotorConfig;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Button;
import com.team2813.lib.motors.SparkMaxWrapper;
import com.team2813.lib.motors.VictorWrapper;
import com.team2813.lib.solenoid.PistonSolenoid;

public class Intake extends Subsystem {

    private SparkMaxWrapper MOTOR;
    private final Button INTAKE_BUTTON = SubsystemControlsConfig.getIntakeButton();
    private final Button PISTONS_BUTTON = SubsystemControlsConfig.getIntakePistons();
    private Demand demand;

    Intake() {
         MOTOR = MotorConfigs.sparks.get("intake");
    }

    private static PistonSolenoid LEFT_PISTON = new PistonSolenoid(0);
    private static PistonSolenoid RIGHT_PISTON = new PistonSolenoid(1);

    @Override
    public void outputTelemetry() {

    }

    @Override
    public void teleopControls() {
        INTAKE_BUTTON.whenPressedReleased(() -> demand = Demand.ON, () -> demand = Demand.OFF);
        PISTONS_BUTTON.whenPressedReleased(() -> LEFT_PISTON.set(PistonSolenoid.PistonState.EXTENDED), () -> LEFT_PISTON.set(PistonSolenoid.PistonState.RETRACTED));
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
