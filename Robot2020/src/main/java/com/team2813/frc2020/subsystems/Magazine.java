package com.team2813.frc2020.subsystems;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.ParamEnum;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Button;
import com.team2813.lib.motors.SparkMaxWrapper;
import com.team2813.lib.motors.interfaces.ControlMode;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static com.team2813.frc2020.subsystems.Subsystems.SHOOTER;

public class Magazine extends Subsystem {

    private final SparkMaxWrapper MOTOR;
    private final CANifier INTAKE_COUNTER;
    private final Joystick OPERATOR_JOYSTICK = SubsystemControlsConfig.getOperatorJoystick();
    private final Button START_STOP_BUTTON = SubsystemControlsConfig.getMagButton();
    private final Button REVERSE_BUTTON = SubsystemControlsConfig.getMagReverse();
    private Demand demand;

    public int ammo = 0;
    public boolean triggered = false;

    Magazine() {
        MOTOR = MotorConfigs.sparks.get("magazine");
        demand = Demand.OFF;
        INTAKE_COUNTER = new CANifier(14);
    }

    public void spinMagazineForward() {
        demand = Demand.ON;
        SHOOTER.setKicker(Shooter.KickerDemand.ON);
    }

    public void spinMagazineIntake() {
        demand = Demand.INTAKE;
        SHOOTER.setKicker(Shooter.KickerDemand.REV);
    }

    public void spinMagazineReverse() {
        demand = Demand.REV;
        SHOOTER.setKicker(Shooter.KickerDemand.REV);
    }

    public void stopMagazine() {
        demand = Demand.OFF;
        SHOOTER.setKicker(Shooter.KickerDemand.OFF);
    }

    public boolean isCounterBlocked() {
        return INTAKE_COUNTER.getGeneralInput(CANifier.GeneralPin.SDA);
    }

    @Override
    public void outputTelemetry() {
        SmartDashboard.putBoolean("Ball Detected", isCounterBlocked());
    }

    @Override
    public void teleopControls() {
        START_STOP_BUTTON.whenPressedReleased(this::spinMagazineForward, this::stopMagazine);
        REVERSE_BUTTON.whenPressedReleased(this::spinMagazineReverse, this::stopMagazine);
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
    protected void readPeriodicInputs() {
        if (isCounterBlocked()) {
            if (ammo < 5 && !triggered) { // block only runs once
                ammo++;
            }
            if (ammo == 5) { // if there is more than 5 balls
                spinMagazineReverse(); // spit it out
            } else {
                spinMagazineIntake(); // take it in
            }
            triggered = true;
        } else triggered = false;
        stopMagazine(); // default to not move anything
    }

    @Override
    protected void writePeriodicOutputs() {
        MOTOR.set(ControlMode.DUTY_CYCLE, demand.percent);
    }

    enum Demand {
        ON(0.5), OFF(0.0), REV(-0.3), INTAKE(0.2);

        double percent;

        Demand(double percent) {
            this.percent = percent;
        }
    }
}