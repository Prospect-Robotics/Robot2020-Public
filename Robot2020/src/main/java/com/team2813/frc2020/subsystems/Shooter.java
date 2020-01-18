package com.team2813.frc2020.subsystems;

import com.team2813.lib.config.MotorConfig;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Button;
import com.team2813.lib.motors.SparkMaxWrapper;
import com.team2813.lib.motors.TalonWrapper;

/**
 * Class for the shooter on the robot.
 *
 * @author Sid Banerjee
 *
 */

public class Shooter extends Subsystem1d<Shooter.Position>{

    private static final SparkMaxWrapper MOTOR = MotorConfigs.sparks.get("shooter");
    private static final Button SHOOTER_BUTTON = SubsystemControlsConfig.getShooterButton();
    private static Position currentPosition = Position.ONE;

    Shooter() {
        super(MOTOR);
    }


    @Override
    void setNextPosition(boolean clockwise) {
        currentPosition = currentPosition.getClock(clockwise);
        setPosition(currentPosition);
    }

    @Override
    void setNextPosition(Position position) {

    }

    @Override
    public void outputTelemetry() {

    }

    @Override
    public void teleopControls() {
        SHOOTER_BUTTON.whenPressed(() -> setNextPosition(true));
    }

    public enum Position implements Subsystem1d.Position<Shooter.Position> {
        ONE(0.0){
            @Override
            public Position getNextClockwise() {
                return TWO;
            }

            @Override
            public Position getNextCounter() {
                return EIGHT;
            }
        }, TWO(6.857){
            @Override
            public Position getNextClockwise() {
                return THREE;
            }

            @Override
            public Position getNextCounter() {
                return ONE;
            }
        }, THREE(13.714){
            @Override
            public Position getNextClockwise() {
                return FOUR;
            }

            @Override
            public Position getNextCounter() {
                return TWO;
            }
        }, FOUR(20.571){
            @Override
            public Position getNextClockwise() {
                return FIVE;
            }

            @Override
            public Position getNextCounter() {
                return THREE;
            }
        }, FIVE(27.428){
            @Override
            public Position getNextClockwise() {
                return SIX;
            }

            @Override
            public Position getNextCounter() {
                return FOUR;
            }
        }, SIX(34.285){
            @Override
            public Position getNextClockwise() {
                return SEVEN;
            }

            @Override
            public Position getNextCounter() {
                return FIVE;
            }

        }, SEVEN(41.142){
            @Override
            public Position getNextClockwise() {
                return SIX;
            }

            @Override
            public Position getNextCounter() {
                return EIGHT;
            }

        }, EIGHT(48.0){
            @Override
            public Position getNextClockwise() {
                return ONE;
            }

            @Override
            public Position getNextCounter() {
                return SEVEN;
            }};


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
            return ONE;
        }

        @Override
        public Position getMax() {
            return EIGHT;
        }

        @Override
        public Position getClock(boolean clockwise) {
            return clockwise ? getNextClockwise() : getNextCounter();
        }
    }
}
