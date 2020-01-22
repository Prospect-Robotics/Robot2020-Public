package com.team2813.frc2020.auto;

import com.team2813.frc2020.util.ShuffleboardData;
import com.team2813.lib.actions.Action;
import com.team2813.lib.actions.SeriesAction;
import com.team2813.lib.actions.WaitAction;
import com.team2813.lib.auto.AutoTrajectory;
import com.team2813.lib.auto.GeneratedTrajectory;
import com.team2813.lib.auto.PauseTrajectory;
import com.team2813.lib.auto.RamseteTrajectory;

import java.util.Arrays;
import java.util.List;

public enum AutoRoutine {
    FIVE_BALL("5 Ball", List.of(
            new PauseTrajectory(0.1),
            new GeneratedTrajectory("2-ball", false),
            new GeneratedTrajectory("go back", true),
            new GeneratedTrajectory("return", false)
    ), new SeriesAction(new WaitAction(1)/* shoot ball, intake, shoot ball*/)),
    TEST_ROUTINE("Test", List.of(
            new PauseTrajectory(0.5),
            new GeneratedTrajectory("test", false)
    ), new SeriesAction(new WaitAction(1)));

    public String name;
    public RamseteTrajectory trajectory;
    public Action action;

    AutoRoutine(String name, List<AutoTrajectory> trajectory, Action action) {
        this.name = name;
        this.trajectory = new RamseteTrajectory(trajectory);
        this.action = action;
    }

    public static void addRoutines() {
        for (AutoRoutine routine : Arrays.asList(AutoRoutine.values())) {
            ShuffleboardData.routineChooser.addOption(routine.name, routine);
        }
    }
}
