package com.team2813.frc2020.subsystems;

import com.team2813.lib.config.MotorConfig;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Button;
import com.team2813.lib.motors.SparkMaxWrapper;
import com.team2813.lib.motors.TalonFXWrapper;
import com.team2813.lib.motors.TalonWrapper;
import com.team2813.lib.motors.interfaces.ControlMode;

/**
 * Class for the shooter on the robot.
 *
 * @author Sid Banerjee
 *
 */

public class Shooter extends Subsystem1d<Shooter.Position>{

    private static final Button HOOD_BUTTON = SubsystemControlsConfig.getHoodButton();
    private static final Button SHOOTER_BUTTON = SubsystemControlsConfig.getShooterButton();
    private final SparkMaxWrapper HOOD;
    private final TalonFXWrapper FLYWHEEL;
    private static final int MIN_ANGLE = 35;
    private static final int MAX_ANGLE = 70;
    private static Position currentPosition = Position.ONE;

    Shooter() {
        super(MotorConfigs.sparks.get("hood"));
        HOOD = MotorConfigs.sparks.get("hood");
        FLYWHEEL = (TalonFXWrapper) MotorConfigs.talons.get("flywheel");
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
        HOOD_BUTTON.whenPressed(() -> setNextPosition(true));
        SHOOTER_BUTTON.whileHeld(() -> FLYWHEEL.set(ControlMode.DUTY_CYCLE, 1));
        SHOOTER_BUTTON.whenReleased(() -> FLYWHEEL.set(ControlMode.DUTY_CYCLE, 0));
    }

    public enum Position implements Subsystem1d.Position<Shooter.Position> {
        ONE(revsToDegrees(35.0)){
            @Override
            public Position getNextClockwise() {
                return TWO;
            }

            @Override
            public Position getNextCounter() {
                return EIGHT;
            }
        }, TWO(revsToDegrees(39.375)){
            @Override
            public Position getNextClockwise() {
                return THREE;
            }

            @Override
            public Position getNextCounter() {
                return ONE;
            }
        }, THREE(revsToDegrees(43.75)){
            @Override
            public Position getNextClockwise() {
                return FOUR;
            }

            @Override
            public Position getNextCounter() {
                return TWO;
            }
        }, FOUR(revsToDegrees(52.5)){
            @Override
            public Position getNextClockwise() {
                return FIVE;
            }

            @Override
            public Position getNextCounter() {
                return THREE;
            }
        }, FIVE(revsToDegrees(56.875)){
            @Override
            public Position getNextClockwise() {
                return SIX;
            }

            @Override
            public Position getNextCounter() {
                return FOUR;
            }
        }, SIX(revsToDegrees(61.25)){
            @Override
            public Position getNextClockwise() {
                return SEVEN;
            }

            @Override
            public Position getNextCounter() {
                return FIVE;
            }

        }, SEVEN(revsToDegrees(65.625)){
            @Override
            public Position getNextClockwise() {
                return SIX;
            }

            @Override
            public Position getNextCounter() {
                return EIGHT;
            }

        }, EIGHT(revsToDegrees(70)){
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

    private static double revsToDegrees(double revs){
        return revs*(MAX_ANGLE-MIN_ANGLE)/48;
    }

    private static double distanceToAngle(double meters){
        //TODO Get equation from Sid S.
        return meters;
    }
}
