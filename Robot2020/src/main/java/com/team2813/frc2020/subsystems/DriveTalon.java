package com.team2813.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.team2813.frc2020.util.ShuffleboardData;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.*;
import com.team2813.lib.drive.ArcadeDrive;
import com.team2813.lib.drive.CurvatureDrive;
import com.team2813.lib.drive.DriveDemand;
import com.team2813.lib.drive.VelocityDriveTalon;
import com.team2813.lib.motors.TalonFXWrapper;
import com.team2813.lib.motors.TalonWrapper;
import com.team2813.lib.motors.TalonWrapper.PIDProfile;
import com.team2813.lib.motors.interfaces.ControlMode;
import com.team2813.lib.util.LimelightValues;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static com.team2813.frc2020.subsystems.Subsystems.DRIVE;

/**
 * The Drive subsystem is the main subsystem for
 * the drive train, and handles both driver control
 * and autonomous path control.
 *
 * @author Grady Whelan
 * @author Samuel Li
 */
public class DriveTalon extends Subsystem {
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

    // Mode
    private static DriveMode driveMode = DriveMode.OPEN_LOOP;
    private static TeleopDriveType teleopDriveType = TeleopDriveType.CURVATURE;

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

    private NetworkTableEntry velocityEntry = Shuffleboard.getTab("Tuning")
              .addPersistent("Velocity Drive", 0).getEntry();
    private boolean velocityEnabled = velocityEntry.getNumber(0).intValue() == 1;
    private boolean velocityFailed = false;

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
//        System.out.println("Teleop Controls");
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
    }

    @Override
    public synchronized void writePeriodicOutputs() {
        if (driveMode == DriveMode.VELOCITY) {
            double leftVelocity = velocityDrive.getVelocityFromDemand(driveDemand.getLeft());
            double rightVelocity = velocityDrive.getVelocityFromDemand(driveDemand.getRight());
            SmartDashboard.putNumber("Left Feedforward", feedforward.calculate(leftVelocity / 600) / 1000);
//            LEFT.set(ControlMode.VELOCITY, leftVelocity, feedforward.calculate(leftVelocity / 600) / 1000);
//            RIGHT.set(ControlMode.VELOCITY, rightVelocity, feedforward.calculate(rightVelocity / 600) / 1000);
            LEFT.set(ControlMode.VELOCITY, leftVelocity);
            RIGHT.set(ControlMode.VELOCITY, rightVelocity);
        } else {
            LEFT.set(driveMode.controlMode, driveDemand.getLeft());
            RIGHT.set(driveMode.controlMode, driveDemand.getRight());
        }
    }

    public synchronized void setBrakeMode(boolean brake) {
        NeutralMode mode = brake ? NeutralMode.Brake : NeutralMode.Coast;
        RIGHT.setNeutralMode(mode);
        LEFT.setNeutralMode(mode);
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