package com.team2813.frc2020.auto;

import com.team2813.frc2020.Robot;
import com.team2813.frc2020.util.Limelight;
import com.team2813.frc2020.util.ShuffleboardData;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class GalacticSearch {
    private static final double TOLERANCE = 1;
    private Limelight limelight = Limelight.getInstance();

    public void run() {
        double angle = limelight.getValues().getTx();
        AutoRoutine routine = AutoRoutine.PATH_A_RED;
        if (Math.abs(PathIndicators.PATH_A_BLUE.indicatorAngle - angle) < TOLERANCE)
            routine = AutoRoutine.PATH_A_BLUE;
        if (Math.abs(PathIndicators.PATH_B_RED.indicatorAngle - angle) < TOLERANCE)
            routine = AutoRoutine.PATH_B_BLUE;
        if (Math.abs(PathIndicators.PATH_B_BLUE.indicatorAngle - angle) < TOLERANCE)
            routine = AutoRoutine.PATH_B_BLUE;
        Robot.autonomous.run(routine);
    }

    public enum PathIndicators {
        PATH_A_RED(0),
        PATH_A_BLUE(0),
        PATH_B_RED(0),
        PATH_B_BLUE(0);

        double indicatorAngle;

        PathIndicators(double indicatorAngle) {
            this.indicatorAngle = indicatorAngle;
        }
    }
}
