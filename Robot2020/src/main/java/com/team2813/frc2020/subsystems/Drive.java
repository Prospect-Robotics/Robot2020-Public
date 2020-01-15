package com.team2813.frc2020.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Axis;
import com.team2813.lib.controls.Button;
import com.team2813.lib.ctre.CTREException;
import com.team2813.lib.ctre.TalonWrapper;
import com.team2813.lib.drive.ArcadeDrive;
import com.team2813.lib.drive.CurvatureDrive;
import com.team2813.lib.drive.DriveDemand;
import com.team2813.lib.drive.VelocityDriveTalon;
import com.team2813.lib.util.LimelightValues;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

/**
 * The Drive subsystem is the main subsystem for
 * the drive train, and handles both driver control
 * and autonomous path control.
 *
 * @author Grady Whelan
 * @author Samuel Li
 */
public class Drive extends Subsystem {
    // Motor Controllers
    private static final TalonWrapper LEFT = MotorConfigs.talons.get("driveLeft");
    private static final TalonWrapper RIGHT = MotorConfigs.talons.get("driveRight");
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
    private static final TeleopDriveType TELEOP_DRIVE_TYPE = TeleopDriveType.CURVATURE;
    private static final Button AUTO_BUTTON = SubsystemControlsConfig.getAutoButton();

    // Mode
    private static DriveMode driveMode = DriveMode.OPEN_LOOP;

    public enum TeleopDriveType {
        ARCADE, CURVATURE
    }

    private static final int MAX_VELOCITY = 18000; // max velocity of velocity drive in rpm

    private static final double CORRECTION_MAX_STEER_SPEED = 0.5;
    LimelightValues limelightValues = new LimelightValues();

    VelocityDriveTalon velocityDrive = new VelocityDriveTalon(MAX_VELOCITY);
    CurvatureDrive curvatureDrive = new CurvatureDrive(TELEOP_DEAD_ZONE);
    ArcadeDrive arcadeDrive = curvatureDrive.getArcadeDrive();
    DriveDemand driveDemand = new DriveDemand(0, 0);

    private NetworkTableEntry velocityEntry = Shuffleboard.getTab("Tuning")
            .addPersistent("Bruh", 0).getEntry();
    private boolean velocityEnabled = velocityEntry.getNumber(0).intValue() == 1;
    private boolean velocityFailed = false;

    Drive() {
        try {
            velocityDrive.configureMotor(LEFT, MotorConfigs.motorConfigs.getTalons().get("driveLeft"));
            velocityDrive.configureMotor(RIGHT, MotorConfigs.motorConfigs.getTalons().get("driveRight"));

            // be sure they're inverted correctly
//            LEFT.setInverted(LEFT.getConfig().getInverted());
//            RIGHT.setInverted(RIGHT.getConfig().getInverted());
        } catch (CTREException e) {
            velocityFailed = true;
            e.printStackTrace();
        }
    }

    private void teleopDrive(TeleopDriveType driveType) {
        if (driveType == TeleopDriveType.ARCADE) {
            driveDemand = arcadeDrive.getDemand(ARCADE_Y_AXIS.get(), ARCADE_X_AXIS.get());
        } else if (driveType == TeleopDriveType.CURVATURE) {
            driveDemand = curvatureDrive.getDemand(CURVATURE_FORWARD.get(), CURVATURE_REVERSE.get(), CURVATURE_STEER.get(), PIVOT_BUTTON.get());
        }
    }

    public void driveForwardTest(){
        driveDemand = new DriveDemand(MAX_VELOCITY, MAX_VELOCITY);
    }

    public void driveBackwardsTest(){
        driveDemand = new DriveDemand(-MAX_VELOCITY, -MAX_VELOCITY);
    }

    public void driveRightTest(){
        driveDemand = new DriveDemand(MAX_VELOCITY, -MAX_VELOCITY);
    }

    public void driveLeftTest(){
        driveDemand = new DriveDemand(-MAX_VELOCITY, MAX_VELOCITY);
    }

    @Override
    protected void teleopControls_() {
        driveMode = DriveMode.OPEN_LOOP;
        teleopDrive(TELEOP_DRIVE_TYPE);
    }

    @Override
    protected boolean checkSystem_() throws CTREException {
        return false;
    }

    @Override
    protected void outputTelemetry_() throws CTREException {

    }



    @Override
    protected void onEnabledStart_(double timestamp) throws CTREException {
//		setBrakeMode(false);
    }

    @Override
    protected void onEnabledLoop_(double timestamp) throws CTREException {
    }

    @Override
    protected void onEnabledStop_(double timestamp) throws CTREException {
    }


    protected synchronized void writePeriodicOutputs_() throws CTREException {
        if (!velocityFailed && velocityEnabled) {
            double leftVelocity = velocityDrive.getVelocityFromDemand(driveDemand.getLeft());
            double rightVelocity = velocityDrive.getVelocityFromDemand(driveDemand.getRight());
            LEFT.set(ControlMode.Velocity, leftVelocity);
            RIGHT.set(ControlMode.Velocity, rightVelocity);
        } else {
            LEFT.set(driveMode.controlMode,driveDemand.getLeft());
            RIGHT.set(driveMode.controlMode,driveDemand.getRight());
        }
    }

    public synchronized void setBrakeMode(boolean brake) {
        if (isBrakeMode != brake) {
            isBrakeMode = brake;
            NeutralMode mode = brake ? NeutralMode.Brake : NeutralMode.Coast;
            try {
                RIGHT.setNeutralMode(mode);
                LEFT.setNeutralMode(mode);
            } catch (CTREException e) {
                e.printStackTrace();
            }
        }
    }

    private enum DriveMode {
        OPEN_LOOP(ControlMode.PercentOutput),
        MOTION_MAGIC(ControlMode.MotionMagic),
        VELOCITY(ControlMode.Velocity);

        ControlMode controlMode;

        DriveMode(ControlMode controlMode) {
            this.controlMode = controlMode;
        }
    }
}
