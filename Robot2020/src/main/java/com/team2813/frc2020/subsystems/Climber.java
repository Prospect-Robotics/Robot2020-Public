package com.team2813.frc2020.subsystems;

import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Button;
import com.team2813.lib.motors.SparkMaxWrapper;

/**
 * Class for the climber
 *
 * @author Daniel Tsai
 *
 */

public class Climber extends Subsystem1d<Climber.Position>{

    private static final Button CLIMBER_BUTTON = SubsystemControlsConfig.getClimberButton();
    public final SparkMaxWrapper CLIMBER;
    private static Position currentPosition = Position.RETRACTED;

    Climber() {
        super((SparkMaxWrapper) MotorConfigs.sparks.get("climb"));
        CLIMBER = (SparkMaxWrapper) MotorConfigs.sparks.get("climb");
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

    @Override
    public void outputTelemetry() {

    }

    @Override
    public void teleopControls() {
        CLIMBER_BUTTON.whenPressed(() -> setNextPosition(true));
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
