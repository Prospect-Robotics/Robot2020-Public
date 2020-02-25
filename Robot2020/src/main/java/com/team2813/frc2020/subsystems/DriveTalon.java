package com.team2813.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.team2813.frc2020.Robot;
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
public class DriveTalon extends Subsystem {

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
    private static final Button HOOD_BUTTON = SubsystemControlsConfig.getHoodButton();
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
    public Limelight limelight = new Limelight();

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

    VelocityDriveTalon velocityDrive = new VelocityDriveTalon(MAX_VELOCITY);
    public CurvatureDrive curvatureDrive = new CurvatureDrive(TELEOP_DEAD_ZONE);
    ArcadeDrive arcadeDrive = curvatureDrive.getArcadeDrive();
    DriveDemand driveDemand = new DriveDemand(0, 0);
    public DriveDemand getDriveDemand(){return driveDemand;}

    //    private SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(0.316, 0.0596, 0.0038); // gains in inches
//    private SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(0.016094, 0.003035, 0.000193); // gains in revolutions
    private SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(0.345, 2.32, 0.196); // gains in meters

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

        DriveDemand.circumference = WHEEL_CIRCUMFERENCE;

        pigeon.setYawToCompass();
        pigeon.setHeading(0);
        odometry = new DifferentialDriveOdometry(new Rotation2d(pigeon.getHeading()));

//        velocityDrive.configureMotor(LEFT, MotorConfigs.motorConfigs.getTalons().get("driveLeft"));
//        velocityDrive.configureMotor(RIGHT, MotorConfigs.motorConfigs.getTalons().get("driveRight"));
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
            driveDemand = velocityDrive.getVelocity(driveDemand); // convert from m/s to rpm
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
        SmartDashboard.putNumber("Left Error", driveDemand.getLeft() - leftVelocity);
        SmartDashboard.putNumber("Right Demand", driveDemand.getRight());
        SmartDashboard.putNumber("Right Error", driveDemand.getRight() - rightVelocity);
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
        pigeon.setHeading(0);
    }

    @Override
    public synchronized void writePeriodicOutputs() {
        if (driveMode == DriveMode.VELOCITY || Robot.isAuto) {
//            System.out.println(driveDemand);
            DriveDemand demand = Units2813.dtDemandToMotorDemand(driveDemand); // local variable for telemetry reasons

            System.out.println(driveDemand);
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
        NeutralMode mode = brake ? NeutralMode.Brake : NeutralMode.Coast;
        RIGHT.setNeutralMode(mode);
        LEFT.setNeutralMode(mode);
    }

    public void initAutonomous(Pose2d initialPose) {
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
