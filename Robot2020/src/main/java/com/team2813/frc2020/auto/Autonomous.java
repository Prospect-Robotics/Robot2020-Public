package com.team2813.frc2020.auto;

import com.team2813.frc2020.Robot;
import com.team2813.frc2020.subsystems.Subsystems;
import com.team2813.frc2020.util.ShuffleboardData;
import com.team2813.lib.auto.RamseteAuto;
import com.team2813.lib.auto.RamseteTrajectory;
import com.team2813.lib.drive.DriveDemand;

/**
 * method to run autonomous in {@link Robot} autonomousPeriodic
 *
 * @author Maharishi Rajarethenam
 */
public class Autonomous {

    private RamseteAuto ramseteAuto;
    private RamseteTrajectory trajectory;
    private DriveDemand prevDemand = new DriveDemand(0, 0);

    // this will be run periodically (usually to follow the path)
    public void periodic() {
        DriveDemand demand = ramseteAuto.getDemand(Subsystems.DRIVE.robotPosition);
        if (!demand.equals(prevDemand)) {
            Subsystems.DRIVE.setDemand(demand);
        }
        prevDemand = demand;
    }

    public void run(AutoRoutine routine) {
        System.out.println("Running auto routine " + routine.name);
        ramseteAuto = new RamseteAuto(Subsystems.DRIVE.kinematics, routine.getTrajectory());

        Subsystems.DRIVE.initAutonomous(ramseteAuto.initialPose());
        Subsystems.LOOPER.addAction(routine.getAction());
    }

    public void run() {
        run(ShuffleboardData.routineChooser.getSelected());
    }

    public static void addRoutines() {
        ShuffleboardData.autoModeChooser.addOption("Regular", AutoMode.REGULAR);
        ShuffleboardData.autoModeChooser.addOption("Galactic Search", AutoMode.GALACTIC_SEARCH);

        for (AutoRoutine routine : AutoRoutine.values()) {
            ShuffleboardData.routineChooser.addOption(routine.name, routine);
        }
    }
}
