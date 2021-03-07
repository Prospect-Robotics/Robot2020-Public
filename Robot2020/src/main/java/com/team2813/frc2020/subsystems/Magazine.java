package com.team2813.frc2020.subsystems;

import com.ctre.phoenix.CANifier;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Button;
import com.team2813.lib.motors.SparkMaxWrapper;
import com.team2813.lib.motors.interfaces.ControlMode;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static com.team2813.frc2020.subsystems.Subsystems.INTAKE;
import static com.team2813.frc2020.subsystems.Subsystems.SHOOTER;

public class Magazine extends Subsystem {

    private final SparkMaxWrapper MOTOR;
    private final CANifier INTAKE_COUNTER;
    private final Joystick OPERATOR_JOYSTICK = SubsystemControlsConfig.getOperatorJoystick();
    private final Button START_STOP_BUTTON = SubsystemControlsConfig.getMagButton();
    private final Button FORWARD_BUTTON = SubsystemControlsConfig.getMagForward();
    private final Button REVERSE_BUTTON = SubsystemControlsConfig.getMagReverse();
    private final Button AUTO_BUTTON = SubsystemControlsConfig.getAutoButton();
    private Demand demand;

    public int ammo = 0;
    public boolean triggered = false;
    public boolean ballDetected = false;

    Magazine() {
        MOTOR = MotorConfigs.sparks.get("magazine");
        demand = Demand.OFF;
        INTAKE_COUNTER = new CANifier(14);
    }

    public void spinMagazineForward() {
        if (SHOOTER.demand == Shooter.Demand.LOW_RANGE)
            demand = Demand.LOW_RANGE;
        else demand = Demand.FAR_RANGE;
        SHOOTER.setKicker(Shooter.KickerDemand.ON);
        INTAKE.setIntake(Intake.Demand.IN);
    }

    public void spinMagazineIntake() {
        demand = Demand.INTAKE;
        SHOOTER.setKicker(Shooter.KickerDemand.REV);
        ammo = 0;
    }

    public void spinMagazineReverse() {
        demand = Demand.REV;
        SHOOTER.setKicker(Shooter.KickerDemand.REV);
        SHOOTER.reverseFlywheel();
        System.out.println("Rev");
    }

    public void stopMagazine() {
        demand = Demand.OFF;
        SHOOTER.setKicker(Shooter.KickerDemand.OFF);
        INTAKE.setIntake(Intake.Demand.OFF);
        SHOOTER.stopSpinningFlywheel();
        System.out.println("Off");
    }

    public boolean isCounterBlocked() {
        return INTAKE_COUNTER.getGeneralInput(CANifier.GeneralPin.SDA);
    }

    @Override
    public void outputTelemetry() {
        SmartDashboard.putBoolean("Ball Detected", ballDetected);
        SmartDashboard.putNumber("Ammo", ammo);
        SmartDashboard.putNumber("Magazine Demand", demand.percent);
    }

    @Override
    public void teleopControls() {
        START_STOP_BUTTON.whenPressedReleased(this::spinMagazineForward, this::stopMagazine);
        FORWARD_BUTTON.whenPressedReleased(this::spinMagazineIntake, this::stopMagazine);
        REVERSE_BUTTON.whenPressedReleased(this::spinMagazineReverse, this::stopMagazine);


        if (SHOOTER.isFullyRevvedUp()) {
            // [-4.9, 21.9]
            // -4.9 LOW_MID_THRESHOLD
            // -9.5 MID_FAR_THRESHOLD
            // -11.3 MAX_THRESHOLD
//            double vertAngle = limelight.getVertAngle();
//            if (vertAngle >= LOW_MID_THRESHOLD) {
////                setPosition(calculateLowPosition(getLimelight().getVertAngle()));
////                desiredDemand = Shooter.Demand.LOW_RANGE;
//                demand = Demand.INITIATION;
//            } else if (vertAngle >= MID_FAR_THRESHOLD && vertAngle <= LOW_MID_THRESHOLD) {
////                setPosition(calculateMidPosition(getLimelight().getVertAngle()));
////                desiredDemand = Shooter.Demand.MID_RANGE;
//                demand = Demand.TRENCH;
//            } else if (vertAngle >= MAX_THRESHOLD && vertAngle <= MID_FAR_THRESHOLD) {
////                setPosition(calculateHighPosition(getLimelight().getVertAngle()));
////                desiredDemand = Shooter.Demand.HIGH_RANGE;
//                demand = Demand.TRENCH;
//            }
        }
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
        ballDetected = isCounterBlocked();
        if (ballDetected) { // counts balls
            if (ammo < 5 && !triggered) { // block only runs once
                ammo++;
            }
            triggered = true;
        } else triggered = false;
    }

    @Override
    protected void writePeriodicOutputs() {
//        if(!isCounterBlocked() || demand != Demand.OFF) {
        MOTOR.set(ControlMode.DUTY_CYCLE, demand.percent);
//        }
//        else if (INTAKE.demand == Intake.Demand.IN){
//            if (ammo > 5) { // if there is more than 5 balls
//                spinMagazineReverse(); // spit it out
//            } else {
//            if (ballDetected) spinMagazineIntake(); // take it in
//            }
//        }
    }

    enum Demand {
        LOW_RANGE(1), FAR_RANGE(.5), OFF(0.0), REV(-0.3), INTAKE(0.2);

        double percent;

        Demand(double percent) {
            this.percent = percent;
        }
    }
}