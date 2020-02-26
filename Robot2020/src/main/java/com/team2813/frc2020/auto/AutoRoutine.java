package com.team2813.frc2020.auto;

import com.team2813.frc2020.subsystems.Magazine;
import com.team2813.frc2020.subsystems.Shooter;
import com.team2813.frc2020.subsystems.Subsystems;
import com.team2813.frc2020.util.AutoAimAction;
import com.team2813.frc2020.util.ShuffleboardData;
import com.team2813.lib.actions.*;
import com.team2813.lib.auto.AutoTrajectory;
import com.team2813.lib.auto.GeneratedTrajectory;
import com.team2813.lib.auto.PauseTrajectory;
import com.team2813.lib.auto.RotateTrajectory;
import com.team2813.lib.auto.RamseteTrajectory;
import com.team2813.lib.drive.DriveDemand;

import java.util.List;

import static com.team2813.frc2020.subsystems.Subsystems.*;

public enum AutoRoutine {
    FIVE_BALL_ENEMY("5 Ball Enemy", List.of(
            new PauseTrajectory(1, 0), // shoot 3 balls
            new GeneratedTrajectory("2-ball", true, 1),
            new PauseTrajectory(1, 2), // intake 2 balls, turn intake off
            new GeneratedTrajectory("go back", false, 3),
            new GeneratedTrajectory("return", false, 4),
            new PauseTrajectory(1, 5) // shoot 2 balls
    ), new SeriesAction(new AutoAimAction()/* shoot ball, intake, shoot ball*/)),
    TEST_ROUTINE("Test", List.of(
            new PauseTrajectory(0.5, 0),
            new GeneratedTrajectory("test", false, 1),
            new RotateTrajectory(180, 2),
            new GeneratedTrajectory("another test path", false, 3)
    ), new SeriesAction(new WaitAction(1))),
    THREE_BALL("3-ball", List.of(
            new PauseTrajectory(1, 0), //shoot 3-balls
            new GeneratedTrajectory("3-ball prepare", false, 1),
            new PauseTrajectory(1, 2), //turn intake on
            new GeneratedTrajectory("3-ball", true, 3), //intake 3 balls
            new GeneratedTrajectory("2-ball 2", true, 4), //intake 2 balls
            new PauseTrajectory(1, 5) //turn intake off
    ), new SeriesAction(new WaitAction(1))),
    SIX_BALL("6-ball", List.of(
            new PauseTrajectory(1, 0), // shoot 3 ball turn intake on
            new GeneratedTrajectory("3-ball trench", true, 1), // intake 3 balls
            new PauseTrajectory(1, 2), //turn intake off
            new GeneratedTrajectory("back 3-ball", false, 3),
            new PauseTrajectory(1, 4) // shoot 3 balls
    ), new SeriesAction(new WaitAction(1))),
    EIGHT_BALL("8-ball", List.of(
            new GeneratedTrajectory("back trench", true, 0),
            new PauseTrajectory(1, 1), // turn intake on
            new GeneratedTrajectory("2-ball shield generator", true, 2), // intake 2 balls
            new GeneratedTrajectory("3-ball 2", true, 3), // intake 3 balls
            new PauseTrajectory(1, 4), // turn intake off
            new GeneratedTrajectory("return 8-ball", false, 5),
            new PauseTrajectory(1, 6) // shoot 5 balls
    ), new SeriesAction(new WaitAction(1))),
    FIVE_BALL_TWO("5-ball 2", List.of(
            new PauseTrajectory(1, 0), //turn intake on
            new GeneratedTrajectory("2-ball initiation line", true, 1), //intake 2 balls
            new RotateTrajectory(180, 2),
            new PauseTrajectory(1, 3), //turn intake off, shoot 5 balls
            new GeneratedTrajectory("general zone", false, 4)
    ), new SeriesAction(new WaitAction(1))),
    TEST_REVERSE("test reverse", List.of(
            new GeneratedTrajectory("test reverse", true, 0)
    ), new SeriesAction(new WaitAction(1))),
    SERIES_TEST("Auto Aim Test", List.of(
            new PauseTrajectory(150, 0)
    ), new SeriesAction(
            new AutoAimAction()
    ));


    public String name;
    public RamseteTrajectory trajectory;
    public Action action;

    AutoRoutine(String name, List<AutoTrajectory> trajectory, Action action) {
        this.name = name;
        this.trajectory = new RamseteTrajectory(trajectory);
        this.action = action;
    }


}
