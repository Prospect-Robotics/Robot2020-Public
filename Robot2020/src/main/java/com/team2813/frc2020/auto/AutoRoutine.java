package com.team2813.frc2020.auto;

import com.team2813.frc2020.util.ShuffleboardData;
import com.team2813.lib.actions.Action;
import com.team2813.lib.actions.SeriesAction;
import com.team2813.lib.actions.WaitAction;
import com.team2813.lib.auto.AutoTrajectory;
import com.team2813.lib.auto.GeneratedTrajectory;
import com.team2813.lib.auto.PauseTrajectory;
import com.team2813.lib.auto.RotateTrajectory;
import com.team2813.lib.auto.RamseteTrajectory;


import java.util.List;

public enum AutoRoutine {
    FIVE_BALL_ENEMY("5 Ball Enemy", List.of(
            new PauseTrajectory(1), // shoot 3 balls
            new GeneratedTrajectory("2-ball", true),
            new PauseTrajectory(1), // intake 2 balls, turn intake off
            new GeneratedTrajectory("go back", false),
            new GeneratedTrajectory("return", false),
            new PauseTrajectory(1) // shoot 2 balls
    ), new SeriesAction(new WaitAction(1)/* shoot ball, intake, shoot ball*/)),
    TEST_ROUTINE("Test", List.of(
            new PauseTrajectory(0.5),
            new GeneratedTrajectory("test", false)
    ), new SeriesAction(new WaitAction(1))),
    THREE_BALL("3-ball", List.of(
            new PauseTrajectory(1), //shoot 3-balls
            new GeneratedTrajectory("3-ball prepare", false),
            new PauseTrajectory(1), //turn intake on
            new GeneratedTrajectory("3-ball", true), //intake 3 balls
            new GeneratedTrajectory("2-ball 2", true), //intake 2 balls
            new PauseTrajectory(1) //turn intake off
    ), new SeriesAction(new WaitAction(1))),
    SIX_BALL("6-ball", List.of(
            new PauseTrajectory(1), // shoot 3 ball turn intake on
            new GeneratedTrajectory("3-ball trench", true), // intake 3 balls
            new PauseTrajectory(1), //turn intake off
            new GeneratedTrajectory("back 3-ball", false),
            new PauseTrajectory(1) // shoot 3 balls
    ), new SeriesAction(new WaitAction(1))),
    EIGHT_BALL("8-ball", List.of(
            new GeneratedTrajectory("back trench", true),
            new PauseTrajectory(1), // turn intake on
            new GeneratedTrajectory("2-ball shield generator", true), // intake 2 balls
            new GeneratedTrajectory("3-ball 2", true), // intake 3 balls
            new PauseTrajectory(1), // turn intake off
            new GeneratedTrajectory("return 8-ball", false),
            new PauseTrajectory(1) // shoot 5 balls
    ), new SeriesAction(new WaitAction(1))),
    FIVE_BALL_TWO("5-ball 2", List.of(
            new PauseTrajectory(1), //turn intake on
            new GeneratedTrajectory("2-ball initiation line", true), //intake 2 balls
            new RotateTrajectory(180),
            new PauseTrajectory(1), //turn intake off, shoot 5 balls
            new GeneratedTrajectory("general zone", false)
    ), new SeriesAction(new WaitAction(1)));

    public String name;
    public RamseteTrajectory trajectory;
    public Action action;

    AutoRoutine(String name, List<AutoTrajectory> trajectory, Action action) {
        this.name = name;
        this.trajectory = new RamseteTrajectory(trajectory);
        this.action = action;
    }


}
