package com.team2813.frc2020.subsystems;

import com.team2813.lib.actions.*;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Axis;
import com.team2813.lib.controls.Button;
import com.team2813.lib.motors.SparkMaxWrapper;
import com.team2813.lib.solenoid.PistonSolenoid;
import edu.wpi.first.wpilibj.Timer;

import static com.team2813.frc2020.subsystems.Subsystems.LOOPER;


/**
 * Class for the climber
 *
 * @author Daniel Tsai
 */

public class Climber extends Subsystem1d<Climber.Position> {

    private static final Axis CLIMBER_AXIS = SubsystemControlsConfig.getClimberElevator();
    private static final Button CLIMBER_BUTTON = SubsystemControlsConfig.getClimberButton();
    private static final Button PISTON_BUTTON = SubsystemControlsConfig.getClimberPiston();
    private final SparkMaxWrapper CLIMBER;
    private final PistonSolenoid BRAKE;
    private static Position currentPosition = Position.RETRACTED;
    private boolean isClimbing;

    Climber() {
        super(MotorConfigs.sparks.get("climber"));
        CLIMBER = MotorConfigs.sparks.get("climber");
        BRAKE = new PistonSolenoid(0);
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
        BRAKE.extend();
    }

    public void disengageBrake() { BRAKE.retract(); }

    public void retractClimb() {
        disengageBrake();
        setPosition(Position.RETRACTED);
    }

    public boolean positionReached() {
        return CLIMBER.getEncoderPosition() <= currentPosition.getPos();
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
    }

    @Override
    public void teleopControls() {
        PISTON_BUTTON.whenPressed(BRAKE::toggle);
        if (Timer.getMatchTime() < 30 && !isClimbing) {
            if (Timer.getMatchTime() > 29.9) disengageBrake();
            CLIMBER_BUTTON.whenPressed(this::autoRetractClimb);
            setPosition(CLIMBER.getEncoderPosition() + CLIMBER_AXIS.get());
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
        if (BRAKE.get() == PistonSolenoid.PistonState.EXTENDED) super.writePeriodicOutputs();
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
