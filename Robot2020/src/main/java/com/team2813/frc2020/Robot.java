/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team2813.frc2020;

import com.ctre.phoenix.CANifier;
import com.team2813.frc2020.auto.AutoRoutine;
import com.team2813.frc2020.auto.Autonomous;
import com.team2813.frc2020.subsystems.Subsystem;
import com.team2813.frc2020.subsystems.Subsystems;
import com.team2813.frc2020.util.RobotTest;
import com.team2813.frc2020.util.ShuffleboardData;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.drive.DriveDemand;
import com.team2813.lib.util.CrashTracker;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.IOException;

import static com.team2813.frc2020.subsystems.Subsystems.*;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

    private static final double MIN_IDLE_VOLTAGE = 11.7;
    private static final double MIN_DISABLED_VOLTAGE = 12.0;
    private static boolean BATTERY_TOO_LOW = false;
    private final double WHEEL_DIAMETER = 6.0;

    public static Autonomous autonomous;

    private CANifier caNifier = new CANifier(0);
    public static boolean isAuto = false;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
        try {
            CrashTracker.logRobotInit();
            MotorConfigs.read();
            System.out.println("Motor Config Successful");
            Subsystems.initializeSubsystems();
            System.out.println("Subsystem Initialization Successful");
			autonomous = new Autonomous();
			System.out.println("Auto Constructed");
            Autonomous.addRoutines();
            System.out.println("AutoRoutine Initialization Successful");
            ShuffleboardData.init();

            DriveDemand.circumference = Math.PI * WHEEL_DIAMETER;
            for (Subsystem subsystem : allSubsystems) {
                LOOPER.addLoop(subsystem);
                subsystem.zeroSensors();
            }
        } catch (IOException e) {
            System.out.println("Something went wrong while reading config files!");
            CrashTracker.logThrowableCrash(e);
            e.printStackTrace();
            System.out.println("ERROR WHEN READING CONFIG");
            e.printStackTrace();
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

    /**
     * This function is called every robot packet, no matter the mode. Use
     * this for items like diagnostics that you want ran during disabled,
     * autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before
     * LiveWindow and SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
        boolean disabled = DriverStation.getInstance().isDisabled();
        double voltage = RobotController.getBatteryVoltage();
        SmartDashboard.putBoolean("Replace Battery if Red", disabled ? voltage > MIN_DISABLED_VOLTAGE : voltage > MIN_IDLE_VOLTAGE);

        Subsystems.outputTelemetry();
        BATTERY_TOO_LOW = disabled && voltage > MIN_DISABLED_VOLTAGE;
        SmartDashboard.putBoolean("Replace Battery if Red", disabled ? voltage > MIN_DISABLED_VOLTAGE : voltage > MIN_IDLE_VOLTAGE);
    }

    @Override
    public void disabledInit() {
        try {
            CrashTracker.logDisabledInit();
            LOOPER.setMode(RobotMode.DISABLED);
            LOOPER.start();
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

    /**
     * Put autonomous initialization code here
     */
    @Override
    public void autonomousInit() {
        isAuto = true;
        try {
            CrashTracker.logAutoInit();
//            Compressor compressor = new Compressor(); // FIXME: 11/02/2019 this shouldn't need to be here
//            compressor.start();
            for (Subsystem subsystem : allSubsystems) {
                subsystem.zeroSensors();
            }
            autonomous.run();
        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            throw t;
        }
    }

    @Override
    public void teleopInit() {
        try {
            System.out.println("teleopInit");
            isAuto = false;

            CrashTracker.logTeleopInit();
            LOOPER.setMode(RobotMode.ENABLED);
            LOOPER.start();

        } catch (Throwable t) {
            CrashTracker.logThrowableCrash(t);
            try {
                throw t;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        Subsystems.teleopControls();
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
        autonomous.periodic();
    }

    @Override
    public void testInit() {
        RobotTest robotTest = new RobotTest();
        robotTest.run();
    }

    public enum RobotMode {
        DISABLED, ENABLED
    }
}
