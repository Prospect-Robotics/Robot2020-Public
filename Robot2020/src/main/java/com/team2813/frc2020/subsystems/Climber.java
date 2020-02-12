package com.team2813.frc2020.subsystems;

import com.revrobotics.CANSparkMax;
import com.team2813.lib.actions.*;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Axis;
import com.team2813.lib.controls.Button;
import com.team2813.lib.motors.SparkMaxWrapper;
import com.team2813.lib.motors.interfaces.ControlMode;
import com.team2813.lib.motors.interfaces.LimitDirection;
import com.team2813.lib.solenoid.PistonSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static com.team2813.frc2020.subsystems.Subsystems.LOOPER;


/**
 * Subsystem for the climber
 *
 * @author Daniel Tsai
 */

public class Climber extends Subsystem1d<Climber.Position> {

    private static final Axis CLIMBER_AXIS = SubsystemControlsConfig.getClimberElevator();
    private static final Button CLIMBER_BUTTON = SubsystemControlsConfig.getClimberButton();
    private static final Button PISTON_BUTTON = SubsystemControlsConfig.getClimberPiston();
//    private final SparkMaxWrapper CLIMBER;
    private final PistonSolenoid BRAKE;
    private static Position currentPosition = Position.RETRACTED;
    private boolean isClimbing = false;
    private boolean isVelocity = false;
    private final int RAISE_VELOCITY = 3500;//rpm
    private double velocityFactor = 0;

    Climber() {
        super(MotorConfigs.sparks.get("climber"));
//        CLIMBER = MotorConfigs.sparks.get("climber");
        BRAKE = new PistonSolenoid(0);
        getMotor().setSoftLimit(LimitDirection.REVERSE, -59.0);
        getMotor().setSoftLimit(LimitDirection.FORWARD, 0.0);
        ((SparkMaxWrapper) getMotor()).enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
        ((SparkMaxWrapper) getMotor()).enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);
    }

    @Override
    void setNextPosition(boolean clockwise) {
        currentPosition = currentPosition.getClock(clockwise);
        setPosition(currentPosition);
    }

    @Override
    void setNextPosition(Position position) {
        currentPosition = position;
        setPosition(currentPosition);
    }

    public void engageBrake() {
        BRAKE.retract();
    }

    public void disengageBrake() { BRAKE.extend(); }

    public void retractClimb() {
        disengageBrake();
        setPosition(Position.RETRACTED);
    }

    public boolean positionReached() {
        return getMotor().getEncoderPosition() >= currentPosition.getPos();
    }

    public void autoRetractClimb() {
        isClimbing = true;
        Action startAction = new SeriesAction(
                new LockFunctionAction(this::retractClimb, this::positionReached, true),
                new FunctionAction(this::engageBrake, true),
                new FunctionAction(() -> isClimbing = false, true)
        );
        LOOPER.addAction(startAction);
    }

    @Override
    public void outputTelemetry() {
        SmartDashboard.putNumber("Climber Encoder", getMotor().getEncoderPosition());
        SmartDashboard.putNumber("Climber Demand", periodicIO.demand);
    }

    @Override
    public void teleopControls() {
        PISTON_BUTTON.whenPressed(BRAKE::toggle);
        if (/*Timer.getMatchTime() < 30 && */!isClimbing) {
//            if (Timer.getMatchTime() > 29.9) disengageBrake();
            CLIMBER_BUTTON.whenPressed(this::autoRetractClimb);
            if (Math.abs(CLIMBER_AXIS.get()) >= 0.2) {
                isVelocity = true;
                disengageBrake();
            } else {
                isVelocity = false;
            }
            velocityFactor = CLIMBER_AXIS.get();
            

//            CLIMBER.getEncoderPosition() + CLIMBER_AXIS.get() - 10);
        }
    }

    @Override
    public void onEnabledStart(double timestamp) {
        BRAKE.set(PistonSolenoid.PistonState.RETRACTED);
    }

    @Override
    public void onEnabledLoop(double timestamp) {
    }

    @Override
    public void onEnabledStop(double timestamp) {
    }

    @Override
    public void writePeriodicOutputs() {
        if (BRAKE.get() == PistonSolenoid.PistonState.EXTENDED && (isVelocity)) {
            getMotor().set(ControlMode.VELOCITY, RAISE_VELOCITY*velocityFactor);
        } else {
            getMotor().set(ControlMode.VELOCITY, 0.0);
        }
        if (BRAKE.get() == PistonSolenoid.PistonState.EXTENDED && isClimbing) super.writePeriodicOutputs();
    }

    public enum Position implements Subsystem1d.Position<Climber.Position> {
        RETRACTED(0) {
            @Override
            public Position getNextClockwise() {
                return EXTENDED;
            }

            @Override
            public Position getNextCounter() {
                return EXTENDED;
            }
        }, EXTENDED(400) {
            @Override
            public Position getNextClockwise() {
                return RETRACTED;
            }

            @Override
            public Position getNextCounter() {
                return RETRACTED;
            }
        };

        private double position;

        Position(double position) {
            this.position = position;
        }

        @Override
        public double getPos() {
            return position;
        }

        @Override
        public Position getNextClockwise() {
            return this;
        }

        @Override
        public Position getNextCounter() {
            return this;
        }

        @Override
        public Position getMin() {
            return RETRACTED;
        }

        @Override
        public Position getMax() {
            return EXTENDED;
        }

        @Override
        public Position getClock(boolean clockwise) {
            return clockwise ? getNextClockwise() : getNextCounter();
        }
    }
}
