package com.team2813.frc2020.auto;

import com.team2813.lib.actions.*;
import com.team2813.lib.auto.AutoTrajectory;
import com.team2813.lib.auto.PauseTrajectory;
import com.team2813.lib.auto.RamseteTrajectory;


import java.util.List;

public enum AutoRoutine {
    BRUH("bruh", List.of(new PauseTrajectory(60)),
            new SeriesAction(
                    new WaitAction(1)));
//    BRUH("bruh", List.of(new PauseTrajectory(60)),
//            new SeriesAction(
//                    new FunctionAction(() -> DRIVE.setDemand(new DriveDemand(-.3, -.3)), true),
//                    new WaitAction(1),
//                    new FunctionAction(() -> DRIVE.setDemand(new DriveDemand(0, 0)), true)));


    public String name;
    public RamseteTrajectory trajectory;
    public Action action;

    AutoRoutine(String name, List<AutoTrajectory> trajectory, Action action) {
        this.name = name;
        this.trajectory = new RamseteTrajectory(trajectory);
        this.action = action;
    }


}
