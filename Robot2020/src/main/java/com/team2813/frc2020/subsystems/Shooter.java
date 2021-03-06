package com.team2813.frc2020.subsystems;

import com.revrobotics.AlternateEncoderType;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.team2813.frc2020.Robot;
import com.team2813.frc2020.util.Lightshow;
import com.team2813.frc2020.util.Limelight;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Button;
import com.team2813.lib.motors.SparkMaxWrapper;
import com.team2813.lib.motors.TalonFXWrapper;
import com.team2813.lib.motors.interfaces.ControlMode;
import com.team2813.lib.motors.interfaces.LimitDirection;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Class for the shooter on the robot.
 *
 * @author Sid Banerjee
 * @author Daniel Tsai
 */

public class Shooter extends Subsystem1d<Shooter.Position> {

    private static final Button SHOOTER_BUTTON = SubsystemControlsConfig.getShooterButton();
    private static final Button AUTO_BUTTON = SubsystemControlsConfig.getAutoButton();
    private static final Button HOOD_ZERO_BUTTON = SubsystemControlsConfig.getHoodZeroButton();
    private static final Button HOOD_KILL_BUTTON = SubsystemControlsConfig.getHoodKillButton();
    private static final Button HOOD_CLOSE_BUTTON = SubsystemControlsConfig.getHoodCloseButton();
    private final SparkMaxWrapper HOOD;
    private final TalonFXWrapper FLYWHEEL;
    protected final SparkMaxWrapper KICKER;
    protected final CANEncoder encoder;
    private static final double MAX_ENCODER = -1.2;
    protected static Position currentPosition = Position.MIN;
    private Demand desiredDemand = Demand.LOW_RANGE;
    public Demand demand = Demand.OFF;
    private KickerDemand kickerDemand = KickerDemand.OFF;
    private SimpleMotorFeedforward shooterFeedforward = new SimpleMotorFeedforward(0.266, 0.112, 0.0189);
    private Limelight limelight = Limelight.getInstance();
    static final double LOW_MID_THRESHOLD = -4.5;
    static final double MID_FAR_THRESHOLD = -10.3;
    static final double MAX_THRESHOLD = -11.3;

    private double FLYWHEEL_UPDUCTION = 3.0 / 2;
    private boolean isFullyRevvedUp;

    private boolean controlLock = false;
    private boolean murderedHood = false;

    Shooter() {
        super(MotorConfigs.sparks.get("hood"));

        HOOD = (SparkMaxWrapper) getMotor();
        FLYWHEEL = (TalonFXWrapper) MotorConfigs.talons.get("T5E1");
        KICKER = MotorConfigs.sparks.get("kicker");
        encoder = HOOD.getAlternateEncoder(AlternateEncoderType.kQuadrature, 8192);

        HOOD.getPIDController().setFeedbackDevice(encoder);

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
        System.out.println("help me");
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

    public boolean isFlywheelReady() {
        return Math.abs((FLYWHEEL.getVelocity() * FLYWHEEL_UPDUCTION) - demand.expected) < 500;
    }

    boolean isFullyRevvedUp() {
        return isFullyRevvedUp;
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
            Limelight.getInstance().setLights(true);
            adjustHood();
        }

        isFullyRevvedUp = FLYWHEEL.getVelocity() >= desiredDemand.velocity;

        // operator
        HOOD_CLOSE_BUTTON.whenPressed(() -> setPosition(Position.CLOSE));
        HOOD_ZERO_BUTTON.whenPressed(() -> setPosition(Position.MIN));
        HOOD_KILL_BUTTON.whenPressed(() -> murderedHood = !murderedHood);
    }

    public void adjustHood() {
        // [-4.9, 21.9]
        // -4.9 LOW_MID_THRESHOLD
        // -9.5 MID_FAR_THRESHOLD
        // -11.3 MAX_THRESHOLD
        double vertAngle = limelight.getVertAngle();
        if (vertAngle >= LOW_MID_THRESHOLD) {
            setPosition(calculateLowPosition(limelight.getVertAngle()));
            desiredDemand = Demand.LOW_RANGE;
        } else if (vertAngle >= MID_FAR_THRESHOLD && vertAngle <= LOW_MID_THRESHOLD) {
            setPosition(calculateMidPosition(limelight.getVertAngle()));
            desiredDemand = Demand.MID_RANGE;
        } else if (vertAngle >= MAX_THRESHOLD && vertAngle <= MID_FAR_THRESHOLD) {
            setPosition(calculateHighPosition(limelight.getVertAngle()));
            desiredDemand = Demand.HIGH_RANGE;
        }
        if (desiredDemand != demand && demand != Demand.OFF)
            demand = desiredDemand;
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
            Robot.lightshow.setLight(Lightshow.Light.NOT_READY_TO_SHOOT);
        else Robot.lightshow.resetLight(Lightshow.Light.READY_TO_SHOOT, Lightshow.Light.NOT_READY_TO_SHOOT);
    }

    @Override
    public synchronized void zeroSensors() {
        super.zeroSensors();
        encoder.setPosition(0);
    }

    public double getVelocity() {
        return FLYWHEEL.getVelocity() * FLYWHEEL_UPDUCTION;
    }

    @Override
    public void writePeriodicOutputs() {
        
        if (demand != Demand.OFF && (Math.abs(getVelocity()) < Math.abs(demand.expected)) || (Math.abs(getVelocity() - demand.expected) < 300)) {
            double velocity = demand.velocity / FLYWHEEL_UPDUCTION;
            FLYWHEEL.set(ControlMode.VELOCITY, velocity, shooterFeedforward.calculate(velocity));
        } else {
            FLYWHEEL.set(ControlMode.DUTY_CYCLE, 0);
        }

        if (kickerDemand != KickerDemand.OFF)
            KICKER.set(ControlMode.VELOCITY, kickerDemand.velocity);
        else
            KICKER.set(ControlMode.DUTY_CYCLE, 0);

        if (murderedHood) HOOD.set(ControlMode.DUTY_CYCLE, 0);
        else super.writePeriodicOutputs();
    }

    public enum Position implements Subsystem1d.Position<Shooter.Position> {
        MIN("Min", 0),
        CLOSE("Close", -.578),
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
        LOW_RANGE(3750, 4530), MID_RANGE(5200, 5770), HIGH_RANGE(6100, 6100), OFF(0.0, 0), REV(-1500, -1500);

        double velocity;
        double expected;

        Demand(double velocity, double expected) {
            this.velocity = velocity;
            this.expected = expected;
        }
    }

    enum KickerDemand {
        ON(5500), OFF(0.0), REV(-1200);

        double velocity;

        KickerDemand(double velocity) {
            this.velocity = velocity;
        }
    }

    public double calculateLowPosition(double y) {
        return (-0.00000040621212258987 * Math.pow(y, 5))
                + (0.0000189531 * Math.pow(y, 4))
                - (0.000267987 * Math.pow(y, 3))
                + (0.00139343 * Math.pow(y, 2))
                + (0.0106419 * y)
                - 1;
    }

    public double calculateMidPosition(double y) {
        return (0.0036761 * Math.pow(y, 3))
                + (0.0886286 * Math.pow(y, 2))
                + (0.646713 * y)
                + 0.239859;
    }

    public double calculateHighPosition(double y) {
        return (-0.00315274 * Math.pow(y, 2))
                - (0.0695706 * y)
                - 1.46903;
    }
}
