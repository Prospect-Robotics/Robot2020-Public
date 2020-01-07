/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team2813.frc2020;

import com.team2813.frc2020.util.AutonomousPath;
import com.team2813.frc2020.util.ShuffleboardData;
import com.team2813.frc2020.subsystems.Subsystem;
import com.team2813.frc2020.subsystems.Subsystems;
import com.team2813.lib.config.MotorConfigs;
import com.team2813.lib.util.CrashTracker;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.io.IOException;

import static com.team2813.frc2020.subsystems.Subsystems.LOOPER;
import static com.team2813.frc2020.subsystems.Subsystems.allSubsystems;

public class Robot extends TimedRobot {
  private static final double MIN_IDLE_VOLTAGE = 11.7;
  private static final double MIN_DISABLED_VOLTAGE = 12.0;

  public static AutonomousPath chosenPath;

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    try {
      CrashTracker.logRobotInit();
      MotorConfigs.read();
      Subsystems.initializeSubsystems();
      ShuffleboardData.init();
      for (Subsystem subsystem : allSubsystems) {
        LOOPER.addLoop(subsystem);
        subsystem.zeroSensors();
      }
    } catch (IOException e) {
      System.out.println("Something went wrong while reading config files!");
      CrashTracker.logThrowableCrash(e);
      e.printStackTrace();
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
  }

  /**
   * Put autonomous initialization code here
   */
  @Override
  public void autonomousInit() {
    chosenPath = ShuffleboardData.pathChooser.getSelected();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {

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

  public enum RobotMode {
    DISABLED, ENABLED
  }
}
