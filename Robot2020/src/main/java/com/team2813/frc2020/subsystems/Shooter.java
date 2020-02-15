package com.team2813.frc2020.subsystems;

import com.team2813.lib.actions.*;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Button;
import com.team2813.lib.motors.SparkMaxWrapper;
import com.team2813.lib.motors.TalonFXWrapper;
import com.team2813.lib.motors.interfaces.ControlMode;
import com.team2813.lib.util.LimelightValues;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static com.team2813.frc2020.subsystems.Subsystems.LOOPER;
import static com.team2813.frc2020.subsystems.Subsystems.MAGAZINE;

/**
 * Class for the shooter on the robot.
 *
 * @author Sid Banerjee
 * @author Daniel Tsai
 */

public class Shooter extends Subsystem1d<Shooter.Position> {

    private static final Button HOOD_BUTTON = SubsystemControlsConfig.getHoodButton();
    private static final Button SHOOTER_BUTTON = SubsystemControlsConfig.getShooterButton();
    private final SparkMaxWrapper HOOD;
    private final TalonFXWrapper FLYWHEEL;
    protected final SparkMaxWrapper KICKER;
    private static final int MIN_ANGLE = 35;
    private static final int MAX_ANGLE = 70;
    private static Position currentPosition = Position.ONE;
    private Demand demand = Demand.OFF;
    private KickerDemand kickerDemand = KickerDemand.OFF;

    private Action startAction;

    //TODO May be removed
    private boolean manualMode = true;

    Shooter() {
        super(MotorConfigs.sparks.get("hood"));
        HOOD = MotorConfigs.sparks.get("hood");
        FLYWHEEL = (TalonFXWrapper) MotorConfigs.talons.get("T5E1");
        KICKER = MotorConfigs.sparks.get("kicker");
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

    public void unloadPayload() {
        startAction = new SeriesAction(
                new LockFunctionAction(this::startSpinningFlywheel, this::isFlywheelReady, true)
                //TODO Uncomment this ,new FunctionAction(Subsystems.MAGAZINE::spinMagazineForward);
                , new LockAction(this::hasFinishButtonBeenPressed, true)
                , new FunctionAction(this::stopSpinningFlywheel, true)
        );
        LOOPER.addAction(startAction);
    }

    public void setKicker(KickerDemand demand) {
        kickerDemand = demand;
    }

    public void startSpinningFlywheel() {
        demand = Demand.ON;
        setKicker(KickerDemand.ON);
    }

    public void stopSpinningFlywheel() {
        demand = Demand.OFF;
        setKicker(KickerDemand.OFF);
    }

    public boolean isFlywheelReady() {
        //TODO update threshold
        return FLYWHEEL.getVelocity() > /*Threshold*/ 0.9;
    }

    public boolean hasFinishButtonBeenPressed() {
        return SHOOTER_BUTTON.get();
    }

    @Override
    public void outputTelemetry() {
        SmartDashboard.putNumber("Shooter Velocity (RPM)", FLYWHEEL.getVelocity());
    }

    @Override
    public void teleopControls() {
        if (manualMode) {
            HOOD_BUTTON.whenPressed(() -> setNextPosition(true));
        }
        SHOOTER_BUTTON.whenPressedReleased(this::startSpinningFlywheel, this::stopSpinningFlywheel);
    }

    @Override
    public void onEnabledLoop(double timestamp) {}

    @Override
    public void writePeriodicOutputs() {
        super.writePeriodicOutputs();
        double distance = 2.49 / Math.atan(new LimelightValues().getTy().getDouble(0));
        if (!manualMode) {
            setPosition(degreesToRevs(distanceToAngle(distance)));
        }
        FLYWHEEL.set(ControlMode.DUTY_CYCLE, demand.percent);
        KICKER.set(ControlMode.DUTY_CYCLE, kickerDemand.percent);
    }

    public enum Position implements Subsystem1d.Position<Shooter.Position> {
        ONE(revsToDegrees(35.0)) {
            @Override
            public Position getNextClockwise() {
                return TWO;
            }

            @Override
            public Position getNextCounter() {
                return EIGHT;
            }
        }, TWO(revsToDegrees(39.375)) {
            @Override
            public Position getNextClockwise() {
                return THREE;
            }

            @Override
            public Position getNextCounter() {
                return ONE;
            }
        }, THREE(revsToDegrees(43.75)) {
            @Override
            public Position getNextClockwise() {
                return FOUR;
            }

            @Override
            public Position getNextCounter() {
                return TWO;
            }
        }, FOUR(revsToDegrees(52.5)) {
            @Override
            public Position getNextClockwise() {
                return FIVE;
            }

            @Override
            public Position getNextCounter() {
                return THREE;
            }
        }, FIVE(revsToDegrees(56.875)) {
            @Override
            public Position getNextClockwise() {
                return SIX;
            }

            @Override
            public Position getNextCounter() {
                return FOUR;
            }
        }, SIX(revsToDegrees(61.25)) {
            @Override
            public Position getNextClockwise() {
                return SEVEN;
            }

            @Override
            public Position getNextCounter() {
                return FIVE;
            }

        }, SEVEN(revsToDegrees(65.625)) {
            @Override
            public Position getNextClockwise() {
                return SIX;
            }

            @Override
            public Position getNextCounter() {
                return EIGHT;
            }

        }, EIGHT(revsToDegrees(70)) {
            @Override
            public Position getNextClockwise() {
                return ONE;
            }

            @Override
            public Position getNextCounter() {
                return SEVEN;
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

    enum Demand {
        ON(.7), OFF(0.0);

        double percent;

        Demand(double percent) {
            this.percent = percent;
        }
    }

    enum KickerDemand {
        ON(0.8), OFF(0.0), REV(-0.2);

        double percent;

        KickerDemand(double percent) {
            this.percent = percent;
        }
    }

    private static double revsToDegrees(double revs) {
        return revs * (MAX_ANGLE - MIN_ANGLE) / 48;
    }

    private static double degreesToRevs(double degrees) {
        return degrees * 48 / (MAX_ANGLE - MIN_ANGLE);
    }

    private static double distanceToAngle(double meters) {
        //TODO Get equation from Sid S.
        return meters;
    }
}
