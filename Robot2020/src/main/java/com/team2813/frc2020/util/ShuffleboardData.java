package com.team2813.frc2020.util;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShuffleboardData {
    // define your smartdashboard entries here
    public static SendableChooser<AutonomousPath> pathChooser = new SendableChooser<>();

    public static void init() {
        SmartDashboard.putData("Auto Path", pathChooser);
    }
}
