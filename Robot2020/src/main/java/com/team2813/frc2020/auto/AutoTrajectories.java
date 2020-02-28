package com.team2813.frc2020.auto;

import com.team2813.lib.auto.*;

import java.util.List;

public enum AutoTrajectories {
    FIVE_BALL_ENEMY(List.of(
            new PauseTrajectory(1, 0), // shoot 3 balls
            new GeneratedTrajectory("2-ball", true, 1),
            new PauseTrajectory(1, 2), // intake 2 balls, turn intake off
            new GeneratedTrajectory("go back", false, 3),
            new GeneratedTrajectory("return", false, 4),
            new PauseTrajectory(1, 5) // shoot 2 balls
    )),
    TEST_ROUTINE(List.of(
            new PauseTrajectory(0.5, 0),
            new GeneratedTrajectory("test", false, 1),
            new RotateTrajectory(180, 2),
            new GeneratedTrajectory("another test path", false, 3)
    )),
    THREE_BALL(List.of(
            new PauseTrajectory(1, 0), //shoot 3-balls
            new GeneratedTrajectory("3-ball prepare", false, 1),
            new PauseTrajectory(1, 2), //turn intake on
            new GeneratedTrajectory("3-ball", true, 3), //intake 3 balls
            new GeneratedTrajectory("2-ball 2", true, 4), //intake 2 balls
            new PauseTrajectory(1, 5) //turn intake off
    )),
    SIX_BALL(List.of(
            new PauseTrajectory(3.5, 0), // shoot 3 ball turn intake on
            new GeneratedTrajectory("3-ball trench", true, 1), // intake 3 balls
            new PauseTrajectory(.1, 2), //turn intake off
            new GeneratedTrajectory("back 3-ball", false, 3),
            new PauseTrajectory(1, 4) // shoot 3 balls
    )),
    EIGHT_BALL(List.of(
            new GeneratedTrajectory("3-ball trench", true, 0), // intake 2 balls
            new RotateTrajectory(0, 1),
            new GeneratedTrajectory("8-ball 1", true, 2), // intake 3 balls
            new GeneratedTrajectory("return 8-ball", false, 3) // intake 3 balls
    )),
    FIVE_BALL_TWO(List.of(
            new PauseTrajectory(1, 0), //turn intake on
            new GeneratedTrajectory("2-ball initiation line", true, 1), //intake 2 balls
            new RotateTrajectory(180, 2),
            new PauseTrajectory(1, 3), //turn intake off, shoot 5 balls
            new GeneratedTrajectory("general zone", false, 4)
    )),
    TEST_REVERSE(List.of(
            new GeneratedTrajectory("test reverse", true, 0)
    )),
    SERIES_TEST(List.of(
            new PauseTrajectory(150, 0)
    ));



    private RamseteTrajectory trajectory;

    AutoTrajectories(List<AutoTrajectory> trajectory) {
        this.trajectory = new RamseteTrajectory(trajectory);
    }

    public RamseteTrajectory getTrajectory() {
        return trajectory;
    }
}
