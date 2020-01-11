package com.team2813.frc2020.util;

import com.team2813.frc2020.actions.FunctionAction;
import com.team2813.frc2020.actions.SeriesAction;
import com.team2813.frc2020.subsystems.Subsystems;

/**
 * Automatic tests all robot functions
 * @author Abhineet Pal
 */
public class RobotTest {
    public void run() {
        SeriesAction action = new SeriesAction(
                new FunctionAction(Subsystems.DRIVE::driveForwardTest, true),
                new FunctionAction(Subsystems.DRIVE::driveBackwardsTest, true),
                new FunctionAction(Subsystems.DRIVE::driveRightTest, true),
                new FunctionAction(Subsystems.DRIVE::driveLeftTest, true)
        );
        Subsystems.LOOPER.addAction(action);
    }
}
