package com.team2813.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.team2813.frc2020.Robot;
import com.team2813.frc2020.util.Lightshow;
import com.team2813.frc2020.util.Limelight;
import com.team2813.frc2020.util.ShuffleboardData;
import com.team2813.frc2020.util.Units2813;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.*;
import com.team2813.lib.ctre.PigeonWrapper;
import com.team2813.lib.drive.ArcadeDrive;
import com.team2813.lib.drive.CurvatureDrive;
import com.team2813.lib.drive.DriveDemand;
import com.team2813.lib.drive.VelocityDriveTalon;
import com.team2813.lib.motors.TalonFXWrapper;
import com.team2813.lib.motors.interfaces.ControlMode;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Transform2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.util.Units;

/**
 * The Drive subsystem is the main subsystem for
 * the drive train, and handles both driver control
 * and autonomous path control.
 *
 * @author Grady Whelan
 * @author Samuel Li
 */
public class Drive extends Subsystem {

    // Physical Constants
    public static final double WHEEL_DIAMETER = Units.inchesToMeters(6.25);
    public static final double WHEEL_CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER;

    // Motor Controllers
    private final TalonFXWrapper LEFT;
    private final TalonFXWrapper RIGHT;
    private double right_demand;
    private double left_demand;
    private boolean isBrakeMode;

    // Controls
    private static final double TELEOP_DEAD_ZONE = 0.01;
    private static final Axis ARCADE_X_AXIS = SubsystemControlsConfig.getDriveX();
    private static final Axis ARCADE_Y_AXIS = SubsystemControlsConfig.getDriveY();
    private static final Axis CURVATURE_STEER = SubsystemControlsConfig.getDriveSteer();
    private static final Axis CURVATURE_FORWARD = SubsystemControlsConfig.getDriveForward();
    private static final Axis CURVATURE_REVERSE = SubsystemControlsConfig.getDriveReverse();
    private static final Button PIVOT_BUTTON = SubsystemControlsConfig.getPivotButton();
    private static final Button AUTO_BUTTON = SubsystemControlsConfig.getAutoButton();
    private static final Button SHOOTER_BUTTON = SubsystemControlsConfig.getShooterButton();
    private ControlInput arcade_x;
    private ControlInput arcade_y;

    // Encoders
    private static final double ENCODER_TICKS_PER_REVOLUTION = 2048;
    private static final double ENCODER_TICKS_PER_INCH = ENCODER_TICKS_PER_REVOLUTION / WHEEL_CIRCUMFERENCE;
    private static final double ENCODER_TICKS_PER_FOOT = ENCODER_TICKS_PER_INCH / 12;

    // Mode
    private static DriveMode driveMode = DriveMode.OPEN_LOOP;
    private static TeleopDriveType teleopDriveType = TeleopDriveType.CURVATURE;

    // Gyro
    private final int pigeonID = 13;
    private PigeonWrapper pigeon = new PigeonWrapper(pigeonID, "Drive");
    public PigeonWrapper getPigeon(){return pigeon;}

    // Autonomous
    private double TRACK_WIDTH = 26;
    public static final double GEAR_RATIO = (62.0 / 8.0) * (28.0 / 20.0);
    public DifferentialDriveKinematics kinematics = new DifferentialDriveKinematics(Units.inchesToMeters(TRACK_WIDTH));
    private Limelight limelight = Limelight.getInstance();

    // Odometry
    private static DifferentialDriveOdometry odometry;
    public Pose2d robotPosition;

    public static DifferentialDriveOdometry getOdometry() {
        return odometry;
    }

    public enum TeleopDriveType {
        ARCADE, CURVATURE
    }

    private static final double MAX_VELOCITY = 4.3677; // max velocity of velocity drive in meters per second
    public double getMaxVelocity(){return MAX_VELOCITY;}

    public VelocityDriveTalon velocityDrive = new VelocityDriveTalon(MAX_VELOCITY);
    public CurvatureDrive curvatureDrive = new CurvatureDrive(TELEOP_DEAD_ZONE);
    ArcadeDrive arcadeDrive = curvatureDrive.getArcadeDrive();
    DriveDemand driveDemand = new DriveDemand(0, 0);
    public DriveDemand getDriveDemand(){return driveDemand;}

    private SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(0.181, 2.34, 0.25); // gains in meters

    Drive() {
        ShuffleboardData.driveModeChooser.addOption("Open Loop", DriveMode.OPEN_LOOP);
        ShuffleboardData.driveModeChooser.addOption("Velocity", DriveMode.VELOCITY);
        ShuffleboardData.teleopDriveTypeChooser.addOption("Arcade", TeleopDriveType.ARCADE);
        ShuffleboardData.teleopDriveTypeChooser.addOption("Curvature", TeleopDriveType.CURVATURE);
        arcade_x = new ArcsinFilter(new DeadzoneFilter(ARCADE_X_AXIS, TELEOP_DEAD_ZONE));
        arcade_y = new ArcsinFilter(new DeadzoneFilter(ARCADE_Y_AXIS, TELEOP_DEAD_ZONE));

        LEFT = (TalonFXWrapper) MotorConfigs.talons.get("driveLeft");
        RIGHT = (TalonFXWrapper) MotorConfigs.talons.get("driveRight");

        DriveDemand.circumference = WHEEL_CIRCUMFERENCE;

        pigeon.setYawToCompass();
        pigeon.setHeading(0);
        odometry = new DifferentialDriveOdometry(new Rotation2d(pigeon.getHeading()));
    }

    private void teleopDrive(TeleopDriveType driveType) {
        limelight.setLights(false);
        if (AUTO_BUTTON.get()) {
            limelight.setLights(true);
            driveDemand = curvatureDrive.getDemand(0, 0, limelight.getSteer(), true);
        } else if (driveType == TeleopDriveType.ARCADE) {
            driveDemand = arcadeDrive.getDemand(arcade_y.get(), arcade_x.get());
            ;
        } else {
            double steer = CURVATURE_STEER.get();
            if (PIVOT_BUTTON.get()) steer *= .8; // cap it so it's not too sensitive
            driveDemand = curvatureDrive.getDemand(CURVATURE_FORWARD.get(), CURVATURE_REVERSE.get(), steer, PIVOT_BUTTON.get());
        }

        if (driveMode == DriveMode.VELOCITY) {
            driveDemand = velocityDrive.getVelocity(driveDemand); // convert from duty cycle to m/s
        }
    }

    public void driveForwardTest() {
        driveDemand = new DriveDemand(MAX_VELOCITY, MAX_VELOCITY);
    }

    public void driveBackwardsTest() {
        driveDemand = new DriveDemand(-MAX_VELOCITY, -MAX_VELOCITY);
    }

    public void driveRightTest() {
        driveDemand = new DriveDemand(MAX_VELOCITY, -MAX_VELOCITY);
    }

    public void driveLeftTest() {
        driveDemand = new DriveDemand(-MAX_VELOCITY, MAX_VELOCITY);
    }

    @Override
    public void teleopControls() {
        AUTO_BUTTON.whenPressedReleased(() -> Robot.lightshow.setLight(Lightshow.Light.AUTO_AIM), () -> Robot.lightshow.resetLight(Lightshow.Light.AUTO_AIM));
        driveMode = ShuffleboardData.driveModeChooser.getSelected();
        if (driveMode == null) driveMode = DriveMode.OPEN_LOOP;
        teleopDrive(teleopDriveType);
    }

    public boolean checkSystem() {
        return false;
    }

    public void outputTelemetry() {
        double leftEncoder = LEFT.getEncoderPosition();
        double rightEncoder = RIGHT.getEncoderPosition();
        double leftVelocity = Units2813.motorRpmToDtVelocity(LEFT.getVelocity()); // rpm to m/s
        double rightVelocity = Units2813.motorRpmToDtVelocity(RIGHT.getVelocity());
        SmartDashboard.putNumber("Left Encoder", leftEncoder);
        SmartDashboard.putNumber("Right Encoder", rightEncoder);
        SmartDashboard.putNumber("Left Velocity", leftVelocity);
        SmartDashboard.putNumber("Right Velocity", rightVelocity);
        SmartDashboard.putString("Control Drive Mode", driveMode.toString());
        SmartDashboard.putNumber("Gyro", pigeon.getHeading());
        SmartDashboard.putString("Odometry", odometry.getPoseMeters().toString());
        SmartDashboard.putNumber("Limelight Angle", limelight.getValues().getTx());

        SmartDashboard.putNumber("Left Demand", driveDemand.getLeft());
        SmartDashboard.putNumber("Right Demand", driveDemand.getRight());
        SmartDashboard.putNumber("Left Temp", LEFT.controller.getTemperature());
//        SmartDashboard.putNumber("Left Slave Temp", LEFT.slaves.get(0).controller.getTemperature());
        SmartDashboard.putNumber("Right Temp", RIGHT.controller.getTemperature());
//        SmartDashboard.putNumber("Right Slave Temp", RIGHT.slaves.get(0).controller.getTemperature());
    }

    @Override
    public void onEnabledStart(double timestamp) {
        // TODO: 01/18/2020 verify true and false
        setBrakeMode(true);
    }

    @Override
    public void onDisabledStart(double timestamp) {
        setBrakeMode(false);
    }

    @Override
    public void onEnabledLoop(double timestamp) {
    }

    @Override
    public void onEnabledStop(double timestamp) {
    }

    @Override
    public void zeroSensors() {
        LEFT.setEncoderPosition(0.0);
        RIGHT.setEncoderPosition(0.0);
        pigeon.setHeading(0);
    }

    @Override
    public synchronized void writePeriodicOutputs() {
//        if (driveMode == DriveMode.VELOCITY && !Robot.isAuto && driveDemand.equals(new DriveDemand(0, 0))) {
//            LEFT.set(ControlMode.DUTY_CYCLE, 0);
//            RIGHT.set(ControlMode.DUTY_CYCLE, 0);
//        }
        if (driveMode == DriveMode.VELOCITY || Robot.isAuto) {
            DriveDemand demand = Units2813.dtDemandToMotorDemand(driveDemand); // local variable for telemetry reasons also converts m/s to rpm
            LEFT.set(ControlMode.VELOCITY, demand.getLeft(), feedforward.calculate(driveDemand.getLeft()) / 12);
            RIGHT.set(ControlMode.VELOCITY, demand.getRight(), feedforward.calculate(driveDemand.getRight()) / 12);
        } else {
            LEFT.set(driveMode.controlMode, driveDemand.getLeft());
            RIGHT.set(driveMode.controlMode, driveDemand.getRight());
        }
    }

    @Override
    protected void readPeriodicInputs() {
        double leftDistance = Units2813.motorRevsToWheelRevs(LEFT.getEncoderPosition()) * WHEEL_CIRCUMFERENCE;
        double rightDistance = Units2813.motorRevsToWheelRevs(RIGHT.getEncoderPosition()) * WHEEL_CIRCUMFERENCE;
        robotPosition = odometry.update(Rotation2d.fromDegrees(pigeon.getHeading()), leftDistance, rightDistance);
//        robotPosition = new Pose2d(new Translation2d(-robotPosition.getTranslation().getX(), robotPosition.getTranslation().getY()), robotPosition.getRotation());
//        robotPosition.transformBy(new Transform2d(0, ));
    }

    public synchronized void setBrakeMode(boolean brake) {
        NeutralMode mode = !brake ? NeutralMode.Brake : NeutralMode.Coast;
        RIGHT.setNeutralMode(mode);
        LEFT.setNeutralMode(mode);
        System.out.println("Setting Brake Mode:" + brake);
    }

    public void initAutonomous(Pose2d initialPose) {
        System.out.println("Autonomous Initial Pose: " + initialPose.toString());
        pigeon.setHeading(initialPose.getRotation().getDegrees());
        odometry.resetPosition(initialPose, initialPose.getRotation());
    }

    public void setDemand(DriveDemand demand) {
        this.driveDemand = demand;
    }

    public enum DriveMode {
        OPEN_LOOP(ControlMode.DUTY_CYCLE),
        MOTION_MAGIC(ControlMode.MOTION_MAGIC),
        VELOCITY(ControlMode.VELOCITY);

        ControlMode controlMode;

        DriveMode(ControlMode controlMode) {
            this.controlMode = controlMode;
        }
    }
}
