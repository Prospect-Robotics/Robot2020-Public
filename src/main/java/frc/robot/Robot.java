/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.*;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * methods corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    private Joystick logitech = new Joystick(0);

    //    private VictorSPX victor = new VictorSPX(11);
    private CANSparkMax hood = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless);
    private CANEncoder hoodEncoder = hood.getAlternateEncoder(AlternateEncoderType.kQuadrature, 1);
    private CANPIDController hoodPid = hood.getPIDController();

    private CANSparkMax shooter = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
    private CANSparkMax shooterSlave = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);
    private CANEncoder shooterEncoder = shooter.getEncoder();
    private CANPIDController shooterPid = shooter.getPIDController();

    private NetworkTableEntry shooterVelocity = Shuffleboard.getTab("Tuning")
            .addPersistent("Flywheel Speed", 0)
            .getEntry();


    /**
     * This method is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
        shooterSlave.follow(shooter, true);

        hoodEncoder.setPosition(0);

        hoodPid.setFeedbackDevice(hoodEncoder);
        hoodPid.setP(0.0002);
//        hoodPid.setP(0.03);
        hoodPid.setD(.00001);
        hoodPid.setSmartMotionMaxVelocity(10000, 0);
        hoodPid.setSmartMotionMaxAccel(30000, 0);
        hoodPid.setSmartMotionAllowedClosedLoopError(.03, 0);

        shooterPid.setFeedbackDevice(shooterEncoder);
        shooterPid.setP(0.0001);
//        shooterPid.setD(1);

//        pid.setOutputRange()
    }

    /**
     * This method is called every robot packet, no matter the mode. Use
     * this for items like diagnostics that you want ran during disabled,
     * autonomous, teleoperated and test.
     * <p>
     * This runs after the mode specific periodic methods, but before
     * LiveWindow and SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
        SmartDashboard.putNumber("Hood Position", hoodEncoder.getPosition());
        SmartDashboard.putNumber("Shooter Velocity", shooterEncoder.getVelocity());
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable
     * chooser code works with the Java SmartDashboard. If you prefer the
     * LabVIEW Dashboard, remove all of the chooser code and uncomment the
     * getString line to get the auto name from the text box below the Gyro
     * <p>
     * You can add additional auto modes by adding additional comparisons to
     * the switch structure below with additional strings. If using the
     * SendableChooser make sure to add them to the chooser code above as well.
     */
    @Override
    public void autonomousInit() {
    }

    /**
     * This method is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopInit() {
        hoodEncoder.setPosition(0);
    }

    /**
     * This method is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        if (logitech.getRawButton(2)) {
            hoodEncoder.setPosition(0);
        }

        // 32.57 revolutions of neo 550
        double fineControl = Math.abs(logitech.getRawAxis(3));
        if (fineControl < 0.01)
            fineControl = 0;
//        double position = 0;
//        double max = 32.5;
//        if (logitech.getRawButton(1)) position = max * .33;
//        if (logitech.getRawButton(4)) position = max * .66;
//        if (logitech.getRawButton(3)) position = max;
//        pid.setReference(position, ControlType.kSmartMotion);
        hoodPid.setReference(fineControl * 47.5, ControlType.kSmartMotion);
//        hoodPid.setReference(fineControl, ControlType.kDutyCycle);
//        pid.setReference(Math.abs(logitech.getRawAxis(3)) * 4096, ControlType.kSmartMotion);

        if (logitech.getRawButton(6)) {
            shooterPid.setReference(500, ControlType.kSmartVelocity);
        } else {
            shooterPid.setReference(-Math.abs(logitech.getRawAxis(1)), ControlType.kDutyCycle);
//            shooter.set(-Math.abs(logitech.getRawAxis(1)));
        }
//        victor.set(ControlMode.PercentOutput, -logitech.getRawAxis(1));
    }

    /**
     * This method is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }
}
