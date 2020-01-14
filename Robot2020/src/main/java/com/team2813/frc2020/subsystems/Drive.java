package com.team2813.frc2020.subsystems;

import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.ControlType;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.controls.Axis;
import com.team2813.lib.controls.Button;
import com.team2813.lib.ctre.CTREException;
import com.team2813.lib.drive.ArcadeDrive;
import com.team2813.lib.drive.CurvatureDrive;
import com.team2813.lib.drive.DriveDemand;
import com.team2813.lib.drive.VelocityDriveSpark;
import com.team2813.lib.sparkMax.CANSparkMaxWrapper;
import com.team2813.lib.sparkMax.SparkMaxException;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
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
    private static final CANSparkMaxWrapper LEFT = MotorConfigs.sparks.get("driveLeft");
    private static final CANSparkMaxWrapper RIGHT = MotorConfigs.sparks.get("driveRight");
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


    private static final double CORRECTION_MAX_STEER_SPEED = 0.5;
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry tv = table.getEntry("tv");
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry camtranEntry = table.getEntry("camtran");

    private static final int MAX_VELOCITY = 18000; // max velocity of velocity drive in rpm
    VelocityDriveSpark velocityDrive = new VelocityDriveSpark(MAX_VELOCITY);
    CurvatureDrive curvatureDrive = new CurvatureDrive(TELEOP_DEAD_ZONE);
    ArcadeDrive arcadeDrive = curvatureDrive.getArcadeDrive();
    DriveDemand driveDemand = new DriveDemand(0, 0);

    private NetworkTableEntry velocityEntry = Shuffleboard.getTab("Tuning")
            .addPersistent("Bruh", 0).getEntry();
    private boolean velocityEnabled = velocityEntry.getNumber(0).intValue() == 1;
    private boolean velocityFailed = false;

    Drive() {
        try {
            velocityDrive.configureMotor(LEFT, MotorConfigs.motorConfigs.getSparks().get("driveLeft"));
            velocityDrive.configureMotor(RIGHT, MotorConfigs.motorConfigs.getSparks().get("driveRight"));

            // be sure they're inverted correctly
            LEFT.setInverted(LEFT.getConfig().getInverted());
            RIGHT.setInverted(RIGHT.getConfig().getInverted());
        } catch (SparkMaxException e) {
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

    @Override
    protected boolean checkSystem_() throws CTREException {
        return false;
    }

    @Override
    protected void outputTelemetry_() throws CTREException {

    }

    @Override
    protected void teleopControls_() throws CTREException, SparkMaxException {
        driveMode = DriveMode.OPEN_LOOP;
        teleopDrive(TELEOP_DRIVE_TYPE);
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

    protected synchronized void writePeriodicOutputs_() throws SparkMaxException, CTREException {
        if (!velocityFailed && velocityEnabled) {
            double leftVelocity = velocityDrive.getVelocityFromDemand(driveDemand.getLeft());
            double rightVelocity = velocityDrive.getVelocityFromDemand(driveDemand.getRight());
            LEFT.set(leftVelocity, ControlType.kSmartVelocity);
            RIGHT.set(rightVelocity, ControlType.kSmartVelocity);
        } else {
            LEFT.set(driveDemand.getLeft(), driveMode.controlType);
            RIGHT.set(driveDemand.getRight(), driveMode.controlType);
        }
    }

    public synchronized void setBrakeMode(boolean brake) {
        if (isBrakeMode != brake) {
            isBrakeMode = brake;
            IdleMode mode = brake ? IdleMode.kBrake : IdleMode.kCoast;
            try {
                RIGHT.setNeutralMode(mode);
                LEFT.setNeutralMode(mode);
            } catch (SparkMaxException e) {
                e.printStackTrace();
            }
        }
    }

    private enum DriveMode {
        OPEN_LOOP(ControlType.kDutyCycle),
        SMART_MOTION(ControlType.kSmartMotion),
        VELOCITY(ControlType.kVelocity);

        ControlType controlType;

        DriveMode(ControlType controlType) {
            this.controlType = controlType;
        }
    }
}
