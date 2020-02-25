package com.team2813.frc2020.auto;

import com.team2813.frc2020.Robot;
import com.team2813.frc2020.subsystems.Subsystems;
import com.team2813.frc2020.util.ShuffleboardData;
import com.team2813.lib.actions.Action;
import com.team2813.lib.actions.SeriesAction;
import com.team2813.lib.auto.GeneratedTrajectory;
import com.team2813.lib.auto.RamseteAuto;
import com.team2813.lib.auto.RamseteTrajectory;

import java.util.List;

/**
 * method to run autonomous in {@link Robot} autonomousPeriodic
 *
 * @author Maharishi Rajarethenam
 */
public class Autonomous {

    private RamseteAuto ramseteAuto;
    private RamseteTrajectory trajectory;

    // this will be run periodically (usually to follow the path)
    public void periodic() {
        Subsystems.DRIVE.setDemand(ramseteAuto.getDemand(Subsystems.DRIVE.robotPosition));
    }

    public void run() {
        AutoRoutine routine = ShuffleboardData.routineChooser.getSelected();
        ramseteAuto = new RamseteAuto(Subsystems.DRIVE.kinematics, routine.trajectory);

        Subsystems.DRIVE.initAutonomous(ramseteAuto.initialPose());
        Subsystems.LOOPER.addAction(routine.action);
    }

    public static void addRoutines() {
        int x = 1;
        for (AutoRoutine routine : AutoRoutine.values()) {
            ShuffleboardData.routineChooser.addOption(routine.name, routine);
        }
    }
}
