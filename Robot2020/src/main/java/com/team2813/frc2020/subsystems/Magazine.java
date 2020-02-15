package com.team2813.frc2020.subsystems;

import com.ctre.phoenix.CANifier;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Button;
import com.team2813.lib.motors.SparkMaxWrapper;
import com.team2813.lib.motors.interfaces.ControlMode;
import edu.wpi.first.wpilibj.Joystick;

import static com.team2813.frc2020.subsystems.Subsystems.SHOOTER;

public class Magazine extends Subsystem {

    private final SparkMaxWrapper MOTOR;
    private final CANifier INTAKE_COUNTER;
    private final Joystick OPERATOR_JOYSTICK = SubsystemControlsConfig.getOperatorJoystick();
    private final Button START_STOP_BUTTON = SubsystemControlsConfig.getMagButton();
    private final Button REVERSE_BUTTON = SubsystemControlsConfig.getMagReverse();
    private Demand demand;

    private boolean isCounterOn = false;

    Magazine() {
        MOTOR = MotorConfigs.sparks.get("magazine");
        demand = Demand.OFF;
        INTAKE_COUNTER = new CANifier(14);
    }

    public void spinMagazineForward(){
        demand = Demand.ON;
        SHOOTER.setKicker(Shooter.KickerDemand.ON);
    }

    public void spinMagazineIntake(){
        demand = Demand.INTAKE;
        SHOOTER.setKicker(Shooter.KickerDemand.REV);
    }

    public void spinMagazineReverse(){
        demand = Demand.REV;
        SHOOTER.setKicker(Shooter.KickerDemand.REV);
    }

    public void stopMagazine() {
        demand = Demand.OFF;
        SHOOTER.setKicker(Shooter.KickerDemand.OFF);
    }

    @Override
    public void outputTelemetry() {
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
        isCounterOn = INTAKE_COUNTER.getGeneralInput(CANifier.GeneralPin.SDA);
    }

    @Override
    protected void writePeriodicOutputs() {
        MOTOR.set(ControlMode.DUTY_CYCLE, demand.percent);
        if(demand == Demand.ON) {
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