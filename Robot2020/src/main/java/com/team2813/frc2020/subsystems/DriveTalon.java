package com.team2813.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.team2813.frc2020.Robot;
import com.team2813.frc2020.util.ShuffleboardData;
import com.team2813.lib.auto.RamseteTrajectory;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.*;
import com.team2813.lib.ctre.PigeonWrapper;
import com.team2813.lib.drive.ArcadeDrive;
import com.team2813.lib.drive.CurvatureDrive;
import com.team2813.lib.drive.DriveDemand;
import com.team2813.lib.drive.VelocityDriveTalon;
import com.team2813.lib.motors.TalonFXWrapper;
import com.team2813.lib.motors.interfaces.ControlMode;
import com.team2813.lib.util.LimelightValues;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
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
public class DriveTalon extends Subsystem {

    // Physical Constants
    private static final double WHEEL_DIAMETER_INCHES = 6.25;
    private static final double WHEEL_CIRCUMFERENCE_INCHES = Math.PI * WHEEL_DIAMETER_INCHES;

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
    private ControlInput arcade_x;
    private ControlInput arcade_y;

    // Encoders
    private static final double ENCODER_TICKS_PER_REVOLUTION = 2048;
    private static final double ENCODER_TICKS_PER_INCH = ENCODER_TICKS_PER_REVOLUTION / WHEEL_CIRCUMFERENCE_INCHES;
    private static final double ENCODER_TICKS_PER_FOOT = ENCODER_TICKS_PER_INCH / 12;

    // Mode
    private static DriveMode driveMode = DriveMode.OPEN_LOOP;
    private static TeleopDriveType teleopDriveType = TeleopDriveType.CURVATURE;

    // Gyro
    private final int pigeonID = 13;
    private PigeonWrapper pigeon = new PigeonWrapper(pigeonID, "Drive");

    // Autonomous
    private double TRACK_WIDTH = 26;
    private double gearRatio = (10.0 / 60.0) * (20.0 / 28.0);
    public DifferentialDriveKinematics kinematics = new DifferentialDriveKinematics(Units.inchesToMeters(TRACK_WIDTH));

    // Odometry
    private static DifferentialDriveOdometry odometry;
    public Pose2d robotPosition;

    public static DifferentialDriveOdometry getOdometry() {
        return odometry;
    }

    public enum TeleopDriveType {
        ARCADE, CURVATURE
    }

    private static final int MAX_VELOCITY = 6370; // max velocity of velocity drive in rpm

    private static final double CORRECTION_MAX_STEER_SPEED = 0.5;
    LimelightValues limelightValues = new LimelightValues();

    VelocityDriveTalon velocityDrive = new VelocityDriveTalon(MAX_VELOCITY);
    CurvatureDrive curvatureDrive = new CurvatureDrive(TELEOP_DEAD_ZONE);
    ArcadeDrive arcadeDrive = curvatureDrive.getArcadeDrive();
    DriveDemand driveDemand = new DriveDemand(0, 0);

    private SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(.343, .0462, .00316);

    DriveTalon() {

        ShuffleboardData.driveModeChooser.addOption("Open Loop", DriveMode.OPEN_LOOP);
        ShuffleboardData.driveModeChooser.addOption("Velocity", DriveMode.VELOCITY);
        ShuffleboardData.teleopDriveTypeChooser.addOption("Arcade", TeleopDriveType.ARCADE);
        ShuffleboardData.teleopDriveTypeChooser.addOption("Curvature", TeleopDriveType.CURVATURE);
        arcade_x = new ArcsinFilter(new DeadzoneFilter(ARCADE_X_AXIS, TELEOP_DEAD_ZONE));
        arcade_y = new ArcsinFilter(new DeadzoneFilter(ARCADE_Y_AXIS, TELEOP_DEAD_ZONE));

        LEFT = (TalonFXWrapper) MotorConfigs.talons.get("driveLeft");
        RIGHT = (TalonFXWrapper) MotorConfigs.talons.get("driveRight");

//        LEFT.configEncoder(TalonFXFeedbackDevice.IntegratedSensor, PIDProfile.PRIMARY, 0);
//        RIGHT.configEncoder(TalonFXFeedbackDevice.IntegratedSensor, PIDProfile.PRIMARY, 0);

        DriveDemand.circumference = WHEEL_CIRCUMFERENCE_INCHES;

        odometry = new DifferentialDriveOdometry(new Rotation2d(pigeon.getHeading()));

//        velocityDrive.configureMotor(LEFT, MotorConfigs.motorConfigs.getTalons().get("driveLeft"));
//        velocityDrive.configureMotor(RIGHT, MotorConfigs.motorConfigs.getTalons().get("driveRight"));
    }

    private void teleopDrive(TeleopDriveType driveType) {
//        System.out.println("Teleop Drive");
        if (driveType == TeleopDriveType.ARCADE) {
            driveDemand = arcadeDrive.getDemand(arcade_y.get(), arcade_x.get());
            ;
        } else {
            driveDemand = curvatureDrive.getDemand(CURVATURE_FORWARD.get(), CURVATURE_REVERSE.get(), CURVATURE_STEER.get(), PIVOT_BUTTON.get());
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
        driveMode = ShuffleboardData.driveModeChooser.getSelected();
        if (driveMode == null) driveMode = DriveMode.OPEN_LOOP;
        teleopDrive(teleopDriveType);
    }

    public boolean checkSystem() {
        return false;
    }

    public void outputTelemetry() {
        SmartDashboard.putNumber("Left Encoder", LEFT.getEncoderPosition());
        SmartDashboard.putNumber("Right Encoder", RIGHT.getEncoderPosition());
        SmartDashboard.putNumber("Left Velocity", LEFT.getVelocity() * (600.0 / 2048));
        SmartDashboard.putNumber("Right Velocity", RIGHT.getVelocity() * (600.0 / 2048));
        SmartDashboard.putString("Control Drive Mode", driveMode.toString());
        SmartDashboard.putNumber("Gyro", pigeon.getHeading());
        SmartDashboard.putString("Odometry", odometry.getPoseMeters().toString());

        double left = driveDemand.getLeft();
        double right = driveDemand.getRight();
        if (driveMode == DriveMode.VELOCITY) {
            left = velocityDrive.getVelocityFromDemand(left);
            right = velocityDrive.getVelocityFromDemand(right);
        }
        SmartDashboard.putNumber("Left Demand", left);
        SmartDashboard.putNumber("Right Demand", right);
    }

    @Override
    public void onEnabledStart(double timestamp) {
        // TODO: 01/18/2020 verify true and false
        setBrakeMode(false);
    }

    @Override
    public void onDisabledStart(double timestamp) {
        setBrakeMode(true);
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
        pigeon.setFusedHeading(0);
    }

    @Override
    public synchronized void writePeriodicOutputs() {
        if (Robot.isAuto) {
//            SmartDashboard.putString("Demand", driveDemand.toString());
            LEFT.set(ControlMode.VELOCITY, driveDemand.getLeft() / gearRatio);
            RIGHT.set(ControlMode.VELOCITY, driveDemand.getRight() / gearRatio);
        } else if (driveMode == DriveMode.VELOCITY) {
            double leftVelocity = velocityDrive.getVelocityFromDemand(driveDemand.getLeft());
            double rightVelocity = velocityDrive.getVelocityFromDemand(driveDemand.getRight());
            //            LEFT.set(ControlMode.VELOCITY, leftVelocity, feedforward.calculate(leftVelocity / 600) / 1000);
//            RIGHT.set(ControlMode.VELOCITY, rightVelocity, feedforward.calculate(rightVelocity / 600) / 1000);
            LEFT.set(ControlMode.VELOCITY, leftVelocity);
            RIGHT.set(ControlMode.VELOCITY, rightVelocity);
        } else {
            LEFT.set(driveMode.controlMode, driveDemand.getLeft());
            RIGHT.set(driveMode.controlMode, driveDemand.getRight());
        }
    }

    @Override
    protected void readPeriodicInputs() {
        double leftDistance = Units.inchesToMeters(LEFT.getEncoderPosition() / 2048 * gearRatio * WHEEL_CIRCUMFERENCE_INCHES);
        double rightDistance = Units.inchesToMeters(RIGHT.getEncoderPosition() / 2048 * gearRatio * WHEEL_CIRCUMFERENCE_INCHES);
        robotPosition = odometry.update(Rotation2d.fromDegrees(-pigeon.getHeading()), leftDistance, rightDistance);
    }

    public synchronized void setBrakeMode(boolean brake) {
        NeutralMode mode = brake ? NeutralMode.Brake : NeutralMode.Coast;
        RIGHT.setNeutralMode(mode);
        LEFT.setNeutralMode(mode);
    }

    public void initAutonomous(Pose2d initialPose) {
        odometry.resetPosition(initialPose, initialPose.getRotation());
        pigeon.setFusedHeading(initialPose.getRotation().getDegrees());
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
