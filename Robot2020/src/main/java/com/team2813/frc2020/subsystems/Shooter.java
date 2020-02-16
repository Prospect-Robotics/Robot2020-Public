package com.team2813.frc2020.subsystems;

import com.revrobotics.AlternateEncoderType;
import com.revrobotics.CANAnalog;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.team2813.lib.actions.*;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Button;
import com.team2813.lib.motors.SparkMaxWrapper;
import com.team2813.lib.motors.TalonFXWrapper;
import com.team2813.lib.motors.interfaces.ControlMode;
import com.team2813.lib.motors.interfaces.LimitDirection;
import com.team2813.lib.util.LimelightValues;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
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
    private static final Button HOOD_INITIATION_BUTTON = SubsystemControlsConfig.getHoodInitiation();
    private static final Button HOOD_TRENCH_BUTTON = SubsystemControlsConfig.getHoodTrench();
    private final SparkMaxWrapper HOOD;
    private final TalonFXWrapper FLYWHEEL;
    protected final SparkMaxWrapper KICKER;
    protected final CANEncoder encoder;
    private static final int MIN_ANGLE = 35;
    private static final int MAX_ANGLE = 70;
    private static final double MAX_ENCODER = 41;
    private static Position currentPosition = Position.MIN;
    private Demand demand = Demand.OFF;
    private KickerDemand kickerDemand = KickerDemand.OFF;
    private SimpleMotorFeedforward shooterFeedforward = new SimpleMotorFeedforward(0.266,0.112, 0.0189);

    private Action startAction;

    //TODO May be removed
    private boolean manualMode = true;

    Shooter() {
        super(MotorConfigs.sparks.get("hood"));
        HOOD = (SparkMaxWrapper) getMotor();
        FLYWHEEL = (TalonFXWrapper) MotorConfigs.talons.get("T5E1");
        KICKER = MotorConfigs.sparks.get("kicker");
        encoder = HOOD.getAlternateEncoder(AlternateEncoderType.kQuadrature, 8192);
        HOOD.getPIDController().setFeedbackDevice(HOOD.getEncoder());
        HOOD.setSoftLimit(LimitDirection.REVERSE, 0);
        HOOD.setSoftLimit(LimitDirection.FORWARD, MAX_ENCODER);
        HOOD.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
        HOOD.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);
    }

    @Override
    synchronized void setPosition(Position position) {
        currentPosition = position;
        super.setPosition(position);
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
        SmartDashboard.putNumber("Hood Encoder", encoder.getPosition());
        SmartDashboard.putNumber("Hood NEO Encoder", HOOD.getEncoderPosition());
        SmartDashboard.putString("Hood Demand", currentPosition.getName());
    }

    @Override
    public void teleopControls() {
        if (manualMode) {
            HOOD_BUTTON.whenPressed(() -> {

                setNextPosition(true);

            });
        }
        SHOOTER_BUTTON.whenPressedReleased(this::startSpinningFlywheel, this::stopSpinningFlywheel);

        // operator
        HOOD_INITIATION_BUTTON.whenPressed(() -> setPosition(Position.INITIATION));
        HOOD_TRENCH_BUTTON.whenPressed(() -> setPosition(Position.TRENCH));
        if(HOOD_INITIATION_BUTTON.get() && HOOD_TRENCH_BUTTON.get()) {
            setPosition(Position.MIN);
        }
    }

    @Override
    public void onEnabledLoop(double timestamp) {}

    @Override
    public void writePeriodicOutputs() {
        super.writePeriodicOutputs();
        double distance = 2.49 / Math.atan(new LimelightValues().getTy().getDouble(0));
        if (!manualMode) {
//            setPosition(degreesToRevs(distanceToAngle(distance)));
        }
        if(demand == Demand.ON) {
            //TODO Temporarily/Permenantly removed feedforward
            FLYWHEEL.set(ControlMode.VELOCITY, demand.velocity, shooterFeedforward.calculate(demand.velocity));
        }else{
            FLYWHEEL.set(ControlMode.DUTY_CYCLE, 0);
        }
        KICKER.set(ControlMode.DUTY_CYCLE, kickerDemand.percent);
    }

    public enum Position implements Subsystem1d.Position<Shooter.Position> {
//        ONE(revsToDegrees(35.0)) {
//            @Override
//            public Position getNextClockwise() {
//                return TWO;
//            }
//
//            @Override
//            public Position getNextCounter() {
//                return EIGHT;
//            }
//        }, TWO(revsToDegrees(39.375)) {
//            @Override
//            public Position getNextClockwise() {
//                return THREE;
//            }
//
//            @Override
//            public Position getNextCounter() {
//                return ONE;
//            }
//        }, THREE(revsToDegrees(43.75)) {
//            @Override
//            public Position getNextClockwise() {
//                return FOUR;
//            }
//
//            @Override
//            public Position getNextCounter() {
//                return TWO;
//            }
//        }, FOUR(revsToDegrees(52.5)) {
//            @Override
//            public Position getNextClockwise() {
//                return FIVE;
//            }
//
//            @Override
//            public Position getNextCounter() {
//                return THREE;
//            }
//        }, FIVE(revsToDegrees(56.875)) {
//            @Override
//            public Position getNextClockwise() {
//                return SIX;
//            }
//
//            @Override
//            public Position getNextCounter() {
//                return FOUR;
//            }
//        }, SIX(revsToDegrees(61.25)) {
//            @Override
//            public Position getNextClockwise() {
//                return SEVEN;
//            }
//
//            @Override
//            public Position getNextCounter() {
//                return FIVE;
//            }
//
//        }, SEVEN(revsToDegrees(65.625)) {
//            @Override
//            public Position getNextClockwise() {
//                return SIX;
//            }
//
//            @Override
//            public Position getNextCounter() {
//                return EIGHT;
//            }
//
//        }, EIGHT(revsToDegrees(70)) {
//            @Override
//            public Position getNextClockwise() {
//                return ONE;
//            }
//
//            @Override
//            public Position getNextCounter() {
//                return SEVEN;
//            }
//        };
//        ONE(revsToDegrees(35.0)) {
//            @Override
//            public Position getNextClockwise() {
//                return TWO;
//            }
//
//            @Override
//            public Position getNextCounter() {
//                return TWO;
//            }
//        }, TWO(revsToDegrees(39.375)) {
//            @Override
//            public Position getNextClockwise() {
//                return ONE;
//            }
//
//            @Override
//            public Position getNextCounter() {
//                return ONE;
//            }
//        },;
        MIN("Min", 0),
        MAX("Max", MAX_ENCODER),
        INITIATION("Initiation", 37.1),
        TRENCH("Trench", 10);


        private String name;
        private double position;

        Position(String name, double position) {
            this.name = name;
            this.position = position;
        }

        public String getName() {
            return name;
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
            return MIN;
        }

        //TODO MAX Changed to TWO, should the returned to MAX
        @Override
        public Position getMax() {
            return MAX;
        }

        @Override
        public Position getClock(boolean clockwise) {
            return clockwise ? getNextClockwise() : getNextCounter();
        }
    }

    enum Demand {
        ON(2500), OFF(0.0);

        double velocity;

        Demand(double velocity) {
            this.velocity = velocity;
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
        return revs * (MAX_ANGLE - MIN_ANGLE) / MAX_ENCODER;
    }

    private static double degreesToRevs(double degrees) {
        return degrees * MAX_ENCODER / (MAX_ANGLE - MIN_ANGLE);
    }

    private static double distanceToAngle(double meters) {
        //TODO Get equation from Sid S.
        return meters;
    }
}
