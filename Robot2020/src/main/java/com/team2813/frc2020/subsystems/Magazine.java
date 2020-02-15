package com.team2813.frc2020.subsystems;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.ParamEnum;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Button;
import com.team2813.lib.controls.Controller;
import com.team2813.lib.motors.SparkMaxWrapper;
import com.team2813.lib.motors.TalonFXWrapper;
import com.team2813.lib.motors.interfaces.ControlMode;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Magazine extends Subsystem {

    private final SparkMaxWrapper MOTOR;
    protected final SparkMaxWrapper KICKER;
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
        KICKER = MotorConfigs.sparks.get("kicker");
        INTAKE_COUNTER = new CANifier(14);
    }

    public void spinMagazineForward() {
        demand = Demand.ON;
    }

    public void spinMagazineIntake() {
        demand = Demand.INTAKE;
    }

    public void spinMagazineReverse() {
        demand = Demand.REV;
    }

    public void stopMagazine() {
        demand = Demand.OFF;
    }

    public boolean isCounterBlocked() {
        return INTAKE_COUNTER.getGeneralInput(CANifier.GeneralPin.SDA);
    }

    @Override
    public void outputTelemetry() {
        SmartDashboard.putBoolean("Ball Detected", isCounterBlocked());
    }

    @Override
    public void teleopControls() { // todo use da methods
        START_STOP_BUTTON.whenPressed(() -> {
            demand = demand == Demand.ON ? Demand.OFF : Demand.ON;
        });
        REVERSE_BUTTON.whenPressed(() -> {
            demand = demand == Demand.REV ? Demand.OFF : Demand.REV;
        });
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
        if (isCounterBlocked() == true) {
            if (ammo < 5 && !triggered) { // block only runs once
                ammo++;
            }
            if (ammo == 5) { // if there is more than 5 balls
                demand = Demand.REV; // spit it out
            } else {
                demand = Demand.ON; // take it in
            }
            triggered = true;
        } else triggered = false;
        demand = Demand.OFF; // default to not move anything
    }

    @Override
    protected void writePeriodicOutputs() {
        MOTOR.set(ControlMode.DUTY_CYCLE, demand.percent);
        if (demand == Demand.ON) {
//            KICKER.set(ControlMode.DUTY_CYCLE, demand.percent);
        } else {
            // KICKER.set(ControlMode.DUTY_CYCLE, Demand.OFF.percent);
        }
    }

    enum Demand {
        ON(0.5), OFF(0.0), REV(-0.3), INTAKE(0.2);

        double percent;

        Demand(double percent) {
            this.percent = percent;
        }
    }
}