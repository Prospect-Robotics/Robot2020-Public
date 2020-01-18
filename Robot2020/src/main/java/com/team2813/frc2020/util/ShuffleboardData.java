package com.team2813.frc2020.util;

import com.team2813.frc2020.subsystems.DriveTalon;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShuffleboardData {
    // define your smartdashboard entries here
    public static SendableChooser<AutonomousPath> pathChooser = new SendableChooser<>();
    public static SendableChooser<DriveTalon.DriveMode> driveModeChooser = new SendableChooser<>();
    public static SendableChooser<DriveTalon.TeleopDriveType> teleopDriveTypeChooser = new SendableChooser<>();

    public static void init() {
        SmartDashboard.putData("Auto Path", pathChooser);
        SmartDashboard.putData("Drive Mode", driveModeChooser);
        SmartDashboard.putData("Teleop Drive Type", teleopDriveTypeChooser);

    }
}
