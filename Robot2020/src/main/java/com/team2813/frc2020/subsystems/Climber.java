package com.team2813.frc2020.subsystems;

import com.team2813.lib.actions.Action;
import com.team2813.lib.actions.FunctionAction;
import com.team2813.lib.actions.LockFunctionAction;
import com.team2813.lib.actions.SeriesAction;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Button;
import com.team2813.lib.motors.SparkMaxWrapper;
import com.team2813.lib.motors.interfaces.ControlMode;
import com.team2813.lib.solenoid.PistonSolenoid;

import static com.team2813.frc2020.subsystems.Subsystems.LOOPER;


/**
 * Class for the climber
 *
 * @author Daniel Tsai
 *
 */

public class Climber extends Subsystem1d<Climber.Position>{

    private static final Button CLIMBER_BUTTON = SubsystemControlsConfig.getClimberButton();
    private final SparkMaxWrapper CLIMBER;
    private final PistonSolenoid BRAKE;
    private static Position currentPosition = Position.RETRACTED;

    private Action startAction;
    private Action abortAction;
    private Action retractAction;

    Climber() {
        super((SparkMaxWrapper) MotorConfigs.sparks.get("climb"));
        CLIMBER = (SparkMaxWrapper) MotorConfigs.sparks.get("climb");
        //TODO Insert PCM port ID
        BRAKE = new PistonSolenoid(5);
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

    public void engageBrake(){
        BRAKE.extend();
    }

    public void extendClimb(){
        setPosition(Position.EXTENDED);
    }

    public void retractClimb(){
        setPosition(Position.RETRACTED);
    }

    public boolean positionReached(){
        return CLIMBER.getEncoderPosition() == currentPosition.getPos();
    }

    public void startClimb(){
        startAction = new SeriesAction(
                new LockFunctionAction(this::retractClimb, this::positionReached, true),
                new FunctionAction(this::engageBrake, true)
        );
        LOOPER.addAction(startAction);
    }

    @Override
    public void outputTelemetry() { }

    @Override
    public void teleopControls() {
        CLIMBER_BUTTON.whenPressed(() -> setNextPosition(true));
    }

    @Override
    public void onEnabledStart(double timestamp) { }

    @Override
    public void onEnabledLoop(double timestamp) { }

    @Override
    public void onEnabledStop(double timestamp) { }

    public enum Position implements Subsystem1d.Position<Climber.Position> {
        RETRACTED(0){
            @Override
            public Position getNextClockwise() {
                return EXTENDED;
            }

            @Override
            public Position getNextCounter() {
                return EXTENDED;
            }
        },EXTENDED(400){
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

        Position(double position){
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
