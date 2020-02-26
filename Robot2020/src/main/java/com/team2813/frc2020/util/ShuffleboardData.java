package com.team2813.frc2020.util;

import com.team2813.frc2020.auto.AutoRoutineActions;
import com.team2813.frc2020.subsystems.Drive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShuffleboardData {
    // define your smartdashboard entries here
    public static SendableChooser<AutoRoutineActions> routineChooser = new SendableChooser<>();
    public static SendableChooser<Drive.DriveMode> driveModeChooser = new SendableChooser<>();
    public static SendableChooser<Drive.TeleopDriveType> teleopDriveTypeChooser = new SendableChooser<>();

    public static void init() {
        SmartDashboard.putData("Auto Routine", routineChooser);
        SmartDashboard.putData("Drive Mode", driveModeChooser);
        SmartDashboard.putData("Teleop Drive Type", teleopDriveTypeChooser);
    }
}
