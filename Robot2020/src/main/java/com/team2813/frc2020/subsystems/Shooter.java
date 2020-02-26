package com.team2813.frc2020.subsystems;

import com.revrobotics.AlternateEncoderType;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.team2813.frc2020.Robot;
import com.team2813.frc2020.util.Lightshow;
import com.team2813.frc2020.util.Limelight;
import com.team2813.lib.actions.*;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Button;
import com.team2813.lib.motors.SparkMaxWrapper;
import com.team2813.lib.motors.TalonFXWrapper;
import com.team2813.lib.motors.interfaces.ControlMode;
import com.team2813.lib.motors.interfaces.LimitDirection;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static com.team2813.frc2020.subsystems.Subsystems.LOOPER;

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
    private static final Button AUTO_BUTTON = SubsystemControlsConfig.getAutoButton();
    private final SparkMaxWrapper HOOD;
    private final TalonFXWrapper FLYWHEEL;
    protected final SparkMaxWrapper KICKER;
    protected final CANEncoder encoder;
    private static final int MIN_ANGLE = 35;
    private static final int MAX_ANGLE = 70;
    private static final double MAX_ENCODER = -1.2;
    protected static Position currentPosition = Position.MIN;
    private Demand desiredDemand = Demand.LOW_RANGE;
    private Demand demand = Demand.OFF;
    private KickerDemand kickerDemand = KickerDemand.OFF;
    private SimpleMotorFeedforward shooterFeedforward = new SimpleMotorFeedforward(0.266, 0.112, 0.0189);

    private Limelight limelight = Limelight.getInstance();

    private double FLYWHEEL_UPDUCTION = 3.0 / 2;

    private Action startAction;

    private boolean controlLock = false;

    Shooter() {
        super(MotorConfigs.sparks.get("hood"));

        HOOD = (SparkMaxWrapper) getMotor();
        FLYWHEEL = (TalonFXWrapper) MotorConfigs.talons.get("T5E1");
        KICKER = MotorConfigs.sparks.get("kicker");
        encoder = HOOD.getAlternateEncoder(AlternateEncoderType.kQuadrature, 8192);

        HOOD.getPIDController().setFeedbackDevice(HOOD.getAlternateEncoder());

        HOOD.setSoftLimit(LimitDirection.REVERSE, MAX_ENCODER);
        HOOD.setSoftLimit(LimitDirection.FORWARD, 0);
        HOOD.enableSoftLimit(CANSparkMax.SoftLimitDirection.kReverse, true);
        HOOD.enableSoftLimit(CANSparkMax.SoftLimitDirection.kForward, true);
    }

    @Override
    public synchronized void setPosition(Position position) {
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

    public void setKicker(KickerDemand demand) {
        kickerDemand = demand;
    }

    public void startSpinningFlywheel(boolean controlLock) {
        if (controlLock == this.controlLock) {
            demand = desiredDemand;
            setKicker(KickerDemand.ON);
        }
    }

    public void startSpinningFlywheel() {
        startSpinningFlywheel(false);
    }

    public void reverseFlywheel(boolean controlLock) {
        if (controlLock == this.controlLock)
            demand = Demand.REV;
    }

    public void reverseFlywheel() {
        reverseFlywheel(false);
    }

    public void stopSpinningFlywheel(boolean controlLock) {
        if (controlLock == this.controlLock) {
            demand = Demand.OFF;
            setKicker(KickerDemand.OFF);
        }
    }

    public void stopSpinningFlywheel() {
        stopSpinningFlywheel(false);
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

    public boolean isFlywheelReady() {
        return FLYWHEEL.getVelocity() * FLYWHEEL_UPDUCTION > demand.velocity;
    }

    //TODO Change this to when Mag empty
    public boolean hasFinishButtonBeenPressed() {
        return SHOOTER_BUTTON.get();
    }

    @Override
    public void outputTelemetry() {
        SmartDashboard.putNumber("Kicker Velocity (RPM)", KICKER.getVelocity());
        SmartDashboard.putNumber("Shooter Velocity (RPM)", FLYWHEEL.getVelocity() * FLYWHEEL_UPDUCTION);
        SmartDashboard.putNumber("Hood Encoder", encoder.getPosition());
        SmartDashboard.putNumber("Hood Demand", periodicIO.demand);
        SmartDashboard.putNumber("Limelight Vertical Angle", limelight.getVertAngle());
        SmartDashboard.putNumber("Shooter Flywheel Demand", desiredDemand.velocity);
    }

    @Override
    public void teleopControls() {
        SHOOTER_BUTTON.whenPressedReleased(() -> {
            controlLock = true;
            startSpinningFlywheel(true);
        }, () -> {
            stopSpinningFlywheel(true);
            controlLock = false;
        });

        if (AUTO_BUTTON.get()) {
            // [-4.9, 21.9]
            double vertAngle = limelight.getVertAngle();
            if (vertAngle >= -4.9) {
                setPosition(calculateLowPosition(limelight.getVertAngle()));
                desiredDemand = Demand.LOW_RANGE;
            } else if (vertAngle >= -9.5 && vertAngle <= -4.9) {
                setPosition(calculateMidPosition(limelight.getVertAngle()));
                desiredDemand = Demand.MID_RANGE;
            } else if (vertAngle >= -11.3 && vertAngle <= -9.5) {
                setPosition(calculateHighPosition(limelight.getVertAngle()));
                desiredDemand = Demand.HIGH_RANGE;
            }
        }

        // operator
        HOOD_INITIATION_BUTTON.whenPressed(() -> setPosition(Position.INITIATION));
        HOOD_TRENCH_BUTTON.whenPressed(() -> setPosition(Position.TRENCH));
        if (HOOD_INITIATION_BUTTON.get() && HOOD_TRENCH_BUTTON.get()) {
            setPosition(Position.MIN);
        }
    }

    /* https://www.desmos.com/calculator/g4b5gohz4a see red graph
       -0.0000008514567632688y^5+0.0000361146y^{4}-0.000432028y^{3}+0.00126728y^{2}+0.00961345y-1.00157
     */

    @Override
    public void onEnabledLoop(double timestamp) {
    }

    @Override
    public synchronized void readPeriodicInputs() {
        super.readPeriodicInputs();

        // Set lights if spooled
        if (isFlywheelReady() && demand != Demand.OFF)
            Robot.lightshow.setLight(Lightshow.Light.READY_TO_SHOOT, false);
        else if (demand != Demand.OFF)
            Robot.lightshow.setLight(Lightshow.Light.READY_TO_SHOOT, true);
        else Robot.lightshow.resetLight(Lightshow.Light.READY_TO_SHOOT);
    }

    @Override
    public synchronized void zeroSensors() {
        super.zeroSensors();
        encoder.setPosition(0);
    }

    @Override
    public void writePeriodicOutputs() {
        super.writePeriodicOutputs();

        if (demand != Demand.OFF) {
            double velocity = demand.velocity / FLYWHEEL_UPDUCTION;
            FLYWHEEL.set(ControlMode.VELOCITY, velocity, shooterFeedforward.calculate(velocity));
        } else
            FLYWHEEL.set(ControlMode.DUTY_CYCLE, 0);

        if (kickerDemand != KickerDemand.OFF)
            KICKER.set(ControlMode.VELOCITY, kickerDemand.velocity);
        else
            KICKER.set(ControlMode.DUTY_CYCLE, 0);
    }

    public enum Position implements Subsystem1d.Position<Shooter.Position> {
        MIN("Min", 0),
        MAX("Max", MAX_ENCODER),
        INITIATION("Initiation", -.4),
        TRENCH("Trench", -.6);


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
        LOW_RANGE(3750), MID_RANGE(5000), HIGH_RANGE(6100), OFF(0.0), REV(-1500);

        double velocity;

        Demand(double velocity) {
            this.velocity = velocity;
        }
    }

    enum KickerDemand {
        ON(5500), OFF(0.0), REV(-1200);

        double velocity;

        KickerDemand(double velocity) {
            this.velocity = velocity;
        }
    }

    private static double revsToDegrees(double revs) {
        return revs * (MAX_ANGLE - MIN_ANGLE) / MAX_ENCODER;
    }

    private static double degreesToRevs(double degrees) {
        return degrees * MAX_ENCODER / (MAX_ANGLE - MIN_ANGLE);
    }

    public double calculateLowPosition(double y) {
        return (-0.0000008514567632688 * Math.pow(y, 5)) + (0.0000361146 * Math.pow(y, 4)) - (0.000432028 * Math.pow(y, 3)) + (0.00126728 * Math.pow(y, 2)) + (0.00961345 * y) - 1.00157;
    }

    public double calculateMidPosition(double y) {
        return (-0.00315274 * Math.pow(y,2)) - (0.0695706 * y) - 1.46903;
    }

    public double calculateHighPosition(double y) {
        return (-1.95046 * Math.pow(y,2)) - (43.0127 * y) - 237.929;
    }
}
